package util;

/**
 * Created by N.Hartmann on 30.06.2017.
 * Copyright 2017
 */
public interface Globals {
    //Prefix Globals
    String ADMIN_PREFIX = "!";
    String INFO_PREFIX = ".";
    String GAME_PREFIX = "~";
    String MUSIC_PREFIX = "$";

    String[] allprefixes = new String[] {ADMIN_PREFIX, INFO_PREFIX, GAME_PREFIX, MUSIC_PREFIX};

    //Permission globals
    String BOT_OWNER = "bot_owner";
    String BOT_MANAGE = "bot_manage";
    String BOT_INFO = "bot_info";
    String BOT_PERM = "bot_permissions";
}
