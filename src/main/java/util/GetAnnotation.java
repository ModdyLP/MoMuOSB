package util;

import events.Command;
import main.Fast;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

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
    public static HashMap<Command, Method> getCommandAnnotation(Class clazz){
        HashMap<Command, Method> allannotation = new HashMap<>();
        try {
                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    Command annotation = method.getAnnotation(Command.class);
                    if (annotation != null) {
                        if (method.getParameters().length == 2) {
                            if (method.getParameters()[0].getParameterizedType().equals(MessageReceivedEvent.class) && method.getParameters()[1].getParameterizedType().equals(String[].class)) {
                                Console.debug("Method added as Command: " + method.getName());
                                allannotation.put(annotation, method);
                            } else {
                                Console.error("Method has no valid Parameters: "+method.getName());
                            }
                        } else {
                            Console.error("Method has no valid Parameters Count: "+method.getName());
                        }
                    }
                }
        } catch (Exception ex) {
            Console.error(String.format(LANG.getTranslation("annotation_error"), clazz.getName(), ex.getMessage()));
        }
        return allannotation;
    }
}
