package storage;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by N.Hartmann on 07.07.2017.
 * Copyright 2017
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface LanguageMethod {
    int languagestringcount();
}
