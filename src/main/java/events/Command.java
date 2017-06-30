package events;

import main.Prefix;
import sx.blah.discord.handle.obj.Permissions;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    String command();
    String[] arguments();
    String description();
    String alias();
    Permissions permission();
    String prefix();
}
