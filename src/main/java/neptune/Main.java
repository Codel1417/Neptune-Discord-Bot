package neptune;

import com.neovisionaries.ws.client.WebSocketFactory;

import neptune.commands.PassiveCommands.Listener;
import neptune.storage.commandLineOptionsSingleton;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.security.auth.login.LoginException;

public class Main extends ListenerAdapter {
    static Options Options = new Options();
    protected static final Logger log = LogManager.getLogger();

    public static void main(String[] args) {

        // CLI
        Options.addRequiredOption("d", "discord-token", true, "The discord bot token");
        Options.addRequiredOption("t", "tenor", true, "Tenor api key");
        Options.addRequiredOption("o", "owner-id", true, "My Discord member id;");
        Options.addOption("m", "media-dir", true, " Directory to look for media"); // not yet used
        Options.addOption(
                "l", "legacy-import", false, " Convert between legacy and new storage system");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(Options, args);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        commandLineOptionsSingleton.getInstance().setOptions(cmd);

        startJDA(cmd.getOptionValue("d"));
    }

    private static void startJDA(String token) {
        log.info("Starting JDA");
        DefaultShardManagerBuilder builder = new DefaultShardManagerBuilder();
        builder.addEventListeners(new Listener());
        builder.setActivity(Activity.playing("!Nep Help"));
        builder.setToken(token);

        builder.setWebsocketFactory(new WebSocketFactory().setVerifyHostname(false));
        try {
            builder.build();
        } catch (LoginException e) {
            log.error(e.toString());
        }
    }
}
