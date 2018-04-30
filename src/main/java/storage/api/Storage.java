package storage.api;

import de.moddylp.simplecommentconfig.Config;
import main.MoMuOSBMain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import storage.FileDriver;
import storage.LanguageLoader;

public interface Storage {
    Config CONFIG = MoMuOSBMain.config;
    Config LANG = LanguageLoader.lang;

}
