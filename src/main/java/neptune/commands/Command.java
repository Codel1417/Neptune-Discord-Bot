package neptune.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Command implements Comparable<Command> {
    private final String command;
    private final String name;
    private final String description;
    private final String help;
    private final CategoriesEnum category;
    private final Permission[] requiredPermissions;
    private final ICommand commandInterface;
    protected Command(String command, String name, String description,String help, Permission[] requiredPermissions, ICommand commandInterface, CategoriesEnum category){
        this.requiredPermissions = requiredPermissions;
        this.command = command;
        this.name = name;
        this.description = description;
        this.commandInterface = commandInterface;
        this.help = help;
        this.category = category;
    }
    public String getCommand() {
        return command;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getHelp() {
        return help;
    }
    public CategoriesEnum getCategory() {
        return category;
    }
    public Permission[] getRequiredPermissions() {
        return requiredPermissions;
    }
    public void run(GuildMessageReceivedEvent event, String messageContent) {
        commandInterface.run(event, messageContent);
    }
    @Override
    public int compareTo(Command command) {
        return this.command.compareToIgnoreCase(command.command);
    }
}
    