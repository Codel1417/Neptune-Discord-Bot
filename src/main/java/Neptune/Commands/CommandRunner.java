package Neptune.Commands;

import Neptune.Commands.AdminCommands.AdminOptions;
import Neptune.Commands.FunCommands.*;
import Neptune.Commands.FunCommands.GIF.*;
import Neptune.Commands.FunCommands.Imgur;
import Neptune.Commands.FunCommands.GreatSleepKing;
import Neptune.Commands.AdminCommands.GuildInfo;
import Neptune.Commands.HelpCommands.Help;
import Neptune.Commands.AdminCommands.Logging;
import Neptune.Commands.DevCommands.ServerInfo;
import Neptune.Commands.UtilityCommands.MinecraftServerStatus;
import Neptune.Commands.InProgress.VRChatAPI;
import Neptune.Commands.UtilityCommands.About;
import Neptune.Commands.UtilityCommands.Leave;
import Neptune.Commands.UtilityCommands.Screenshare;
import Neptune.Commands.UtilityCommands.Uptime;
import Neptune.Storage.StorageController;
import Neptune.Storage.VariablesStorage;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;

//TODO: Make commands shard aware
//handles neptune base commands
public class CommandRunner extends CommonMethods {

    private final Nep NepCountCommand;
    private final VariablesStorage VariableStorageRead;
    private final Say NepSayCommand;
    private final Translate translateMessage = new Translate();
    private final AdminOptions adminOptions = new AdminOptions();
    private final static Logger Log = Logger.getLogger(CommandRunner.class.getName());
    private final RandomMediaPicker randomMediaPicker = new RandomMediaPicker();
    private final Screenshare screenshare = new Screenshare();
    private final About about = new About();
    private final Leave leave = new Leave();
    private final UWU_Translater uwu_translater = new UWU_Translater();
    private final Uptime uptime = new Uptime();
    private final Help help = new Help();
    private final CoinFlip coinFlip = new CoinFlip();
    private final RollDie rollDie = new RollDie();
    private final Imgur imgur = new Imgur();
    private final VRChatAPI vrChatAPI = new VRChatAPI();
    private final GreatSleepKing greatSleepKing = new GreatSleepKing();
    private final PayRespect payRespect = new PayRespect();
    private final Attack attack = new Attack();
    private final Ping ping = new Ping();
    private final MinecraftServerStatus minecraftServerStatus = new MinecraftServerStatus();
    private final GuildInfo guildInfo = new GuildInfo();
    private final Logging logging = new Logging();
    private final ServerInfo serverInfo = new ServerInfo();
    private HashMap <String, Object> commands = new HashMap<>();
    private final Pat pat = new Pat();
    private final Hug hug = new Hug();
    private final Poke poke = new Poke();
    private final Cuddle cuddle = new Cuddle();
    private final Nom nom = new Nom();
    private final Confused confused = new Confused();

    public CommandRunner(VariablesStorage variablesStorage) {
        VariableStorageRead = variablesStorage;
        NepCountCommand = new Nep();
        NepSayCommand = new Say(new File(VariableStorageRead.getMediaFolder() + File.separator + "say"));

        //Add all commands to this hashmap;
        commands.put(NepCountCommand.getCommand(),NepCountCommand);
        commands.put(NepSayCommand.getCommand(), NepSayCommand);
        commands.put(translateMessage.getCommand(),translateMessage);
        commands.put(screenshare.getCommand(), screenshare);
        commands.put(about.getCommand(), about);
        commands.put(leave.getCommand(), leave);
        commands.put(uwu_translater.getCommand(), uwu_translater);
        commands.put(uptime.getCommand(), uptime);
        commands.put(adminOptions.getCommand(), adminOptions);
        commands.put(coinFlip.getCommand(), coinFlip);
        commands.put(rollDie.getCommand(), rollDie);
        commands.put(imgur.getCommand(), imgur);
        commands.put(greatSleepKing.getCommand(),greatSleepKing);
        commands.put(payRespect.getCommand(),payRespect);
        commands.put(attack.getCommand(),attack);
        commands.put(ping.getCommand(),ping);
        commands.put(minecraftServerStatus.getCommand(),minecraftServerStatus);
        commands.put(guildInfo.getCommand(), guildInfo);
        commands.put(help.getCommand(),help);
        commands.put(logging.getCommand(),logging);
        commands.put(serverInfo.getCommand(),serverInfo);
        commands.put(pat.getCommand(), pat);
        commands.put(hug.getCommand(),hug);
        commands.put(poke.getCommand(),poke);
        commands.put(cuddle.getCommand(),cuddle);
        commands.put(nom.getCommand(),nom);
        commands.put(confused.getCommand(),confused);


        //dev commands
        if(variablesStorage.getDevMode()){
            commands.put(vrChatAPI.getCommand(),vrChatAPI);
        }
        StorageController.getInstance().setCommandList(commands);
    }

    public boolean run(MessageReceivedEvent event, VariablesStorage variablesStorage){
        String[] CommandArray = getCommandName(event.getMessage().getContentRaw().trim().toLowerCase().replaceFirst(VariableStorageRead.getCallBot().toLowerCase(), "").trim());
        Log.info("        Bot Called");
        if (commands.containsKey(CommandArray[0])){
            CommandInterface command = (CommandInterface) commands.get(CommandArray[0]);
            //TODO: add error message
            //Permission Check
            if(!variablesStorage.getOwnerID().equalsIgnoreCase(event.getAuthor().getId())){
                if (command.getRequireManageServer() && !event.getMember().hasPermission(Permission.MANAGE_SERVER)) {return true;}
                if (command.getRequireOwner() && !event.getAuthor().getId().matches(variablesStorage.getOwnerID())) {return true;}
            }
            //analytics
            //storageController.incrementAnalyticForCommand(command.getName().toLowerCase().trim());
            Log.info("    Running Command: " + command.getName());
            return command.run(event, variablesStorage, CommandArray[1]);
        }
        return false;
        }
}


