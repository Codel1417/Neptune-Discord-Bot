package neptune.commands;

import neptune.commands.AdminCommands.*;
import neptune.commands.DevCommands.*;
import neptune.commands.FunCommands.*;
import neptune.commands.GeneralCommands.*;
import neptune.commands.HelpCommands.Help;
import neptune.commands.ImageCommands.*;
import neptune.commands.ImageCommands.Tenor.*;
import neptune.commands.UtilityCommands.*;
import neptune.commands.audio.*;
import neptune.storage.Guild.GuildStorageHandler;
import neptune.storage.Guild.guildObject;
import neptune.storage.VariablesStorage;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// handles neptune base commands
public class CommandRunner extends CommonMethods {
    protected static final Logger log = LogManager.getLogger();
    private HashMap<String, Object> commands = new HashMap<>();
    private final Nep NepCountCommand = new Nep();
    private final Say NepSayCommand;
    private final Translate translateMessage = new Translate();
    private final AdminOptions adminOptions = new AdminOptions();
    private final Screenshare screenshare = new Screenshare();
    private final About about = new About();
    private final Leave leave = new Leave();
    private final UWU_Translater uwu_translater = new UWU_Translater();
    private final Uptime uptime = new Uptime();
    private final Help help = new Help();
    private final CoinFlip coinFlip = new CoinFlip();
    private final RollDie rollDie = new RollDie();
    private final Imgur imgur = new Imgur();
    private final GreatSleepKing greatSleepKing = new GreatSleepKing();
    private final PayRespect payRespect = new PayRespect();
    private final Attack attack = new Attack();
    private final Ping ping = new Ping();
    private final MinecraftServerStatus minecraftServerStatus = new MinecraftServerStatus();
    private final GuildInfo guildInfo = new GuildInfo();
    private final Logging logging = new Logging();
    private final ServerInfo serverInfo = new ServerInfo();
    private final Pat pat = new Pat();
    private final Hug hug = new Hug();
    private final Poke poke = new Poke();
    private final Cuddle cuddle = new Cuddle();
    private final Nom nom = new Nom();
    private final Confused confused = new Confused();
    private final D10K d10K = new D10K();
    private final GuildList guildList = new GuildList();
    private final PowerLevel powerLevel = new PowerLevel();
    private final Pout pout = new Pout();
    private final Senko senko = new Senko();
    private final Stare stare = new Stare();
    private final Neko neko = new Neko();
    private final Shocked shocked = new Shocked();
    private final Nya nya = new Nya();
    private final Sleepy sleepy = new Sleepy();
    private final WhyWasIBreached whyWasIBreached = new WhyWasIBreached();
    private final IsCaliforniaOnFire isCaliforniaOnFire = new IsCaliforniaOnFire();
    private final CustomRole customRole = new CustomRole();
    private final Leaderboard leaderboard = new Leaderboard();
    private final Magic8Ball magic8Ball = new Magic8Ball();
    private final Awoo awoo = new Awoo();
    private final Wan wan = new Wan();
    private final moreJpeg moreJpeg = new moreJpeg();
    private final anime4k anime4k = new anime4k();
    private final unixTime unixTime = new unixTime();
    private final ocr ocr = new ocr();
    private final profile profile = new profile();
    private final a a = new a();
    private final bonk bonk = new bonk();
    ExecutorService executor = Executors.newCachedThreadPool();

