package util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;

/**
 * Created by N.Hartmann on 29.06.2017.
 * Copyright 2017
 */
public class Utils {
    /**
     * Calculate the TimeDiff and format it
     * @param firstdate The earliest date
     * @param seconddate the latest date
     * @return a formated string
     */
    public static String calculateAndFormatTimeDiff(Date firstdate, Date seconddate){
        long diffInSeconds = (seconddate.getTime() - firstdate.getTime()) / 1000;

        long diff[] = new long[] { 0, 0, 0, 0 };
    /* sec */diff[3] = (diffInSeconds >= 60 ? diffInSeconds % 60 : diffInSeconds);
    /* min */diff[2] = (diffInSeconds = (diffInSeconds / 60)) >= 60 ? diffInSeconds % 60 : diffInSeconds;
    /* hours */diff[1] = (diffInSeconds = (diffInSeconds / 60)) >= 24 ? diffInSeconds % 24 : diffInSeconds;
    /* days */diff[0] = (diffInSeconds = (diffInSeconds / 24));
        return String.format(
                "%d day%s, %d hour%s, %d minute%s, %d second%s",
                diff[0],
                diff[0] > 1 ? "s" : "",
                diff[1],
                diff[1] > 1 ? "s" : "",
                diff[2],
                diff[2] > 1 ? "s" : "",
                diff[3],
                diff[3] > 1 ? "s" : "");
    }
    public static String format(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-d:HH-mm:ss");
        return sdf.format(date);
    }
    public static String makeArgsToString(String[] args, String[] replace) {
        String string = Arrays.toString(args).replace("[", "").replace("]", "").replace(",", "");
        for (String arg: replace) {
            string = string.replace(arg, "");
        }
        return string.trim();
    }
    public static String crunchifyPrettyJSONUtility(String simpleJSON) {
        JsonParser crunhifyParser = new JsonParser();
        JsonObject json = crunhifyParser.parse(simpleJSON).getAsJsonObject();

        Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();

        return prettyGson.toJson(json);
    }
    public static JSONArray objectToJSONArray(Object object){
        Object json = null;
        JSONArray jsonArray = null;
        try {
            json = new JSONTokener(object.toString()).nextValue();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (json instanceof JSONArray) {
            jsonArray = (JSONArray) json;
        }
        return jsonArray;
    }
    public static JSONObject objectToJSONObject(Object object){
        Object json = null;
        JSONObject jsonObject = null;
        try {
            json = new JSONTokener(object.toString()).nextValue();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (json instanceof JSONObject) {
            jsonObject = (JSONObject) json;
        }
        return jsonObject;
    }
}
