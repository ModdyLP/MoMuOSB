package storage;

import main.MoMuOSBMain;
import org.hjson.JsonObject;
import org.hjson.JsonType;
import org.hjson.JsonValue;
import org.hjson.Stringify;

import java.io.*;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
public class FileDriver {

    //FILEDRIVE IS NOT TRANSLATED - CAUSES ERRORS BECAUSE LANGUAGE IS NOT LOADED
    public String CONFIG = "config.json";
    public String MODULE = "modules.json";

    private static FileDriver instance;
    private static HashMap<String, File> files = new HashMap<>();
    private static HashMap<String, JsonObject> jsons = new HashMap<>();

    /**
     * Get Instance
     *
     * @return Class Instance
     */
    public static FileDriver getInstance() {
        if (instance == null) {
            instance = new FileDriver();
        }
        return instance;
    }

    public boolean checkIfFileExists(String filename) {
        return files.get(filename) != null && files.get(filename).exists();
    }

    public boolean checkIfFileisEmpty(String filename) {
        return jsons.get(filename).names().size() == 0;
    }

    /**
     * Create new File
     *
     * @param filenamewithpath FileName
     */
    public void createNewFile(String filenamewithpath) {

        try {
            String[] parts = filenamewithpath.split("/");
            for (int i = 0; i < parts.length - 1; i++) {
                File file = new File(parts[i]);
                if (!file.exists()) {
                    if (!file.mkdir()) {
                        MoMuOSBMain.logger.error("Cant create Folder: " + file.getAbsolutePath());
                        MoMuOSBMain.shutdown();
                    }
                }
            }
            File file = new File(filenamewithpath);
            files.put(filenamewithpath, file);
            if (!file.exists()) {
                if (file.createNewFile()) {
                    MoMuOSBMain.logger.info(filenamewithpath + " created at " + file.getAbsolutePath());
                }
            } else {
                MoMuOSBMain.logger.info(filenamewithpath + " loaded at " + file.getAbsolutePath());
            }
            loadJson();
        } catch (Exception ex) {
            MoMuOSBMain.logger.error("File can not be accessed: " + filenamewithpath);
            MoMuOSBMain.logger.error(ex);
            MoMuOSBMain.shutdown();
        }
    }


    /**
     * Load Json from File
     */
    private void loadJson() {
        try {
            MoMuOSBMain.logger.info("===LOADFILES===");
            for (String filename : files.keySet()) {
                BufferedReader reader = new BufferedReader(new FileReader(files.get(filename)));
                jsons.put(filename, JsonValue.readHjson(reader).asObject());
            }

        } catch (Exception ex) {
            MoMuOSBMain.logger.error("File can not be loaded");
            MoMuOSBMain.logger.error(ex);
            MoMuOSBMain.shutdown();
        }
    }

    /**
     * Save Json to File
     */
    public void saveJson() {
        try {
            MoMuOSBMain.logger.info("===SAVEFILES===");
            for (String filename : files.keySet()) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(files.get(filename)));
                jsons.get(filename).writeTo(writer, Stringify.HJSON);
                writer.close();
            }
        } catch (Exception ex) {
            MoMuOSBMain.logger.error("File can not be saved");
            MoMuOSBMain.logger.error(ex);
            MoMuOSBMain.shutdown();
        }
    }
    /**
     * Set A property in a specific file
     *
     * @param filename filename
     * @param option   option
     * @param value    value
     */
    public void setProperty(String filename, String option, Object value) {
        try {
            if (jsons.get(filename) != null) {
                if (hasKey(filename, option)) {
                    removeProperty(filename, option);
                }
                setJsonObject(jsons.get(filename), option, value);
            } else {
                JsonObject json = new JsonObject();
                setJsonObject(json, option, value);
                jsons.put(filename, json);
            }
        } catch (Exception ex) {
            MoMuOSBMain.logger.error("Can not set Property: ");
            MoMuOSBMain.logger.error(ex);
        }
    }

    private void setJsonObject(JsonObject json, String option, Object value) {
        try {
            if (value instanceof Integer) {
                json.set(option, (Integer) value);
                return;
            }
            if (value instanceof String) {
                json.set(option, (String) value);
                return;
            }
            if (value instanceof Boolean) {
                json.set(option, (Boolean) value);
                return;
            }
            if (value instanceof Long) {
                json.set(option, (Long) value);
                return;
            }
            if (value instanceof Float) {
                json.set(option, (Float) value);
                return;
            }
            MoMuOSBMain.logger.error("No Valid Value found: " + option + "   " + value);
        } catch (Exception ex) {
            MoMuOSBMain.logger.error(ex);
        }
    }
    private Object getJsonObject(JsonValue value) {
        try {

            if (value.getType().equals(JsonType.NUMBER)) {
                if (value.toString().contains(".")) {
                    return value.asDouble();
                } else {
                    return value.asLong();
                }
            }
            if (value.getType().equals(JsonType.STRING)) {
                return value.asString();
            }
            if (value.getType().equals(JsonType.BOOLEAN)) {
                return value.asBoolean();
            }
            if (value.getType().equals(JsonType.ARRAY)) {
                return value.asArray();
            }
            if (value.getType().equals(JsonType.OBJECT)) {
                return value.asObject();
            }
            MoMuOSBMain.logger.error("No Valid Value found: "+value.asString());
        } catch (Exception ex) {
            MoMuOSBMain.logger.error(ex);
        }
        return null;
    }


    /**
     * Get Property of File with defaultvalue
     *
     * @param filename     filename
     * @param option       option
     * @param defaultvalue defaultvalue
     * @return value
     */
    public Object getProperty(String filename, String option, Object defaultvalue) {
        try {
            if (jsons.get(filename) == null || !hasKey(filename, option)) {
                setProperty(filename, option, defaultvalue);
                saveJson();
            }
        } catch (Exception ex) {
            setProperty(filename, option, defaultvalue);
            MoMuOSBMain.logger.error(ex);
            saveJson();
        }
        return getJsonObject(jsons.get(filename).get(option));
    }

    private boolean hasKey(String filename, String option) {
        try {
            return jsons.get(filename).get(option) != null;
        } catch (Exception ex) {
            MoMuOSBMain.logger.error(ex);
        }
        return false;
    }

    /**
     * Get Property of File but without a Default Value
     *
     * @param filename filename
     * @param option   option
     * @return value
     */
    public Object getPropertyOnly(String filename, String option) {
        if (hasKey(filename, option) && !jsons.get(filename).get(option).toString().equals("")) {
            return getJsonObject(jsons.get(filename).get(option));
        } else {
            return null;
        }

    }

    /**
     * Removes A property in a specific file
     *
     * @param filename filename
     * @param option   option
     */
    private void removeProperty(String filename, String option) {
        try {
            if (hasKey(filename, option)) {
                jsons.get(filename).remove(option);
            }
        } catch (Exception ex) {
            MoMuOSBMain.logger.error("Can not remove Property: ");
            MoMuOSBMain.logger.error(ex);
        }
    }

    public HashMap<String, Object> getAllKeysWithValues(String filename) {
        HashMap<String, Object> objects = new HashMap<>();
        try {
            for (Object key : jsons.get(filename).names()) {
                objects.put(key.toString(), jsons.get(filename).get(key.toString()));
            }
        } catch (Exception ex) {
            MoMuOSBMain.logger.error("Can not list Property: ");
            MoMuOSBMain.logger.error(ex);
        }
        return objects;
    }


}
