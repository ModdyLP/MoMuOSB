package events;

import discord.BotUtils;
import discord.ServerControl;
import modules.RoleManagement;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent;
import sx.blah.discord.handle.obj.IRole;
import util.Console;
import util.Fast;
import util.SMB;

import java.util.List;

/**
 * Created by N.Hartmann on 06.07.2017.
 * Copyright 2017
 */
public class UserEvents implements Fast{
    private static UserEvents instance;

    public static UserEvents getInstance() {
        if (instance == null) {
            instance = new UserEvents();
        }
        return instance;
    }

    @EventSubscriber
    public void onUserJoin(UserJoinEvent event) {
        if (!SERVER_CONTROL.getDisabledlist(SERVER_CONTROL.JOIN_MODULE).contains(event.getGuild().getStringID()) && DRIVER.getPropertyOnly(DRIVER.CONFIG, "genderroles").equals(true)) {
            if (RoleManagement.isGenderdefined(event.getGuild())) {
                BotUtils.sendPrivEmbMessage(event.getUser().getOrCreatePMChannel(), SMB.shortMessage(LANG.getTranslation("female_ask")));
            } else {
                Console.debug("Server has not valid Gender.");
            }
        }
    }

    public void setGenderRole(MessageReceivedEvent event, String gender) {
        List<IRole> roles = RoleManagement.getRoleforGender(event.getGuild(), gender);
        for (IRole role: roles) {
            event.getAuthor().addRole(role);
        }
        BotUtils.sendPrivEmbMessage(event.getAuthor().getOrCreatePMChannel(), SMB.shortMessage("gender_role_added"));
    }
}
