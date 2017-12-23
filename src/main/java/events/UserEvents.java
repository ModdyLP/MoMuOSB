package events;

import discord.BotUtils;
import discord.Stats;
import modules.RoleManagement;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import util.Console;
import util.Fast;
import util.SMB;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by N.Hartmann on 06.07.2017.
 * Copyright 2017
 */
public class UserEvents implements Fast{
    private static UserEvents instance;

    private static HashMap<IUser, IGuild> user = new HashMap<>();

    public static UserEvents getInstance() {
        if (instance == null) {
            instance = new UserEvents();
        }
        return instance;
    }

    @EventSubscriber
    public void onUserJoin(UserJoinEvent event) {
        Console.debug(Console.recievedprefix+"User joined: [S]"+event.getGuild().getName()+" [U]"+event.getUser().getName());
        Stats.addUser();
        if (SERVER_CONTROL.getEnabledList(SERVER_CONTROL.JOIN_MODULE).contains(event.getGuild().getStringID()) && DRIVER.getPropertyOnly(DRIVER.CONFIG, "genderroles").equals(true)) {
            if (RoleManagement.isGenderdefined(event.getGuild())) {
                user.put(event.getUser(), event.getGuild());
                Console.debug(Console.recievedprefix+"Waiting for Answer"+ Arrays.toString(user.keySet().toArray())+"  "+ Arrays.toString(user.values().toArray()));
                BotUtils.sendPrivEmbMessage(event.getUser().getOrCreatePMChannel(), SMB.shortMessage(LANG.getTranslation("female_ask").replaceAll("%1s", INIT.BOT.getOurUser().getName())), false);
            } else {
                Console.debug("Server has not valid Gender.");
            }
        }
    }

    public void setGenderRole(MessageReceivedEvent event, String gender) {
        try {
            List<IRole> roles = RoleManagement.getRoleforGender(user.get(event.getAuthor()), gender);
            StringBuilder rolename = new StringBuilder();
            if (roles != null) {
                for (IRole role : roles) {
                    rolename.append(role.getName()).append(",");
                    event.getAuthor().addRole(role);
                }
                if (roles.size() > 0) {
                    user.remove(event.getAuthor());
                }
                BotUtils.sendPrivEmbMessage(event.getAuthor().getOrCreatePMChannel(), SMB.shortMessage(String.format(LANG.getTranslation("gender_role_added"), rolename.toString())), true);
            } else {
                BotUtils.sendPrivEmbMessage(event.getAuthor().getOrCreatePMChannel(), SMB.shortMessage(LANG.getTranslation("role_notfound")), true);
            }
        } catch (Exception ex) {
            BotUtils.sendPrivEmbMessage(event.getGuild().getOwner().getOrCreatePMChannel(), SMB.shortMessage(LANG.getTranslation("role_permissions")), true);
        }
    }
}
