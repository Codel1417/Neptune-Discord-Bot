package neptune;

import neptune.commands.PassiveCommands.Listener;
import neptune.prometheus.promListener;
import neptune.scheduler.SchedulerListener;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import io.sentry.Sentry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.security.auth.login.LoginException;

public class Main extends ListenerAdapter {
    protected static final Logger log = LogManager.getLogger();

    public static void main(String[] args) {
        try {
            Sentry.init(options -> {
                options.setEnableExternalConfiguration(true);
                options.setEnableUncaughtExceptionHandler(true);
                options.setTracesSampleRate(1.0);
                options.setDebug(true);
                options.setHostnameVerifier((arg0, arg1) -> true);
            });
        }
        catch(Exception e) {
            log.error(e);
        }

        //currently this only lets me know when the bot starts/updates
        WebhookClientBuilder builder = new WebhookClientBuilder("https://discord.com/api/webhooks/824934905137987604/ENvcx0LdZrAqIafi306vVfFG9T8rE5djOH07ouKZcOZ1zbiXS3mj78S2734KihP2SGCA");
        builder.setThreadFactory((job) -> {
            Thread thread = new Thread(job);
            thread.setName("Neptune Status Webhook");
            thread.setDaemon(true);
            return thread;
        });
        builder.setWait(true);
        WebhookClient client = builder.build();

        startJDA(System.getenv("NEPTUNE_TOKEN"));
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
                    .addEventListeners(new Listener(), new promListener(), new SchedulerListener())
                    .setActivity(Activity.listening("Nep Nep Nep Nep Nep"))
                    .setMemberCachePolicy(MemberCachePolicy.ALL)    
                    .disableCache(CacheFlag.ACTIVITY, CacheFlag.EMOTE, CacheFlag.CLIENT_STATUS)
                    .build();
        } catch (LoginException e) {
            Sentry.captureException(e);
            log.error(e);
            System.exit(1);
        }
    }
}