    public CommandRunner() {
        VariablesStorage variablesStorage = new VariablesStorage();
        NepSayCommand =
                new Say(new File(variablesStorage.getMediaFolder() + File.separator + "say"));

        // Add all commands to this hashmap;
        commands.put(NepCountCommand.getCommand(), NepCountCommand);
        commands.put(NepSayCommand.getCommand(), NepSayCommand);
        commands.put(translateMessage.getCommand(), translateMessage);
        commands.put(screenshare.getCommand(), screenshare);
        commands.put(about.getCommand(), about);
        commands.put(leave.getCommand(), leave);
        commands.put(uwu_translater.getCommand(), uwu_translater);
        commands.put(uptime.getCommand(), uptime);
        commands.put(adminOptions.getCommand(), adminOptions);
        commands.put(coinFlip.getCommand(), coinFlip);
        commands.put(rollDie.getCommand(), rollDie);
        commands.put(imgur.getCommand(), imgur);
        commands.put(greatSleepKing.getCommand(), greatSleepKing);
        commands.put(payRespect.getCommand(), payRespect);
        commands.put(attack.getCommand(), attack);
        commands.put(ping.getCommand(), ping);
        commands.put(minecraftServerStatus.getCommand(), minecraftServerStatus);
        commands.put(guildInfo.getCommand(), guildInfo);
        commands.put(help.getCommand(), help);
        commands.put(logging.getCommand(), logging);
        commands.put(serverInfo.getCommand(), serverInfo);
        commands.put(pat.getCommand(), pat);
        commands.put(hug.getCommand(), hug);
        commands.put(poke.getCommand(), poke);
        commands.put(cuddle.getCommand(), cuddle);
        commands.put(nom.getCommand(), nom);
        commands.put(confused.getCommand(), confused);
        commands.put(d10K.getCommand(), d10K);
        commands.put(guildList.getCommand(), guildList);
        commands.put(powerLevel.getCommand(), powerLevel);
        commands.put(pout.getCommand(), pout);
        commands.put(senko.getCommand(), senko);
        commands.put(stare.getCommand(), stare);
        commands.put(neko.getCommand(), neko);
        commands.put(shocked.getCommand(), shocked);
        commands.put(nya.getCommand(), nya);
        commands.put(sleepy.getCommand(), sleepy);
        commands.put(whyWasIBreached.getCommand(), whyWasIBreached);
        commands.put(isCaliforniaOnFire.getCommand(), isCaliforniaOnFire);
        commands.put(customRole.getCommand(), customRole);
        commands.put(leaderboard.getCommand(), leaderboard);
        commands.put(magic8Ball.getCommand(), magic8Ball);
        commands.put(awoo.getCommand(), awoo);
        commands.put(wan.getCommand(), wan);
        commands.put(moreJpeg.getCommand(), moreJpeg);
        commands.put(anime4k.getCommand(), anime4k);
        commands.put(unixTime.getCommand(), unixTime);
        commands.put(ocr.getCommand(), ocr);
        commands.put(profile.getCommand(), profile);
        commands.put(a.getCommand(), a);
        commands.put(bonk.getCommand(), bonk);
    }

    public Map<String, Object> getCommandList() {
        return commands;
    }

    public boolean run(GuildMessageReceivedEvent event, guildObject guildEntity) {
        String[] CommandArray =
                getCommandName(event.getMessage().getContentRaw().replaceFirst("!nep", ""));
        CommandInterface command = null;

        /*
            Check for Command
            This allows me to check for the command regardless of case
            commands.get(key) is case sensitive

        */

        for (Map.Entry<String, Object> set : commands.entrySet()) {
            if (CommandArray[0].equalsIgnoreCase(set.getKey())) {
                command = (CommandInterface) set.getValue();
                break;
            }
        }
        if (command != null) {
            // Permission Check
            if (command.getRequireManageServer()
                    && !event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
                permissionException(event);
                return false;
            } else {
                log.info("NEPTUNE: Running Command: " + command.getName());
                commandExecutor exec =
                        new commandExecutor(command, event, CommandArray[1], guildEntity);
                executor.execute(exec);
                return true;
            }
        }
        return false;
    }

    private void permissionException(GuildMessageReceivedEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.RED);
        embedBuilder.setDescription("You Lack the Required permission to do that!");
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }

    /*
        Due to some commands taking more longer to run, commands are now run in their own thread
    */
    public class commandExecutor implements Runnable {
        GuildMessageReceivedEvent event;
        String messagecontent;
        guildObject guildEntity;
        CommandInterface command;
        protected final Logger log = LogManager.getLogger();

        public commandExecutor(
                CommandInterface command,
                GuildMessageReceivedEvent event,
                String messagecontent,
                guildObject guildEntity) {
            this.event = event;
            this.messagecontent = messagecontent;
            this.guildEntity = guildEntity;
            this.command = command;
        }

        @Override
        public void run() {
            guildEntity = command.run(event, messagecontent, guildEntity);
            try {
                new GuildStorageHandler().writeFile(guildEntity);
            } catch (IOException e) {
                log.error(e);
            }
        }
    }
}
