package neptune;

import com.neovisionaries.ws.client.WebSocketFactory;
import neptune.commands.PassiveCommands.Listener;
import neptune.commands.PassiveCommands.guildListener;
import neptune.music.PlayerControl;
import neptune.storage.MySQL.GetAuthToken;
import neptune.storage.VariablesStorage;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.security.auth.login.LoginException;

public class Main extends ListenerAdapter {
    protected static final Logger log = LogManager.getLogger();
    private static GetAuthToken getAuthToken = new GetAuthToken();
    /* Mode
    0: Main
    1: Dev
    2: Music Bot
     */
    public final static int mode = 1;
    private static String botToken;
    public static final String DatabaseURL = "jdbc:mysql://10.0.0.52/Neptune?user=Neptune&password=Neptune";
    public static void main(String[] args) {
        log.traceEntry();
        botToken = getJdaAuthKey(mode);
        VariablesStorage variablesStorage = new VariablesStorage();
        if (mode == 2){
            startJDAMusic(botToken);
        }
        else {
            //if (mode == 0) startDropboxBackup((String) authKeys.get("dropbox"));
            startJDA(botToken,variablesStorage);
        }
    }
    private static String getJdaAuthKey(int mode){
        switch (mode){
            case 0: { //main
                return getAuthToken.GetToken("discord-token");
            }
            case 1: { //debug
                return getAuthToken.GetToken("dev-discord-token");
            }
            case 2: {
                return getAuthToken.GetToken("music-discord-token");
            }
        }
        return null;
    }

    private static void startJDA(String token, VariablesStorage variablesStorage){
        log.info("Starting JDA");
        DefaultShardManagerBuilder builder = new DefaultShardManagerBuilder();
        builder.addEventListeners(new Listener(variablesStorage));
        builder.addEventListeners(new guildListener(variablesStorage));
        builder.setActivity(Activity.playing("!Nep Help"));
        builder.setToken(token);

        builder.setWebsocketFactory(new WebSocketFactory().setVerifyHostname(false));
        try {
            builder.build();
        } catch (LoginException e) {
            log.error(e.toString());
        }
    }
    private static void startJDAMusic(String token){
        log.info("Starting JDA Music");
        JDABuilder builder = new JDABuilder();
        builder.setToken(token);
        builder.addEventListeners(new PlayerControl());
        builder.setActivity(Activity.listening(".play"));
        builder.setWebsocketFactory(new WebSocketFactory().setVerifyHostname(false));
        try {
            builder.build();
        } catch (LoginException e) {
            log.error(e.toString());
        }
    }
}