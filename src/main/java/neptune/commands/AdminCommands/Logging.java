package neptune.commands.AdminCommands;

import neptune.commands.CommandInterface;
import neptune.commands.CommonMethods;
import neptune.commands.commandCategories;
import neptune.storage.guildObject;
import neptune.storage.Enum.LoggingOptionsEnum;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

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
    public boolean getHideCommand() {
        return false;
    }

    @Override
    public boolean getRequireManageUsers() {
        return false;
    }

    @Override
    public guildObject run(GuildMessageReceivedEvent event,String messageContent, guildObject guildEntity) {
        String[] command = getCommandName(messageContent);
        boolean enabledOption = false;
        if (command[1].equalsIgnoreCase("enabled")){
            enabledOption = true;
        }
        switch (command[0]){
            case "global":{
                if(enabledOption){
                    guildEntity.getLogOptions().setChannel(event.getChannel().getId());
                    guildEntity.getLogOptions().setOption(LoggingOptionsEnum.GlobalLogging, enabledOption);
                }
                else{
                    guildEntity.getLogOptions().setOption(LoggingOptionsEnum.GlobalLogging, enabledOption);

                }
                break;
            }
            case "text":{
                guildEntity.getLogOptions().setOption(LoggingOptionsEnum.TextChannelLogging, enabledOption);
                break;
            }
            case "voice":{
                guildEntity.getLogOptions().setOption(LoggingOptionsEnum.VoiceChannelLogging, enabledOption);
                break;
            }
            case "member":{
                guildEntity.getLogOptions().setOption(LoggingOptionsEnum.MemberActivityLogging, enabledOption);
                break;
            }
            case "server":{
                guildEntity.getLogOptions().setOption(LoggingOptionsEnum.ServerModificationLogging, enabledOption);
                break;
            }
        }

        displayMenu(event,guildEntity);

        return guildEntity;
    }
    private void displayMenu(GuildMessageReceivedEvent event,guildObject guildEntity){
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.MAGENTA);
        embedBuilder.setTitle("Logging Options");
        embedBuilder.setDescription("Controls Logging");

        //logging status
        String LoggingChannel = guildEntity.getLogOptions().getChannel();
        if (LoggingChannel == null){
            LoggingChannel = "";
        }

        embedBuilder.addField("Logging Status",getEnabledDisabledIconText(guildEntity.getLogOptions().getOption(LoggingOptionsEnum.GlobalLogging)),true);
        if (!LoggingChannel.equalsIgnoreCase("")){
            embedBuilder.addField("Channel", event.getGuild().getTextChannelById(LoggingChannel).getAsMention(),true);
        }

        StringBuilder logOptionsMessage = new StringBuilder();
        logOptionsMessage.append("Text Activity ").append(getEnabledDisabledIcon(guildEntity.getLogOptions().getOption(LoggingOptionsEnum.TextChannelLogging))).append("\n");
        logOptionsMessage.append("Voice Activity" ).append(getEnabledDisabledIcon(guildEntity.getLogOptions().getOption(LoggingOptionsEnum.VoiceChannelLogging))).append("\n");
        logOptionsMessage.append("Member Activity" ).append(getEnabledDisabledIcon(guildEntity.getLogOptions().getOption(LoggingOptionsEnum.MemberActivityLogging))).append("\n");
        logOptionsMessage.append("Server Changes ").append(getEnabledDisabledIcon(guildEntity.getLogOptions().getOption(LoggingOptionsEnum.ServerModificationLogging))).append("\n");

        embedBuilder.addField("Logging Options",logOptionsMessage.toString(),false);

        String prefix = "!nep " + getCommand();
        embedBuilder.addField("Logging Commands","",false);
        embedBuilder.addField("Enable Logging",prefix + " global <enabled/disabled>",true);
        embedBuilder.addField("Text Activity Logging",prefix + " text <enabled/disabled>",true);
        embedBuilder.addField("Voice Activity Logging",prefix + " voice <enabled/disabled>",true);
        embedBuilder.addField("Member Activity Logging",prefix + " member <enabled/disabled>",true);
        embedBuilder.addField("Server Changes Logging",prefix + " server <enabled/disabled>",true);

        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
