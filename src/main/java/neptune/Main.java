package neptune;

import com.neovisionaries.ws.client.WebSocketFactory;
import neptune.commands.PassiveCommands.DM_ImageDownload;
import neptune.commands.PassiveCommands.guildListener;
import neptune.music.PlayerControl;
import neptune.storage.DropboxBackupConnection;
import neptune.storage.GetAuthToken;
import neptune.storage.VariablesStorage;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.util.Map;

public class Main extends ListenerAdapter {
    private static Runnable dropboxConnection;
    private final static int mode = 1;
    private static String botToken;
    public static void main(String[] args) {
        //todo eliminate variableStorage for auth keys, hardcode media directory since its relative
        VariablesStorage variablesStorage = new VariablesStorage();
        GetAuthToken getAuthToken = new GetAuthToken();
        Map authKeys = getAuthToken.GetToken(new File("NepAuth.json"));

        //Auth
        variablesStorage.Init(authKeys);

        botToken = getJdaAuthKey(authKeys,mode);

        if (mode == 3){
            startJDAMusic(botToken);
        }
        else {
            if (mode == 0) startDropboxBackup((String) authKeys.get("dropbox"));
            startJDA(botToken,variablesStorage);
        }
    }
    private static String getJdaAuthKey(Map keys, int mode){
        switch (mode){
            case 0: { //main
                return (String) keys.get("token");
            }
            case 1: { //debug
                return (String) keys.get("dev-token");
            }
            case 2: {
                return (String) keys.get("music-token");
            }
        }
        return null;
    }

    private static void startDropboxBackup(String authKey){
        dropboxConnection = new DropboxBackupConnection(authKey);
        Thread dropboxConnectionThread = new Thread(dropboxConnection);
        dropboxConnectionThread.setName("DropboxBackupThread");
        dropboxConnectionThread.start();
    }

    private static void startJDA(String token, VariablesStorage variablesStorage){
        System.out.println("Starting JDA");
        DefaultShardManagerBuilder builder = new DefaultShardManagerBuilder();
        builder.addEventListeners(new Listener(variablesStorage));
        builder.addEventListeners(new DM_ImageDownload());
        builder.addEventListeners(new guildListener(variablesStorage));
        builder.setActivity(Activity.playing("!Nep Help"));
        builder.setToken(token);

        builder.setWebsocketFactory(new WebSocketFactory().setVerifyHostname(false));
        try {
            builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }
    private static void startJDAMusic(String token){
        System.out.println("Starting JDA Music");
        JDABuilder builder = new JDABuilder();
        builder.setToken(token);
        builder.addEventListeners(new PlayerControl());
        builder.setActivity(Activity.listening(".play"));
        builder.setWebsocketFactory(new WebSocketFactory().setVerifyHostname(false));
        try {
            builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }
}
