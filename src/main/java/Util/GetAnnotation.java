package Util;

import Events.Command;
import Events.Module;
import Main.Fast;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
public class GetAnnotation implements Fast{
    /**
     * Parse Class Anotation to Method
     * @param clazz Class
     * @return A Collection of Annotation and Methods
     */
    public static HashMap<Command, Method> getAnnotation(Class clazz){
        HashMap<Command, Method> allannotation = new HashMap<>();
        try {
                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    Command annotation = method.getAnnotation(Command.class);
                    if (annotation != null) {
                        Console.debug("Method added as Command: " + method.getName());
                        allannotation.put(annotation, method);
                    }
                }
        } catch (Exception ex) {
            Console.error(String.format(LANG.getTranslation("annotation_error"), clazz.getName(), ex.getMessage()));
        }
        return allannotation;
    }
}
