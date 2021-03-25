package neptune.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Command implements Comparable<Command> {
    private String command, name, description, help;
    private commandCategories category;
    private Permission[] requiredPermissions;
    private ICommand commandInterface;
    private Command(){}
    protected Command(String command, String name, String description,String help, Permission[] requiredPermissions, ICommand commandInterface, commandCategories category){
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
    public commandCategories getCategory() {
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
    