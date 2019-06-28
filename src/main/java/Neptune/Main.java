package Neptune;

import Neptune.Storage.StorageControllerCached;
import Neptune.music.PlayerControl;
import com.neovisionaries.ws.client.WebSocketFactory;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.net.ssl.SSLContext;
import javax.security.auth.login.LoginException;

import java.util.logging.Level;
import java.util.logging.Logger;

import static net.dv8tion.jda.core.AccountType.BOT;

public class Main extends ListenerAdapter {
    private final static Logger Log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        StorageControllerCached storageControllerCached = new StorageControllerCached();
        VariablesStorage variablesStorage = new VariablesStorage();
        variablesStorage.Init(storageControllerCached.getBotInfo());

        boolean useSharding = true;
        Log.log(Level.CONFIG,"Starting");
        if (variablesStorage.getDevMode()) Log.log(Level.WARNING,"WARNING! DEV MODE ENABLED!!!");
        JDABuilder builder = new JDABuilder(BOT);
        String token = variablesStorage.getDiscordBotToken();
        builder.addEventListener(new MessageListener(variablesStorage, storageControllerCached));
        builder.addEventListener(new DM_ImageDownload());
        builder.addEventListener(new guildListener(variablesStorage));

        if (variablesStorage.getDevMode()) {
            builder.addEventListener(new PlayerControl()); //music player from Yui bot
        }
        builder.setToken(token);
        builder.setWebsocketFactory(new WebSocketFactory().setVerifyHostname(false));
        try {
            if (useSharding) {
                int shardTotal = 5;
                Log.info("Sharding Enabled");
                for (int i = 0; i < shardTotal; i++)
                {
                    builder.useSharding(i, shardTotal).buildAsync();
                }
            }
            else {
                builder.buildAsync();
            }
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }
}
