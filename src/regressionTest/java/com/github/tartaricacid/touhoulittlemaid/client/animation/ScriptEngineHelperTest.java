package com.github.tartaricacid.touhoulittlemaid.client.animation;

import org.junit.Test;
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptException;
import java.util.HashMap;

import static org.junit.Assert.*;

public class ScriptEngineHelperTest {
    private Object evaluate(String source) throws ScriptException {
        return ScriptEngineHelper.runScript(source, ScriptEngineHelper.getEngine().createBindings());
    }

    @Test
    public void originalUnfilteredEngineExposesProcessExecutionClasses() throws Exception {
        // Resolve the class only. Never run an operating-system command in a security test.
        assertNotNull(new NashornScriptEngineFactory().getScriptEngine().eval("Java.type('java.lang.Runtime')"));
    }

    @Test
    public void rejectsArbitraryJavaClasses() throws Exception {
        for (String name : new String[]{"java.lang.Runtime", "java.lang.ProcessBuilder", "java.io.File",
                "java.net.URL", "java.lang.Class", "javax.script.ScriptEngineManager",
                "org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory"}) {
            try {
                evaluate("Java.type('" + name + "')");
                fail("Class must be blocked: " + name);
            } catch (ScriptException | RuntimeException expected) {
                // Nashorn can report a denied class through either exception type.
            }
        }
    }

    @Test
    public void removesIoAndEngineEscapeGlobals() throws Exception {
        assertEquals(true, evaluate("""
                ['load', 'loadWithNewGlobal', 'exit', 'quit', 'readFully', 'readLine',
                 '$EXEC', 'engine', 'context', '__noSuchProperty__'].every(function (name) {
                    return typeof this[name] === 'undefined';
                }, this)
                """));
        assertEquals("undefined", evaluate("""
                Object.defineProperty(this, 'escape', {get: function () { return this.engine; }});
                Function('return typeof engine')()
                """));
    }

    @Test
    public void cannotRestoreForbiddenGlobals() throws Exception {
        assertEquals("undefined", evaluate("""
                delete this.load;
                this.load = function () { return 'unsafe'; };
                typeof this.load
                """));
    }

    @Test
    public void preventsReflectionOnHostObjects() throws Exception {
        Bindings bindings = ScriptEngineHelper.getEngine().createBindings();
        bindings.put("modelMap", new HashMap<>());
        try {
            ScriptEngineHelper.runScript("modelMap.getClass().forName('java.lang.Runtime')", bindings);
            fail("Reflection must be blocked");
        } catch (ScriptException | RuntimeException expected) {
        }
    }

    @Test
    public void supportsAnimationFunctionsAndModelMapAccess() throws Exception {
        Object animation = evaluate("""
                var GlWrapper = Java.type('com.github.tartaricacid.touhoulittlemaid.client.animation.script.GlWrapper');
                var JMath = Java.type('java.lang.Math');
                function animation(entity, swing, amount, age, yaw, pitch, scale, modelMap) {
                    modelMap.put('angle', JMath.sin(age) + Math.cos(age));
                }
                ({animation: animation});
                """);
        HashMap<String, Object> modelMap = new HashMap<>();
        ((Invocable) ScriptEngineHelper.getEngine()).invokeMethod(animation, "animation", null, 0, 0, 0, 0, 0, 1, modelMap);
        assertEquals(1.0, ((Number) modelMap.get("angle")).doubleValue(), 0.0001);
    }

    @Test
    public void isolatesAnimationGlobalVariables() throws Exception {
        evaluate("var privateToThisAnimation = 7;");
        assertEquals("undefined", evaluate("typeof privateToThisAnimation"));
    }
}
