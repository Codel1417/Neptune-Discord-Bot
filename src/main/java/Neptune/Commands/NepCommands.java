package Neptune.Commands;

import Neptune.Storage.StorageControllerCached;
import Neptune.VariablesStorage;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.logging.Logger;

//handles neptune base commands
public class NepCommands {
    private final Nep NepCountCommand;
    private final VariablesStorage VariableStorageRead;
    private final Say NepSayCommand;
    private final RandomImagePicker RandomImage = new RandomImagePicker();
    private final Translate translateMessage = new Translate();
    private final RandomSoundPicker randomSoundPicker = new RandomSoundPicker();
    private AudioController audioController;
    private final AdminOptions adminOptions = new AdminOptions();

    private final static Logger Log = Logger.getLogger(NepCommands.class.getName());

    public NepCommands(StorageControllerCached storageController, VariablesStorage variablesStorage) {
        VariableStorageRead = variablesStorage;
        NepCountCommand = new Nep(storageController);
        NepSayCommand = new Say(VariableStorageRead.getSoundFolder_Say());
    }
    public boolean runCommand(String[] CommandArray, MessageReceivedEvent event, StorageControllerCached storageController) {
        String Command = CommandArray[0];
        String MessageContent = CommandArray[1];
        switch (Command.toLowerCase().trim()) {
            case "say":

                if (!event.getAuthor().getId().matches(VariableStorageRead.getOwnerID()))
                    storageController.incrementAnalyticForCommand("Commands", Command.toLowerCase().trim());

                Log.info("    Running Command: " + Command);
                NepSayCommand.sayQuoteFileList(MessageContent, event, storageController);
                return true;
            case "nepu":

                if (!event.getAuthor().getId().matches(VariableStorageRead.getOwnerID()))
                    storageController.incrementAnalyticForCommand("Commands", Command.toLowerCase().trim());

                Log.info("    Running Command: " + Command);
                NepCountCommand.nepCounter(MessageContent, "Nepu ", event);
                return true;
            case "nep":

                if (!event.getAuthor().getId().matches(VariableStorageRead.getOwnerID()))
                    storageController.incrementAnalyticForCommand("Commands", Command.toLowerCase().trim());

                Log.info("    Running Command: " + Command);
                NepCountCommand.nepCounter(MessageContent, "Nep ", event);
                return true;
            case "wan":

                if (!event.getAuthor().getId().matches(VariableStorageRead.getOwnerID()))
                    storageController.incrementAnalyticForCommand("Commands", Command.toLowerCase().trim());

                Log.info("    Running Command: " + Command);
                event.getChannel().sendMessage("Neps in your Area!!!  \n<http://wanwan-html5.moe/#Neptune> ").queue();
                return true;
            case "imagetest":

                if (!event.getAuthor().getId().matches(VariableStorageRead.getOwnerID()))
                    storageController.incrementAnalyticForCommand("Commands", Command.toLowerCase().trim());

                Log.info("    Running Command: " + Command);
                RandomImage.pickImage("nep_bot/Images/Testing/", event);
                return true;
            case "help":

                if (!event.getAuthor().getId().matches(VariableStorageRead.getOwnerID()))
                    storageController.incrementAnalyticForCommand("Commands", Command.toLowerCase().trim());

                Log.info("    Running Command: " + Command);

                StringBuilder stringBuilder = new StringBuilder();

                stringBuilder.append("```").append(VariableStorageRead.getCharacterName()).append(" Commands: \n");
                stringBuilder.append(VariableStorageRead.getCallBot()).append(" Say <Quote>  :  Plays one of the stored quotes in Voice Chat and as a Message, ").append("leaving 'Quote' empty returns the complete list of quotes, The list is delivered via DM. ").append("If there are multiple matches the results will be DMed to you. \n \n");
                stringBuilder.append(VariableStorageRead.getCallBot()).append(" Leave        :  Have ").append(VariableStorageRead.getCharacterName()).append(" disconnect the voice chat. This automatically happens when everyone else leaves the channel \n \n");
                stringBuilder.append(VariableStorageRead.getCallBot()).append(" Nep ....     :  ").append(VariableStorageRead.getCharacterName()).append(" Counts the number of Neps in the command and adds one more \n \n");
                stringBuilder.append(VariableStorageRead.getCallBot()).append(" Nepu ...     :  ").append(VariableStorageRead.getCharacterName()).append(" Counts the number of Nepus in the command and adds one more \n \n");
                stringBuilder.append(VariableStorageRead.getCallBot()).append(" Translate    :  Translate the Last non bot message into nepnep\n \n");
                stringBuilder.append(VariableStorageRead.getCallBot()).append(" About        :  Creator and Bot Invite Link");
                if (event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
                    stringBuilder.append(VariableStorageRead.getCallBot()).append(" Admin        :  Shows server admin options menu");
                }
                stringBuilder.append("```Notes: \n" + "There is a ").append(VariableStorageRead.getMessageCooldownSeconds()).append(" second cooldown between commands.```");


                event.getChannel().sendMessage(stringBuilder.toString()).queue();
                return true;
            case "owo":

                if (!event.getAuthor().getId().matches(VariableStorageRead.getOwnerID()))
                    storageController.incrementAnalyticForCommand("Commands", Command.toLowerCase().trim());

                Log.info("    Running Command: " + Command);
                event.getChannel().sendMessage("Whats This? \n<https://youtu.be/7mBqm8uO4Cg>").queue();
                return true;
            case "stats":

                if (!event.getAuthor().getId().matches(VariableStorageRead.getOwnerID()))
                    storageController.incrementAnalyticForCommand("Commands", Command.toLowerCase().trim());

                Log.info("    Running Command: " + Command);
                if (event.getAuthor().getId().matches(VariableStorageRead.getOwnerID())) {
                    StringBuilder GuildList = new StringBuilder();
                    for (Guild guild : event.getJDA().getGuilds()) {
                        GuildList.append(guild.getName()).append("\n");
                    }
                    event.getChannel().sendMessage("```Bot Owner Stats \n" +
                            "Command Stats: \n" + storageController.printDocumentValues("Analytics", "Commands") + "\n" +
                            "Total Servers: " + event.getJDA().getGuilds().size() + "\n" +
                            GuildList + "```").queue();
                }
                return true;
            case "leave":

                if (!event.getAuthor().getId().matches(VariableStorageRead.getOwnerID()))
                    storageController.incrementAnalyticForCommand("Commands", Command.toLowerCase().trim());

                Log.info("    Running Command: " + Command);
            {
                if (event.getGuild() != null && event.getGuild().getAudioManager().isConnected()) {
                    event.getGuild().getAudioManager().setSendingHandler(null);
                    event.getGuild().getAudioManager().closeAudioConnection();
                    event.getChannel().sendMessage("Neptune has left the Chat!").queue();
                }
            }
            return true;
            case "translate":

                if (!event.getAuthor().getId().matches(VariableStorageRead.getOwnerID()))
                    storageController.incrementAnalyticForCommand("Commands", Command.toLowerCase().trim());

                Log.info("    Running Command: " + Command);
                if (event.getGuild() != null) {
                    translateMessage.TranslateString(event);
                }
                return true;
            case "about": {

                if (!event.getAuthor().getId().matches(VariableStorageRead.getOwnerID()))
                    storageController.incrementAnalyticForCommand("Commands", Command.toLowerCase().trim());

                Log.info("    Running Command: " + Command);
                event.getChannel().sendMessage(VariableStorageRead.getCharacterName() + " Bot\n" +
                        "Built by <@173221092729683968> \n" +
                        "Bot Link: https://discordapp.com/api/oauth2/authorize?client_id=545565550768816138&permissions=37087296&scope=bot").queue();
                return true;
            }
            case "attack": {

                if (!event.getAuthor().getId().matches(VariableStorageRead.getOwnerID()))
                    storageController.incrementAnalyticForCommand("Commands", Command.toLowerCase().trim());

                Log.info("    Running Command: " + Command);
                randomSoundPicker.playRandomAudioFile(event, VariableStorageRead.getAttackSoundsFolder());
                return true;
            }
            case "uwu": {

                if (!event.getAuthor().getId().matches(VariableStorageRead.getOwnerID()))
                    storageController.incrementAnalyticForCommand("Commands", Command.toLowerCase().trim());

                Log.info("    Running Command: " + Command);
                event.getChannel().sendMessage("<https://www.youtube.com/watch?v=h6DNdop6pD8>").queue();
                return true;
            }
            case "ayaya": {

                if (!event.getAuthor().getId().matches(VariableStorageRead.getOwnerID()))
                    storageController.incrementAnalyticForCommand("Commands", Command.toLowerCase().trim());

                if (audioController == null) {
                    audioController = new AudioController(event);
                }
                Log.info("    Running Command: " + Command);
                audioController.playSound(event, "https://www.youtube.com/watch?v=9wnNW4HyDtg", false);
                return true;
            }
            case "admin": {
                if (!event.getAuthor().getId().matches(VariableStorageRead.getOwnerID()))
                    storageController.incrementAnalyticForCommand("Commands", Command.toLowerCase().trim());
                if (event.getMember().hasPermission(Permission.MANAGE_SERVER) || event.getAuthor().getId().matches(VariableStorageRead.getOwnerID()) ) {
                    Log.info("    Running Command: " + Command);
                    adminOptions.adminCommand(event,storageController,getCommandName(MessageContent));
                }
                return true;
            }
            default:
                return false;
        }
    }
    private String[] getCommandName(String MessageContent){
        String[] splitStr = MessageContent.trim().split("\\s+");
        String[] returnText = new String[2];
        if (splitStr.length == 1) {
            returnText[0] = splitStr[0].trim();
            returnText[1] = "";
        } else {
            returnText[0] = splitStr[0];
            returnText[1] = MessageContent.trim().substring(splitStr[0].length()).trim();
        }
        return returnText;
    }
}


