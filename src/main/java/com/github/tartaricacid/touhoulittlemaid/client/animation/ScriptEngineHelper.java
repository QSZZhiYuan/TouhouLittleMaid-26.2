package com.github.tartaricacid.touhoulittlemaid.client.animation;

import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.Set;

/** Runs resource-pack animations with no access to arbitrary Java classes or script I/O. */
public final class ScriptEngineHelper {
    private static final Set<String> ANIMATION_CLASSES = Set.of(
            "com.github.tartaricacid.touhoulittlemaid.client.animation.script.GlWrapper",
            "java.lang.Math");
    // A ClassFilter also makes Nashorn reject reflection on objects passed to scripts.
    private static final ScriptEngine ENGINE = new NashornScriptEngineFactory().getScriptEngine(
            new String[]{"--no-syntax-extensions"}, ScriptEngineHelper.class.getClassLoader(),
            ANIMATION_CLASSES::contains);
    private static final String RESTRICT_GLOBALS = """
            (function (global) {
                ['load', 'loadWithNewGlobal', 'exit', 'quit', 'readFully', 'readLine',
                 '$EXEC', '$ENV', '$OPTIONS', '$OUT', '$ERR', '$EXIT', 'engine', 'context',
                 '__noSuchProperty__'].forEach(function (name) {
                    Object.defineProperty(global, name, {
                        value: undefined, writable: false, configurable: false
                    });
                });
            })(this);
            """;

    private ScriptEngineHelper() {
    }

    public static ScriptEngine getEngine() {
        return ENGINE;
    }

    public static Object runScript(String script, Bindings bindings) throws ScriptException {
        // Each animation has its own global scope; restrict it before any pack code executes.
        ENGINE.eval(RESTRICT_GLOBALS, bindings);
        return ENGINE.eval(script, bindings);
    }
}
