package neptune.commands.AdminCommands;

import neptune.commands.CommandInterface;
import neptune.commands.CommonMethods;
import neptune.commands.commandCategories;
import neptune.storage.SQLite.SettingsStorage;
import neptune.storage.VariablesStorage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.Map;

public class Logging extends CommonMethods implements CommandInterface {
    private final SettingsStorage settingsStorage = new SettingsStorage();
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
        Map<String, String> LoggingInfo = settingsStorage.getGuildSettings(event.getGuild().getId());

        String[] command = getCommandName(messageContent);
        boolean enabledOption = false;
        if (command[1].equalsIgnoreCase("enabled")){
            enabledOption = true;
        }
        switch (command[0]){
            case "global":{
                if(enabledOption){
                    LoggingInfo.put("LoggingChannel", event.getTextChannel().getId());
                    LoggingInfo.put("LoggingEnabled", "enabled");
                }
                else{
                    LoggingInfo.put("LoggingEnabled", "disabled");

                }
                settingsStorage.updateGuild(event.getGuild().getId(),"LoggingChannel",LoggingInfo.get("LoggingChannel"));
                settingsStorage.updateGuild(event.getGuild().getId(),"LoggingEnabled",LoggingInfo.get("LoggingEnabled"));
                break;
            }
            case "text":{
                if(enabledOption){
                    LoggingInfo.put("TextChannelLogging", "enabled");
                }
                else LoggingInfo.put("TextChannelLogging","disabled");
                settingsStorage.updateGuild(event.getGuild().getId(),"TextChannelLogging",LoggingInfo.get("TextChannelLogging"));
                break;
            }
            case "voice":{
                if(enabledOption){
                    LoggingInfo.put("VoiceChannelLogging", "enabled");
                }
                else LoggingInfo.put("VoiceChannelLogging","disabled");
                settingsStorage.updateGuild(event.getGuild().getId(),"VoiceChannelLogging",LoggingInfo.get("VoiceChannelLogging"));
                break;
            }
            case "member":{
                if(enabledOption){
                    LoggingInfo.put("MemberActivityLogging", "enabled");
                }
                else LoggingInfo.put("MemberActivityLogging","disabled");
                settingsStorage.updateGuild(event.getGuild().getId(),"MemberActivityLogging",LoggingInfo.get("MemberActivityLogging"));
                break;
            }
            case "server":{
                if(enabledOption){
                    LoggingInfo.put("ServerModificationLogging", "enabled");
                }
                else LoggingInfo.put("ServerModificationLogging","disabled");
                settingsStorage.updateGuild(event.getGuild().getId(),"ServerModificationLogging",LoggingInfo.get("ServerModificationLogging"));
                break;
            }
            case "neptune":{
                //Does nothing
                if(enabledOption){
                    LoggingInfo.put("LogSelfActivity", "enabled");
                }
                else LoggingInfo.put("LogSelfActivity","disabled");
                break;
            }
        }

        displayMenu(event,variablesStorage,LoggingInfo);

        return false;
    }
    private void displayMenu(MessageReceivedEvent event, VariablesStorage variablesStorage, Map<String,String> LoggingInfo){
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.MAGENTA);
        embedBuilder.setTitle("Logging Options");
        embedBuilder.setDescription("Controls Logging");

        //logging status
        String LoggingChannel = LoggingInfo.getOrDefault("LoggingChannel","");
        if (LoggingChannel == null){
            LoggingChannel = "";
        }

        String LoggingStatus = LoggingInfo.getOrDefault("LoggingEnabled","");
        if (LoggingStatus == null){
            LoggingStatus = "disabled";
        }

        embedBuilder.addField("Logging Status",getEnabledDisabledIconText(LoggingStatus),true);
        if (!LoggingChannel.equalsIgnoreCase("")){
            embedBuilder.addField("Channel", event.getGuild().getTextChannelById(LoggingChannel).getAsMention(),true);
        }

        StringBuilder logOptionsMessage = new StringBuilder();
        logOptionsMessage.append("Text Activity ").append(getEnabledDisabledIcon(LoggingInfo.getOrDefault("TextChannelLogging","disabled"))).append("\n");
        logOptionsMessage.append("Voice Activity" ).append(getEnabledDisabledIcon(LoggingInfo.getOrDefault("VoiceChannelLogging","disabled"))).append("\n");
        logOptionsMessage.append("Member Activity" ).append(getEnabledDisabledIcon(LoggingInfo.getOrDefault("MemberActivityLogging","disabled"))).append("\n");
        logOptionsMessage.append("Server Changes ").append(getEnabledDisabledIcon(LoggingInfo.getOrDefault("ServerModificationLogging","disabled"))).append("\n");
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
    }
}
