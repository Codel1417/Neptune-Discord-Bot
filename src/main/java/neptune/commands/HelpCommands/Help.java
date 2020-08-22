package neptune.commands.HelpCommands;

import neptune.commands.CommandInterface;
import neptune.commands.CommandRunner;
import neptune.commands.CommonMethods;
import neptune.commands.commandCategories;
import neptune.storage.guildObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.*;

public class Help extends CommonMethods implements CommandInterface {

    private EmbedBuilder sortedCommandList = null;
    @Override
    public String getName() {
        return "Help";
    }

    @Override
    public String getCommand() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Displays help";
    }

    @Override
    public commandCategories getCategory() {
        return commandCategories.Help;
    }

    @Override
    public String getHelp() {
        return getCommand() + "<Command> for more info";
    }

    @Override
    public boolean getRequireManageServer() {
        return false;
    }

    @Override
    public boolean getHideCommand() {
        return true;
    }

    @Override
    public boolean getRequireManageUsers() {
        return false;
    }

    @Override
    public guildObject run(GuildMessageReceivedEvent event,String messageContent, guildObject guildEntity) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.MAGENTA);
        embedBuilder.setAuthor("Help",event.getGuild().getSelfMember().getUser().getEffectiveAvatarUrl());
        embedBuilder.setDescription("Use !nep <Command>");

        //sort list
        Map<String, Object> commands = new TreeMap<>(new CommandRunner().getCommandList());

        String[] commandArray = getCommandName(messageContent);

        //per command check
        if (commands.containsKey(commandArray[0])){
            CommandInterface command = (CommandInterface) commands.get(commandArray[0]);
            if (!command.getHideCommand()) {
                embedBuilder.setTitle(command.getName());
                embedBuilder.setDescription(command.getDescription());
                embedBuilder.addField("Command",command.getCommand(),true);
                embedBuilder.addField("Category", command.getCategory().name(),true);
                if (!command.getHelp().equals("")){
                    embedBuilder.addField("Usage", command.getHelp(),true);
                }
            }
        }
        else {
            //only generate help list once
            if (sortedCommandList == null){
                sortedCommandList = CreateSortedCommandList(commands,embedBuilder);
            }
            embedBuilder = sortedCommandList;
        }

        //send message
        event.getChannel().sendMessage(embedBuilder.build()).queue();
        return guildEntity;
    }

    private EmbedBuilder CreateSortedCommandList(Map<String, Object> Commands, EmbedBuilder embedBuilder){
        Map<String, StringBuilder> CommandsSortedCategory = new HashMap<>();
        for (Object commandObject : Commands.values()) {
            CommandInterface command = (CommandInterface) commandObject;

            if (!command.getHideCommand()) { //exclude hidden commands
                if (!CommandsSortedCategory.containsKey(command.getCategory().name())) {
                    CommandsSortedCategory.put(command.getCategory().name(), new StringBuilder());
                }
                StringBuilder stringBuilder = CommandsSortedCategory.get(command.getCategory().name());
                stringBuilder.append("`").append(command.getCommand()).append("` ");
                CommandsSortedCategory.put(command.getCategory().name(), stringBuilder);
            }
        }
        //sort list
        CommandsSortedCategory = new TreeMap<>(CommandsSortedCategory);

        //creates the embed
        for(Map.Entry<String, StringBuilder> entry : CommandsSortedCategory.entrySet()){
            String Category = entry.getKey();
            StringBuilder stringBuilder = entry.getValue();
            embedBuilder.addField(Category, stringBuilder.toString(),false);
        }
        return embedBuilder;
    }
}
