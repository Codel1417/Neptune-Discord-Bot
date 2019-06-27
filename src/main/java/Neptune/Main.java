package Neptune;

import Neptune.Storage.StorageControllerCached;
import Neptune.music.PlayerControl;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

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
        boolean useSharding = false;
        Log.log(Level.CONFIG,"Starting");
        if (variablesStorage.getDevMode()) Log.log(Level.WARNING,"WARNING! DEV MODE ENABLED!!!");
        JDABuilder builder = new JDABuilder(BOT);
        String token = variablesStorage.getDiscordBotToken();
        builder.addEventListener(new MessageListener(variablesStorage, storageControllerCached));
        builder.addEventListener(new DM_ImageDownload());
        builder.addEventListener(new guildListener());

        if (variablesStorage.getDevMode()) {
            builder.addEventListener(new PlayerControl()); //music player from Yui bot
        }
        builder.setToken(token);

        try {
            if (useSharding) {
                for (int i = 0; i < 2; i++)
                {
                    builder.useSharding(i, 2).buildAsync();
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
