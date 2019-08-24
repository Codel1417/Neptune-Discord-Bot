package Neptune.Commands.AdminCommands;

import Neptune.Commands.CommandInterface;
import Neptune.Commands.CommonMethods;
import Neptune.Commands.commandCategories;
import Neptune.Storage.StorageController;
import Neptune.Storage.VariablesStorage;
import com.google.gson.internal.LinkedTreeMap;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.Map;

public class Logging extends CommonMethods implements CommandInterface {
    @Override
    public String getName() {
        return "Logging Options";
    }

    @Override
    public String getCommand() {
        return "log";
    }

    @Override
    public String getDescription() {
        return "Control logging and set channel";
    }

    @Override
    public commandCategories getCategory() {
        return commandCategories.Admin;
    }

    @Override
    public String getHelp() {
        return "";
    }

    @Override
    public boolean getRequireManageServer() {
        return true;
    }

    @Override
    public boolean getRequireOwner() {
        return false;
    }

    @Override
    public boolean getHideCommand() {
        return false;
    }

    @Override
    public boolean getRequireManageUsers() {
        return false;
    }

    @Override
    public boolean run(MessageReceivedEvent event, VariablesStorage variablesStorage, String messageContent) {
        LinkedTreeMap<String, Object> guildSettings = (LinkedTreeMap<String, Object>) StorageController.getInstance().getGuild(event.getGuild());
        LinkedTreeMap<String, String> LoggingInfo = (LinkedTreeMap<String, String>) guildSettings.getOrDefault("Logging", new LinkedTreeMap<String, String>());


        if(messageContent.equalsIgnoreCase("")){
            displayMenu(event,variablesStorage, LoggingInfo);
            return true;
        }

        String[] command = getCommandName(messageContent);
        boolean enabledOption = false;
        if (command[1].equalsIgnoreCase("enabled")){
            enabledOption = true;
        }
        switch (command[0]){
            case "global":{
                if(enabledOption){
                    LoggingInfo.put("LoggingChannel", event.getTextChannel().getId());
                }
                else LoggingInfo.put("LoggingChannel","");
                StorageController.getInstance().updateGuildField(event.getGuild(),"Logging",LoggingInfo);
                break;
            }
            case "text":{
                if(enabledOption){
                    LoggingInfo.put("LogTextActivity", "enabled");
                }
                else LoggingInfo.put("LogTextActivity","disabled");
                StorageController.getInstance().updateGuildField(event.getGuild(),"Logging",LoggingInfo);
                break;
            }
            case "voice":{
                if(enabledOption){
                    LoggingInfo.put("LogVoiceActivity", "enabled");
                }
                else LoggingInfo.put("LogVoiceActivity","disabled");
                StorageController.getInstance().updateGuildField(event.getGuild(),"Logging",LoggingInfo);
                break;
            }
            case "member":{
                if(enabledOption){
                    LoggingInfo.put("LogMemberActivity", "enabled");
                }
                else LoggingInfo.put("LogMemberActivity","disabled");
                StorageController.getInstance().updateGuildField(event.getGuild(),"Logging",LoggingInfo);
                break;
            }
            case "server":{
                if(enabledOption){
                    LoggingInfo.put("LogServerActivity", "enabled");
                }
                else LoggingInfo.put("LogServerActivity","disabled");
                StorageController.getInstance().updateGuildField(event.getGuild(),"Logging",LoggingInfo);
                break;
            }
            case "neptune":{
                if(enabledOption){
                    LoggingInfo.put("LogSelfActivity", "enabled");
                }
                else LoggingInfo.put("LogSelfActivity","disabled");
                StorageController.getInstance().updateGuildField(event.getGuild(),"Logging",LoggingInfo);
                break;
            }
        }
        displayMenu(event,variablesStorage,LoggingInfo);

        return false;
    }
    private boolean displayMenu(MessageReceivedEvent event, VariablesStorage variablesStorage, LinkedTreeMap<String,String> LoggingInfo){
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.MAGENTA);
        embedBuilder.setTitle("Logging Options");
        embedBuilder.setDescription("Controls Logging");

        //logging status
        String LoggingChannel = LoggingInfo.getOrDefault("LoggingChannel","");
        String LoggingStatus;
        if (LoggingChannel.equalsIgnoreCase("")){
            LoggingStatus = "disabled";
        }
        else LoggingStatus = "enabled";

        embedBuilder.addField("Logging Status",getEnabledDisabledIconText(LoggingStatus),true);
        if (!LoggingChannel.equalsIgnoreCase("")){
            embedBuilder.addField("Channel", event.getGuild().getTextChannelById(LoggingChannel).getAsMention(),true);
        }

        StringBuilder logOptionsMessage = new StringBuilder();
        logOptionsMessage.append("Text Activity ").append(getEnabledDisabledIcon(LoggingInfo.getOrDefault("LogTextActivity","disabled"))).append("\n");
        logOptionsMessage.append("Voice Activity" ).append(getEnabledDisabledIcon(LoggingInfo.getOrDefault("LogVoiceActivity","disabled"))).append("\n");
        logOptionsMessage.append("Member Activity" ).append(getEnabledDisabledIcon(LoggingInfo.getOrDefault("LogMemberActivity","disabled"))).append("\n");
        logOptionsMessage.append("Server Changes ").append(getEnabledDisabledIcon(LoggingInfo.getOrDefault("LogServerActivity","disabled"))).append("\n");
        //logOptionsMessage.append("Neptune Changes").append(getEnabledDisabledIcon(LoggingInfo.getOrDefault("LogSelfActivity","disabled"))).append("\n");

        embedBuilder.addField("Logging Options",logOptionsMessage.toString(),false);

        String prefix = variablesStorage.getCallBot() + " " + getCommand();
        embedBuilder.addField("Logging Commands","",false);
        embedBuilder.addField("Enable Logging",prefix + " global <enabled/disabled>",true);
        embedBuilder.addField("Text Activity Logging",prefix + " text <enabled/disabled>",true);
        embedBuilder.addField("Voice Activity Logging",prefix + " voice <enabled/disabled>",true);
        embedBuilder.addField("Member Activity Logging",prefix + " member <enabled/disabled>",true);
        embedBuilder.addField("Server Changes Logging",prefix + " server <enabled/disabled>",true);
        //embedBuilder.addField("Neptune Settings Logging",prefix + " neptune <enabled/disabled>",true);

        event.getChannel().sendMessage(embedBuilder.build()).queue();
        return true;
    }
}
