package events;

import org.tritonus.share.ArraySet;
import storage.LanguageMethod;
import util.Console;
import util.GetAnnotation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by ModdyLP on 01.07.2017. Website: https://moddylp.de/
 */
public class CommandManager {
    private HashMap<Command, Method> modules = new HashMap<>();
    private HashMap<Command, Module> instances = new HashMap<>();

    public HashMap<LanguageMethod, Method> languages = new HashMap<>();
    public HashMap<LanguageMethod, Module> langinstances = new HashMap<>();

    private static ArrayList<String> prefixe = new ArrayList<>();

    private static CommandManager instance;

    public static CommandManager getInstance() {
        if (instance == null) {
            instance = new CommandManager();
        }
        return instance;
    }

    public void registerCommand(Class module, Module instance) {
        HashMap<Command, Method> annotations = GetAnnotation.getCommandAnnotation(module, instance);
        for (Command anno : annotations.keySet()) {
            if (!getAllCommandsAsString().contains(anno.command())) {
                if (!getAllAliasAsString().contains(anno.alias())) {
                    modules.put(anno, annotations.get(anno));
                    instances.put(anno, instance);
                    prefixe.add(anno.prefix());
                } else {
                    Console.error("Duplicate Alias: "+anno.alias());
                }
            } else {
                Console.error("Duplicate Command: "+anno.command());
            }
        }

    }
    public HashMap<Command, Module> getInstances() {
        return instances;
    }
    public HashMap<Command, Method> getModules() {
        return modules;
    }
    public Set<Command> getAllCommands() {
        return modules.keySet();
    }
    public Set<String> getAllCommandsAsString() {
        Set<String> commandsAsString = new ArraySet<>();
        for (Command element: getAllCommands()) {
            commandsAsString.add(element.command());
        }
        return commandsAsString;
    }
    public Set<String> getAllAliasAsString() {
        Set<String> commandsAsString = new ArraySet<>();
        for (Command element: getAllCommands()) {
            commandsAsString.add(element.alias());
        }
        return commandsAsString;
    }
    public Command getCommandByName(String name) {
        for (Command command: modules.keySet()) {
            if (command.command().equalsIgnoreCase(name) || command.alias().equalsIgnoreCase(name)) {
                return command;
            }
        }
        return null;
    }
    public List<Command> getCommandByPermission(String permission) {
        List<Command> commands = new ArrayList<>();
        for (Command command: modules.keySet()) {
            if (command.permission().equals(permission)) {
                commands.add(command);
            }
        }
        return commands;
    }
    public String getMethodNameByCommand(Command command) {
        return modules.get(command).getName();
    }
    public ArrayList<String> getAllPrefixe() {
        return prefixe;
    }
}
