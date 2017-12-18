package storage.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import storage.FileDriver;

public interface Storage {
    FileDriver DRIVER = FileDriver.getInstance();

}
