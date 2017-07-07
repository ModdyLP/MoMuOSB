package util;

import events.Command;
import events.Module;
import events.RegisterCommands;
import storage.LanguageInterface;
import storage.LanguageMethod;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.lang.reflect.Method;
import java.util.Arrays;
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
    public static HashMap<Command, Method> getCommandAnnotation(Class clazz, Module instance){
        HashMap<Command, Method> allannotation = new HashMap<>();
        Console.debug("----------------Module: "+clazz.getName()+"----------------");
        try {
                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    Command annotation = method.getAnnotation(Command.class);
                    LanguageMethod language = method.getAnnotation(LanguageMethod.class);
                    if (annotation != null) {
                        if (method.getParameters().length == 2) {
                            if (method.getParameters()[0].getParameterizedType().equals(MessageReceivedEvent.class) && method.getParameters()[1].getParameterizedType().equals(String[].class)) {
                                Console.debug("Command: " + annotation.prefix()+annotation.command()+" in "+method.getName());
                                allannotation.put(annotation, method);
                                PERM.addPermission(annotation);
                            } else {
                                Console.error("Method has no valid Parameters: "+method.getName());
                            }
                        } else {
                            Console.error("Method has no valid Parameters Count: "+method.getName());
                        }
                    } else if (language != null) {
                        Console.debug("Language: " + language.languagestringcount()+" in "+method.getName()+"   "+clazz.getName()+"   "+instance.getClass().getName());
                        COMMAND.languages.put(language, method);
                        COMMAND.langinstances.put(language, instance);
                    }
                }
        } catch (Exception ex) {
            Console.error(String.format(LANG.getTranslation("annotation_error"), clazz.getName(), ex.getMessage()));
            ex.printStackTrace();
        }
        return allannotation;
    }
}
