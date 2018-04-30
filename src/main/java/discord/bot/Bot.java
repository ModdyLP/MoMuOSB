package discord.bot;

import discord.command.CommandHandler;
import main.MoMuOSBMain;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.requests.SessionReconnectQueue;
import storage.api.Storage;

public class Bot implements Storage{

    private static Bot botinstance;

    public static Bot getBotInst() {
        if (botinstance == null) {
            botinstance = new Bot();
        }
        return botinstance;
    }

    private JDA bot;



    public void startBot() {
        try {
            MoMuOSBMain.logger.info("Starting Bot Instance....");
            MoMuOSBMain.logger.info("===========================================================================");
            if (!CONFIG.get("auth.token").toString().isEmpty() && !CONFIG.get("bot.owner").toString().isEmpty()) {
                        JDABuilder shardBuilder = new JDABuilder(AccountType.BOT)
                                .setToken(CONFIG.get("auth.token").toString())
                                .setReconnectQueue(new SessionReconnectQueue());
                        //register your listeners here using shardBuilder.addEventListener(...)
                        shardBuilder.addEventListener(new BotMainEvents());
                        shardBuilder.addEventListener(CommandHandler.registerCommands());
                        for (int i = 0; i < ((double) CONFIG.get("bot.Shards", 3)); i++)
                        {
                            //using buildBlocking(JDA.Status.AWAITING_LOGIN_CONFIRMATION)
                            // makes sure we start to delay the next shard once the current one actually
                            // sent the login information, otherwise we might hit nasty race conditions
                            bot = shardBuilder.useSharding(i, (Integer.valueOf(CONFIG.get("bot.Shards", 3).toString())))
                                    .buildAsync();
                            Thread.sleep(5000); //sleep 5 seconds between each login
                        }
            } else {
                MoMuOSBMain.logger.error("There is no Bot Token or Owner ID provided in the "+ CONFIG.getAbsolutePath());
            }
            MoMuOSBMain.logger.info("===========================================================================");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public JDA getBot() {
        return bot;
    }
}
