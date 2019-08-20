package Neptune.Commands.HelpCommands;

import Neptune.Commands.CommandInterface;
import Neptune.Commands.CommonMethods;
import Neptune.Commands.commandCategories;
import Neptune.Storage.StorageController;
import Neptune.Storage.VariablesStorage;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.HashMap;

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
        return "";
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

        HashMap<String, Object> commands = StorageController.getInstance().getCommandList();

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
            for (Object commandObject : commands.values()){
                CommandInterface command = (CommandInterface) commandObject;
                if (!command.getHideCommand()){
                    embedBuilder.addField(command.getCommand(), command.getDescription(),false);
                }
            }
        }

        //send message
        event.getChannel().sendMessage(embedBuilder.build()).queue();
        return true;
    }
}
