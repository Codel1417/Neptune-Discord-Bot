package Neptune;

import Neptune.Commands.PassiveCommands.DM_ImageDownload;
import Neptune.Commands.PassiveCommands.guildListener;
import Neptune.Storage.GetAuthToken;
import Neptune.Storage.StorageController;
import Neptune.Storage.VariablesStorage;
import com.neovisionaries.ws.client.WebSocketFactory;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import javax.security.auth.login.LoginException;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import static net.dv8tion.jda.core.AccountType.BOT;

public class Main extends ListenerAdapter {
    private final static Logger Log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        StorageController storageController = new StorageController();
        VariablesStorage variablesStorage = new VariablesStorage();
        GetAuthToken getAuthToken = new GetAuthToken();
        variablesStorage.Init(getAuthToken.GetToken(new File("NepAuth.json")));

        final boolean useSharding = false;
        Log.log(Level.CONFIG,"Starting");
        if (variablesStorage.getDevMode()) Log.log(Level.WARNING,"WARNING! DEV MODE ENABLED!!!");

        JDABuilder builder = new JDABuilder(BOT);
        String token = variablesStorage.getDiscordBotToken();
        builder.addEventListener(new MessageListener(variablesStorage, storageController));
        builder.addEventListener(new DM_ImageDownload());
        builder.addEventListener(new guildListener(variablesStorage));
        builder.setGame(Game.playing("!Nep Help"));
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
