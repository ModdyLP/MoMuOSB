package main;

import discord.DiscordInit;
import events.EventListener;
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
}