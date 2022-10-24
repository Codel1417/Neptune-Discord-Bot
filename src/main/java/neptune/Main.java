package neptune;

import neptune.commands.ListenerHandler;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.security.auth.login.LoginException;

public class Main {
    protected static final Logger log = LogManager.getLogger();

    public static void main(String[] args) {
        log.trace("Start");
        log.trace("Begin Init JDA");
        startJDA(System.getenv("NEPTUNE_TOKEN"));
        log.trace("End Init JDA");
    }

    private static void startJDA(String token) {
        log.info("Starting JDA");
        try {
            DefaultShardManagerBuilder.create(token, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.DIRECT_MESSAGES).addEventListeners(new ListenerHandler()).setActivity(Activity.listening("Nep Nep Nep Nep Nep")).setMemberCachePolicy(MemberCachePolicy.NONE).disableCache(CacheFlag.ACTIVITY, CacheFlag.EMOTE, CacheFlag.CLIENT_STATUS, CacheFlag.ONLINE_STATUS, CacheFlag.MEMBER_OVERRIDES, CacheFlag.ROLE_TAGS).build();
            log.info("Started JDA");
        } catch (LoginException e) {
            log.error(e);
            System.exit(2);
        }
    }
}
