package com.github.tartaricacid.touhoulittlemaid.network;

import java.lang.reflect.Method;

public class ClientPacketHelper {
    public static void handleClient(String handlerClassName, String methodName, Object message) {
        try {
            Class<?> clazz = Class.forName(handlerClassName);
            Method method = clazz.getMethod(methodName, message.getClass());
            method.invoke(null, message);
        } catch (Exception ignore) {
        }
    }
}