package neptune;

import neptune.commands.PassiveCommands.Listener;
import neptune.prometheus.promListener;
import neptune.storage.commandLineOptionsSingleton;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManager;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import javax.security.auth.login.LoginException;

public class Main extends ListenerAdapter {
    static Options Options = new Options();
    protected static final Logger log = LogManager.getLogger();

    public static void main(String[] args) {
        WebhookClientBuilder builder = new WebhookClientBuilder("https://discord.com/api/webhooks/824934905137987604/ENvcx0LdZrAqIafi306vVfFG9T8rE5djOH07ouKZcOZ1zbiXS3mj78S2734KihP2SGCA");
        builder.setThreadFactory((job) -> {
            Thread thread = new Thread(job);
            thread.setName("Neptune Status");
            thread.setDaemon(true);
            return thread;
        });
        builder.setWait(true);
        WebhookClient client = builder.build();
        // CLI
        Options.addRequiredOption("d", "discord-token", true, "The discord bot token");
        Options.addRequiredOption("t", "tenor", true, "Tenor api key");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(Options, args);
        } catch (ParseException e) {
            log.error(e);
        }
        commandLineOptionsSingleton.getInstance().setOptions(cmd);

        startJDA(cmd.getOptionValue("d"));
        client.send("Starting Neptune");
    }

    private static void startJDA(String token) {
        log.info("Starting JDA");
        try {
            DefaultShardManagerBuilder.create(
                            token,
                            GatewayIntent.GUILD_MESSAGES,
                            GatewayIntent.GUILD_VOICE_STATES,
                            GatewayIntent.DIRECT_MESSAGES,
                            GatewayIntent.GUILD_MEMBERS)
                    .addEventListeners(new Listener(), new promListener())
                    .setActivity(Activity.listening("Nep Nep Nep Nep Nep"))
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .disableCache(CacheFlag.ACTIVITY, CacheFlag.EMOTE, CacheFlag.CLIENT_STATUS)
                    .build();
        } catch (LoginException e) {
            log.error(e);
            System.exit(1);
        }
    }
}
