package Storage;

import java.io.*;
import java.util.Properties;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
public class ConfigDriver {

    private static ConfigDriver instance;
    private static File file;

    public static ConfigDriver getInstance() {
        if (instance == null) {
            instance = new ConfigDriver();
        }
        file = new File("config.properties");
        return instance;
    }
    public boolean checkFileExists() {
        return file.exists();
    }
    public Properties loadProperties() {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            if (!checkFileExists()) {
                file.createNewFile();
            }
            input = new FileInputStream(file);

            // load a properties file
            prop.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return prop;
    }
    public void saveProperties(Properties prop) {
        OutputStream output = null;

        try {

            output = new FileOutputStream(file);
            prop.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
    public void setProperty(String option, String value) {
        Properties prop = loadProperties();
        prop.setProperty(option, value);
        saveProperties(prop);
    }
    public String getProperty(String option, String defaultvalue) {
        Properties prop = loadProperties();
        if (prop.getProperty(option) == null) {
            setProperty(option, defaultvalue);
            return defaultvalue;
        } else {
            return prop.getProperty(option);
        }
    }


}
