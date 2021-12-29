package neptune;

import neptune.commands.ListenerHandler;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.sentry.Sentry;

import javax.security.auth.login.LoginException;

public class Main {
    protected static final Logger log = LogManager.getLogger();

    public static void main(String[] args) {
        log.trace("Start");
        /*
        log.trace("Begin Init Sentry");
        Sentry.init(options -> {
            options.setEnableExternalConfiguration(true);
            options.setEnableUncaughtExceptionHandler(true);
            options.setTracesSampleRate(1.0);
            options.setHostnameVerifier((arg0, arg1) -> true);
            options.setAttachThreads(true);
            options.setAttachServerName(true);
            options.setAttachStacktrace(true);
            //options.setRelease(System.getenv("NEPTUNE_COMMIT_ID"));
            options.setEnableAutoSessionTracking(true);
            //options.setEnvironment("development");
        });
        log.trace("Finish Init Sentry");
        */
        log.trace("Begin Init JDA");
        startJDA(System.getenv("NEPTUNE_TOKEN"));
        log.trace("End Init JDA");
        //client.send("Starting Neptune on commit id " + System.getenv("NEPTUNE_COMMIT_ID"));
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
                    .addEventListeners(new ListenerHandler())
                    .setActivity(Activity.listening("Nep Nep Nep Nep Nep"))
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .disableCache(CacheFlag.ACTIVITY, CacheFlag.EMOTE, CacheFlag.CLIENT_STATUS)
                    .build();
            log.info("Started JDA");
        } catch (LoginException e) {
            Sentry.captureException(e);
            log.error(e);
            System.exit(2);
        }
    }
}
