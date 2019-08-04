package Neptune.Commands;

import Neptune.Commands.AdminCommands.AdminOptions;
import Neptune.Commands.FunCommands.*;
import Neptune.Commands.UtilityCommands.About;
import Neptune.Commands.UtilityCommands.Leave;
import Neptune.Commands.UtilityCommands.Screenshare;
import Neptune.Commands.UtilityCommands.Uptime;
import Neptune.Storage.StorageControllerCached;
import Neptune.Storage.VariablesStorage;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.File;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Logger;

//TODO: Make commands shard aware
//handles neptune base commands
//TODO command: Stats, Help,Say
public class NepCommands extends CommonMethods {
    private final Nep NepCountCommand;
    private final VariablesStorage VariableStorageRead;
    private final Say NepSayCommand;
    private final Translate translateMessage = new Translate();
    private final AdminOptions adminOptions = new AdminOptions();
    private final static Logger Log = Logger.getLogger(NepCommands.class.getName());
    private final RandomMediaPicker randomMediaPicker = new RandomMediaPicker();
    private final Screenshare screenshare = new Screenshare();
    private final About about = new About();
    private final Leave leave = new Leave();
    private final UWU_Translater uwu_translater = new UWU_Translater();
    private final Uptime uptime = new Uptime();
    private final Help help = new Help();
    private final CoinFlip coinFlip = new CoinFlip();
    private final RollDie rollDie = new RollDie();


    private HashMap <String, Object> commands = new HashMap<>();
    public NepCommands(VariablesStorage variablesStorage) {
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
    }

    public boolean run(MessageReceivedEvent event, StorageControllerCached storageControllerCached,VariablesStorage variablesStorage){
        String[] CommandArray = getCommandName(event.getMessage().getContentRaw().trim().toLowerCase().replaceFirst(VariableStorageRead.getCallBot().toLowerCase(), "").trim());
        System.out.println(CommandArray[0]);
        if(CommandArray[0].equalsIgnoreCase(help.getCommand())){
            return help.run(event, storageControllerCached, variablesStorage, CommandArray[1], commands);
        }
        if (commands.containsKey(CommandArray[0])){
            CommandInterface command = (CommandInterface) commands.get(CommandArray[0]);
            //TODO: add error message
            //Permission Check
            if (command.getRequireManageServer() && !event.getMember().hasPermission(Permission.MANAGE_SERVER)) {return true;}
            if (command.getRequireOwner() && !event.getAuthor().getId().matches(variablesStorage.getOwnerID())) {return true;}
            //analytics
            if (!event.getAuthor().getId().matches(VariableStorageRead.getOwnerID()))
                storageControllerCached.incrementAnalyticForCommand("Commands", command.getName().toLowerCase().trim());
            Log.info("    Running Command: " + command.getName());
            return command.run(event, storageControllerCached, variablesStorage, CommandArray[1]);
        }
        return false;
        }
}


