package Util;

import Events.Command;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
public class GetAnnotation {
    public static HashMap<Command, Method> getAnnotation(Class clazz){
        HashMap<Command, Method> allannotation = new HashMap<>();
        try {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method: methods) {
                Command annotation = method.getAnnotation(Command.class);
                if (annotation != null) {
                    Console.debug("Annotation added of Method: "+method.getName());
                    allannotation.put(annotation, method);
                }
            }
        } catch (Exception ex) {
            Console.error("Invalid Annotation in Module"+clazz.getName());
        }
        return allannotation;
    }
}
