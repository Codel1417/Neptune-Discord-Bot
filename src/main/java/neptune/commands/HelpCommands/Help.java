package neptune.commands.HelpCommands;

import neptune.commands.ICommand;
import neptune.commands.Command;
import neptune.commands.CommandHandler;
import neptune.commands.CommandHelpers;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.*;

public class Help extends CommandHelpers implements ICommand {

    private EmbedBuilder sortedCommandList = null;

    @Override
    public void run(GuildMessageReceivedEvent event, String messageContent) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.MAGENTA);
        embedBuilder.setAuthor(
                "Help", event.getGuild().getSelfMember().getUser().getEffectiveAvatarUrl());
        embedBuilder.setDescription("Use !nep <Command>");

        // sort list
        ArrayList<Command> commands = new ArrayList<>(new CommandHandler().getCommandList());

        String[] commandArray = getCommandName(messageContent);

        // When user specifies !nep help command, if additional help is availible (and is not hidden), build and display embed. 
/*        if (commands.containsKey(commandArray[0])) {
            Command command = commands.get(commandArray[0]);
                embedBuilder.setTitle(command.getName());
                embedBuilder.setDescription(command.getDescription());
                embedBuilder.addField("Command", command.getCommand(), true);
                embedBuilder.addField("Category", command.getCategory().name(), true);
                if (!command.getHelp().equals("")) {
                    embedBuilder.addField("Usage", command.getHelp(), true);
                }

        } else {
            // If sortedCommandList hasn't been generated yet this run, Generate it. 
            if (sortedCommandList == null) {
                sortedCommandList = CreateSortedCommandList(commands, embedBuilder);
            }
            embedBuilder = sortedCommandList;
        }
*/
        if (sortedCommandList == null) {
            sortedCommandList = CreateSortedCommandList(commands, embedBuilder);
        }
        // send message
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }

    // Create an embed programmatically to display a list of all availible commands. 
    private EmbedBuilder CreateSortedCommandList(ArrayList<Command> Commands, EmbedBuilder embedBuilder) {
        Map<String, StringBuilder> CommandsSortedCategory = new HashMap<>();
        for (Command command : Commands) {
                if (!CommandsSortedCategory.containsKey(command.getCategory().name())) {
                    CommandsSortedCategory.put(command.getCategory().name(), new StringBuilder());
                }
                StringBuilder stringBuilder =
                        CommandsSortedCategory.get(command.getCategory().name());
                stringBuilder.append("`").append(command.getCommand()).append("` ");
                CommandsSortedCategory.put(command.getCategory().name(), stringBuilder);
        }
        // sort list
        CommandsSortedCategory = new TreeMap<>(CommandsSortedCategory);
        // creates the embed
        for (Map.Entry<String, StringBuilder> entry : CommandsSortedCategory.entrySet()) {
            String Category = entry.getKey();
            StringBuilder stringBuilder = entry.getValue();
            embedBuilder.addField(Category, stringBuilder.toString(), false);
        }
        return embedBuilder;
    }
}
