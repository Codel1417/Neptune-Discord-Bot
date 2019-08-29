package Neptune;

import Neptune.Commands.PassiveCommands.DM_ImageDownload;
import Neptune.Commands.PassiveCommands.guildListener;
import Neptune.Storage.DropboxConnection;
import Neptune.Storage.GetAuthToken;
import Neptune.Storage.VariablesStorage;

import com.neovisionaries.ws.client.WebSocketFactory;
import net.dv8tion.jda.bot.entities.impl.JDABotImpl;
import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.JDAInfo;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.impl.JDAImpl;
import net.dv8tion.jda.core.events.guild.GenericGuildEvent;
import net.dv8tion.jda.core.hooks.InterfacedEventManager;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.utils.SessionController;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static net.dv8tion.jda.core.AccountType.BOT;

public class Main extends ListenerAdapter {
    private final static Logger Log = Logger.getLogger(Main.class.getName());

    private static  Runnable dropboxConnection;

    public static void main(String[] args) {
        VariablesStorage variablesStorage = new VariablesStorage();
        GetAuthToken getAuthToken = new GetAuthToken();
        Map authKeys = getAuthToken.GetToken(new File("NepAuth.json"));

        //Auth
        variablesStorage.Init(authKeys);

        //dropbox backup init
        dropboxConnection = new DropboxConnection((String) authKeys.get("dropbox"));
        Thread dropboxConnectionThread = new Thread(dropboxConnection);
        dropboxConnectionThread.setName("DropboxThread");
        dropboxConnectionThread.start();



        System.out.println("Starting JDA");
        if (variablesStorage.getDevMode()) Log.log(Level.WARNING,"WARNING! DEV MODE ENABLED!!!");

        DefaultShardManagerBuilder builder = new DefaultShardManagerBuilder();
        //JDABuilder builder = new JDABuilder(BOT);
        String token = variablesStorage.getDiscordBotToken();

        builder.setEventManager(new InterfacedEventManager());

        builder.addEventListeners(new Listener(variablesStorage));
        builder.addEventListeners(new DM_ImageDownload());
        builder.addEventListeners(new guildListener(variablesStorage));
        //builder.addEventListeners(new CycleGameStatus());
        builder.setGame(Game.playing("!Nep Help"));
        builder.setToken(token);

        builder.setWebsocketFactory(new WebSocketFactory().setVerifyHostname(false));
        try {
            builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }
}
