package events;

import storage.LanguageInterface;
import util.Console;
import util.Fast;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
public class Module implements Fast, LanguageInterface {
    @Override
    public void setdefaultLanguage() {
        Console.println("No Language for "+this.getClass().getName());
    }
}
