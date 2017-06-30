package storage;

import main.MoMuOSBMain;
import util.Console;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

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

    /**
     * Create new File
     * @param filenamewithpath FileName
     */
    public void createNewFile(String filenamewithpath) {
        try {
            String[] parts = filenamewithpath.split("/");
            Console.debug(Arrays.toString(parts));
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
            ex.printStackTrace();
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
                JSONParser jsonParser = new JSONParser();
                json = (JSONObject) jsonParser.parse(string);
            }
        } catch (Exception ex) {
            Console.error("Parsing error");
            ex.printStackTrace();
        }
        return json;
    }

    /**
     * Load Json from File
     */
    private void loadJson() {
        try {
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
            ex.printStackTrace();
            MoMuOSBMain.shutdown();
        }
    }

    /**
     * Save Json to File
     */
    private void saveJson() {
        try {
            for (String filename: files.keySet()) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(files.get(filename)));
                String json = jsons.get(filename).toJSONString();
                json = json.replace("{\"", "{\n     \"")
                        .replace(",\"", ",\n     \"")
                        .replace("\"}", "\"\n}");
                writer.write(json);
                writer.close();
            }
        } catch (Exception ex) {
            Console.error("File can not be saved");
            ex.printStackTrace();
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
            loadJson();
            if (jsons.get(filename) != null) {
                if (jsons.get(filename).containsKey(option)) {
                    removeProperty(filename, option);
                }
                jsons.get(filename).put(option, value);
            } else {
                JSONObject json = new JSONObject();
                json.put(option, value);
                jsons.put(filename, json);
            }
            saveJson();
        } catch (Exception ex) {
            Console.error("Can not set Property: ");
            ex.printStackTrace();
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
        loadJson();
        try {
            if (jsons.get(filename) == null || !jsons.get(filename).containsKey(option)) {
                setProperty(filename, option, defaultvalue);
            }
        } catch (Exception ex) {
            setProperty(filename, option, defaultvalue);
            ex.printStackTrace();
        }
        return jsons.get(filename).get(option);
    }

    /**
     * Get Property of File but without a Default Value
     * @param filename filename
     * @param option option
     * @return value
     */
    public Object getPropertyOnly(String filename, String option) {
        loadJson();
        if (jsons.get(filename).containsKey(option)) {
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
            loadJson();
            if (jsons.get(filename).containsKey(option)) {
                jsons.get(filename).remove(option);
            }
            saveJson();
        } catch (Exception ex) {
            Console.error("Can not remove Property: ");
            ex.printStackTrace();
        }
    }



}
