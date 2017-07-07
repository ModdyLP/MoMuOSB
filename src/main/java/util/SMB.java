package util;

import sx.blah.discord.util.EmbedBuilder;

import java.awt.*;

/**
 * Created by ModdyLP on 29.06.2017. Website: https://moddylp.de/
 */
public class SMB {
    public static EmbedBuilder shortMessage(String message) {
        String[] messageparts = message.split("\n");
        EmbedBuilder builder = new EmbedBuilder();
        if (messageparts.length > 1) {
            builder.withTitle(messageparts[0]);
            for (int i = 1; i < messageparts.length; i++) {
                builder.appendDesc(messageparts[i]+"\n");
            }
        } else {
            builder.withTitle(message);
        }
        builder.withColor(Color.green);
        return builder;
    }
}
