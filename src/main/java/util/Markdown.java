package util;

/**
 * Created by N.Hartmann on 05.07.2017.
 * Copyright 2017
 */
public class Markdown {

    //Generell Markdown
    public static String bold(String text){
        return "**"+text+"**";
    }
    public static String italic(String text){
        return "*"+text+"*";
    }
    public static String underline(String text){
        return "__"+text+"__";
    }
    public static String striketrought(String text){
        return "~~"+text+"~~";
    }
    public static String codeOL(String text){
        return "`"+text+"`";
    }
    public static String codeML(String text){
        return "```\n"+text+"\n```";
    }


    //Utils
    public static String bolditalic(String text){
        return Markdown.italic(Markdown.bold(text));
    }
    public static String underlineitalic(String text){
        return Markdown.underline(Markdown.italic(text));
    }
    public static String underlinebold(String text){
        return Markdown.underline(Markdown.bold(text));
    }
    public static String underlinebolditalic(String text){
        return Markdown.underline(Markdown.bolditalic(text));
    }
}
