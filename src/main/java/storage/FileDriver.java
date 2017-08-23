package storage;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import main.MoMuOSBMain;
import org.json.JSONObject;
import util.Console;
import util.Utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
public class FileDriver {

    //FILEDRIVE IS NOT TRANSLATED - CAUSES ERRORS BECAUSE LANGUAGE IS NOT LOADED
    public String CONFIG = "config.json";

    private static FileDriver instance;
    private static HashMap<String, File> files = new HashMap<>();
    private static HashMap<String, JSONObject> jsons = new HashMap<>();

    /**
     * Get Instance
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
        return jsons.get(filename).keySet().size() == 0;
    }

    /**
     * Create new File
     * @param filenamewithpath FileName
     */
    public void createNewFile(String filenamewithpath) {
        try {
            String[] parts = filenamewithpath.split("/");
            for (int i = 0; i < parts.length-1; i++) {
                File file = new File(parts[i]);
                if(!file.exists()) {
                    if (!file.mkdir()) {
                        Console.error("Cant create Folder: "+file.getAbsolutePath());
                        MoMuOSBMain.shutdown();
                    }
                }
            }
            File file = new File(filenamewithpath);
            files.put(filenamewithpath, file);
            if (!file.exists()) {
                if (file.createNewFile()) {
                    Console.println(filenamewithpath + " created at " + file.getAbsolutePath());
                }
            } else {
                Console.println(filenamewithpath + " loaded at " + file.getAbsolutePath());
            }
            loadJson();
        } catch (Exception ex) {
            Console.error("File can not be accessed: "+filenamewithpath);
            Console.error(ex);
            MoMuOSBMain.shutdown();
        }
    }

    /**
     * Parse the String to a JSONObject
     * @param string Content
     * @return Jsonobject
     */
    private JSONObject parseJson(String string) {
        JSONObject json = new JSONObject();
        try {
            if (!string.equals("")) {
                JsonParser jsonParser = new JsonParser();
                json = new JSONObject(jsonParser.parse(string).getAsJsonObject().toString());
            }
        } catch (Exception ex) {
            Console.error("Parsing error");
            Console.error(ex);
        }
        return json;
    }

    /**
     * Load Json from File
     */
    public void loadJson() {
        try {
            //Console.println("===LOADFILE===");
            for (String filename: files.keySet()) {
                BufferedReader reader = new BufferedReader(new FileReader(files.get(filename)));
                StringBuilder content = new StringBuilder();
                String line;
                while((line = reader.readLine()) != null) {
                    content.append(line);
                }
                jsons.put(filename, parseJson(content.toString()));

            }

        } catch (Exception ex) {
            Console.error("File can not be loaded");
            Console.error(ex);
            MoMuOSBMain.shutdown();
        }
    }

    /**
     * Save Json to File
     */
    public void saveJson() {
        try {
            //Console.println("===SAVEFILE===");
            for (String filename: files.keySet()) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(files.get(filename)));
                String json = jsons.get(filename).toString();
                json = Utils.crunchifyPrettyJSONUtility(json);
                writer.write(json);
                writer.close();
            }
        } catch (Exception ex) {
            Console.error("File can not be saved");
            Console.error(ex);
            MoMuOSBMain.shutdown();
        }
    }

    /**
     * Set A property in a specific file
     * @param filename filename
     * @param option option
     * @param value value
     */
    public void setProperty(String filename, String option, Object value) {
        try {
            if (jsons.get(filename) != null) {
                if (jsons.get(filename).has(option)) {
                    removeProperty(filename, option);
                }
                jsons.get(filename).put(option, value);
            } else {
                JSONObject json = new JSONObject();
                json.put(option, value);
                jsons.put(filename, json);
            }
        } catch (Exception ex) {
            Console.error("Can not set Property: ");
            Console.error(ex);
        }
    }

    /**
     * Get Property of File with defaultvalue
     * @param filename filename
     * @param option option
     * @param defaultvalue defaultvalue
     * @return value
     */
    public Object getProperty(String filename, String option, Object defaultvalue) {
        try {
            if (jsons.get(filename) == null || !jsons.get(filename).has(option)) {
                setProperty(filename, option, defaultvalue);
            }
        } catch (Exception ex) {
            setProperty(filename, option, defaultvalue);
            Console.error(ex);
        }
        return jsons.get(filename).get(option);
    }

    public boolean hasKey(String filename, String option) {
        try {
            return jsons.get(filename).has(option);
        } catch (Exception ex) {
            Console.error(ex);
        }
        return false;
    }

    /**
     * Get Property of File but without a Default Value
     * @param filename filename
     * @param option option
     * @return value
     */
    public Object getPropertyOnly(String filename, String option) {
        if (jsons.get(filename).has(option)) {
            return jsons.get(filename).get(option);
        } else {
            return "No Value";
        }

    }

    /**
     * Removes A property in a specific file
     * @param filename filename
     * @param option option
     */
    public void removeProperty(String filename, String option) {
        try {
            if (jsons.get(filename).has(option)) {
                jsons.get(filename).remove(option);
            }
        } catch (Exception ex) {
            Console.error("Can not remove Property: ");
            Console.error(ex);
        }
    }

    public HashMap<String, Object> getAllKeysWithValues(String filename) {
        HashMap<String, Object> objects = new HashMap<>();
        try {
            for (Object key: jsons.get(filename).keySet()) {
                objects.put(key.toString(), jsons.get(filename).get(key.toString()));
            }
        } catch (Exception ex) {
            Console.error("Can not list Property: ");
            Console.error(ex);
        }
        return objects;
    }



}
