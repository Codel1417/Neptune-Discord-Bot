package Neptune;

import Neptune.Commands.PassiveCommands.DM_ImageDownload;
import Neptune.Commands.PassiveCommands.guildListener;
import Neptune.Storage.DropboxBackupConnection;
import Neptune.Storage.GetAuthToken;
import Neptune.Storage.VariablesStorage;
import Neptune.music.PlayerControl;
import com.neovisionaries.ws.client.WebSocketFactory;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.util.Map;

public class Main extends ListenerAdapter {
    private static Runnable dropboxConnection;
    private final static boolean musicOnly = true;
    public static void main(String[] args) {
        //todo eliminate variableStorage for auth keys, hardcode media directory since its relative
        VariablesStorage variablesStorage = new VariablesStorage();
        GetAuthToken getAuthToken = new GetAuthToken();
        Map authKeys = getAuthToken.GetToken(new File("NepAuth.json"));

        //Auth
        variablesStorage.Init(authKeys);

        if (musicOnly){
            startJDAMusic((String) authKeys.get("music-token"));
        }
        else {
            startDropboxBackup((String) authKeys.get("dropbox"));
            startJDA(variablesStorage.getDiscordBotToken(),variablesStorage);
        }
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
