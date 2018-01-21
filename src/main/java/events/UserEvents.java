package events;

import discord.BotUtils;
import discord.Stats;
import modules.RoleManagement;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
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

    private HashMap<IUser, IGuild> users = new HashMap<>();

    public static UserEvents getInstance() {
        if (instance == null) {
            instance = new UserEvents();
        }
        return instance;
    }

    @EventSubscriber
    public void onUserJoin(UserJoinEvent event) {
        sendAwaittoUser(event.getGuild(), event.getUser());
    }

    public void sendAwaittoUser(IGuild guild, IUser user) {
        Console.debug(Console.recievedprefix+"User waiting for Gender: [S]"+guild.getName()+" [U]"+user.getName());
        Stats.addUser();
        if (SERVER_CONTROL.getEnabledList(SERVER_CONTROL.JOIN_MODULE).contains(guild.getStringID())) {
            if (RoleManagement.isGenderdefined(guild)) {
                users.put(user, guild);
                Console.debug(Console.recievedprefix+"Waiting for Answer"+ Arrays.toString(users.keySet().toArray())+"  "+ Arrays.toString(users.values().toArray()));
                IMessage message = BotUtils.sendPrivEmbMessage(user.getOrCreatePMChannel(), SMB.shortMessage(String.format(LANG.getTranslation("female_ask"),guild.getName())), false);
                BotUtils.addReactionToMessage(message, "mens");
                BotUtils.addReactionToMessage(message, "womens");
            } else {
                Console.debug("Server has not valid Gender.");
            }
        } else {
            Console.debug("Gender Module is disabled.");
        }
    }

    public void setGenderRole(IUser user, String gender) {
        try {
            List<IRole> roles = RoleManagement.getRoleforGender(users.get(user), gender);
            StringBuilder rolename = new StringBuilder();
            if (roles != null) {
                for (IRole role : roles) {
                    rolename.append(role.getName()).append(",");
                    user.addRole(role);
                }
                if (roles.size() > 0) {
                    users.remove(user);
                }
                BotUtils.sendPrivEmbMessage(user.getOrCreatePMChannel(), SMB.shortMessage(String.format(LANG.getTranslation("gender_role_added"), rolename.toString())), true);
            } else {
                BotUtils.sendPrivEmbMessage(user.getOrCreatePMChannel(), SMB.shortMessage(LANG.getTranslation("role_notfound")), true);
            }
        } catch (Exception ex) {
            BotUtils.sendPrivEmbMessage(user.getOrCreatePMChannel(), SMB.shortMessage(LANG.getTranslation("role_permissions")), true);
        }
    }
}
