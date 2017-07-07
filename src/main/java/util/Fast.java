package util;

import discord.DiscordInit;
import discord.ServerControl;
import events.CommandManager;
import events.EventListener;
import permission.PermissionController;
import storage.FileDriver;
import storage.LanguageLoader;

/**
 * Created by N.Hartmann on 29.06.2017.
 * Copyright 2017
 */
public interface Fast {
    FileDriver DRIVER = FileDriver.getInstance();
    DiscordInit INIT = DiscordInit.getInstance();
    EventListener EVENT = EventListener.getInstance();
    LanguageLoader LANG = LanguageLoader.getInstance();
    PermissionController PERM = PermissionController.getInstance();
    CommandManager COMMAND = CommandManager.getInstance();
    ServerControl SERVER_CONTROL = ServerControl.getInstance();
}
