package neptune.commands;

import org.apache.commons.cli.MissingArgumentException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.dv8tion.jda.api.Permission;

public class commandBuilder {
    private String command, name, description, help;
    private commandCategories category;
    private Permission[] requiredPermissions;
    private ICommand commandInterface;
    protected static final Logger log = LogManager.getLogger();

    public commandBuilder setName(String name) {
        this.name = name;
        return this;
    }
    public commandBuilder setCommand(String command) {
        this.command = command;
        return this;
    }
    public commandBuilder setDescription(String description) {
        this.description = description;
        return this;
    }
    public commandBuilder setHelp(String help){
        this.help = help;
        return this;
    }
    public commandBuilder setRequiredPermissions(Permission[] requiredPermissions){
        this.requiredPermissions = requiredPermissions;
        return this;
    }
    public commandBuilder setRun(ICommand commandRun){
        this.commandInterface = commandRun;
        return this;
    }
    public commandBuilder setCategory(commandCategories category){
        this.category = category;
        return this;
    }
    public Command build() throws MissingArgumentException{
        if (name == null){
            for (String w : command.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {
                name = name + w + " ";
            }
        }
        if (command == null){
            throw new MissingArgumentException("Command must not be null");
        }
        if (category == null){
            throw new MissingArgumentException("CommandCatagories cannot be null");
        }
        if (description == null){
            log.debug(command + " does not have a description");
        }
        return new Command(command,name,description,help,requiredPermissions,commandInterface,category);
    }
}
