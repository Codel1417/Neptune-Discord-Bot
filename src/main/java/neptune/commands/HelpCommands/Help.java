package neptune.commands.HelpCommands;

import neptune.commands.CommandInterface;
import neptune.commands.CommandRunner;
import neptune.commands.CommonMethods;
import neptune.commands.commandCategories;
import neptune.storage.VariablesStorage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Help extends CommonMethods implements CommandInterface {

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
        return getCommand() + "<Command/Category>   for more detailed info";
    }

    @Override
    public boolean getRequireManageServer() {
        return false;
    }

    @Override
    public boolean getRequireOwner() {
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
    public boolean run(MessageReceivedEvent event, VariablesStorage variablesStorage, String messageContent) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.MAGENTA);
        embedBuilder.setAuthor("Help",event.getGuild().getSelfMember().getUser().getEffectiveAvatarUrl());
        embedBuilder.setDescription("Use " + variablesStorage.getCallBot() + " <Command>");

        Map<String, Object> commands = new TreeMap<String, Object>(new CommandRunner(variablesStorage).getCommandList());

        String[] commandArray = getCommandName(messageContent);
        commandCategories category = null;
        //checks if a category was specified
        if (!commandArray[0].equalsIgnoreCase("")){
            for (commandCategories commandCat : commandCategories.values()){
                if (commandCat.name().equalsIgnoreCase(commandArray[0])){
                    category = commandCat;
                    for (Object commandObject : commands.values()){
                        CommandInterface command = (CommandInterface) commandObject;
                        if (command.getCategory().name().equalsIgnoreCase(category.name()) && !command.getHideCommand() ){
                            embedBuilder.addField(command.getCommand(), command.getDescription(),false);
                        }
                    }
                    break;
                }
            }
        }
        //check if command exists to display per command help
        boolean perCommandHelp = false;
        if (category == null) {
            for (Object commandObject : commands.values()){
                CommandInterface command = (CommandInterface) commandObject;
                if (command.getCommand().equalsIgnoreCase(commandArray[0]) || command.getName().equalsIgnoreCase(commandArray[0])){
                    if (!command.getHideCommand()) {
                        embedBuilder.setTitle(command.getName());
                        embedBuilder.setDescription(command.getDescription());
                        embedBuilder.addField("Command",command.getCommand(),true);
                        embedBuilder.addField("Category", command.getCategory().name(),true);
                        if (command.getHelp() != ""){
                            embedBuilder.addField("Usage", command.getHelp(),true);
                        }
                        perCommandHelp = true;
                        break;
                    }
                }
            }
        }


        //display all commands
        if(!perCommandHelp && category == null){
/*            for (Object commandObject : commands.values()){
                CommandInterface command = (CommandInterface) commandObject;
                if (!command.getHideCommand()){
                    embedBuilder.addField(command.getCommand(), command.getDescription(),false);
                }
            }*/
        embedBuilder = CreateSortedCommandList(commands,embedBuilder);
        }

        //send message
        event.getChannel().sendMessage(embedBuilder.build()).queue();
        return true;
    }

    private EmbedBuilder CreateSortedCommandList(Map<String, Object> Commands, EmbedBuilder embedBuilder){
        Map<String, StringBuilder> CommandsSortedCategory = new HashMap<>();
        for (Object commandObject : Commands.values()) {
            CommandInterface command = (CommandInterface) commandObject;

            if (!command.getHideCommand()) { //exclude hidden commands
                //Create array if it doesnt exist. COuld probably make this a String early
                if (!CommandsSortedCategory.containsKey(command.getCategory().name())) {
                    CommandsSortedCategory.put(command.getCategory().name(), new StringBuilder());
                }
                StringBuilder stringBuilder = CommandsSortedCategory.get(command.getCategory().name());
                stringBuilder.append("`").append(command.getCommand()).append("` ");
                CommandsSortedCategory.put(command.getCategory().name(), stringBuilder);
            }
        }
        CommandsSortedCategory = new TreeMap<>(CommandsSortedCategory);

        for(Map.Entry<String, StringBuilder> entry : CommandsSortedCategory.entrySet()){
            String Category = entry.getKey();
            StringBuilder stringBuilder = entry.getValue();
            embedBuilder.addField(Category, stringBuilder.toString(),false);
        }
        return embedBuilder;
    }
}
