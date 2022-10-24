package neptune.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import neptune.exceptions.MissingArgumentException;
import java.io.InvalidObjectException;
import java.util.Locale;

public class commandBuilder {
    private String command, name, description, help;
    private CategoriesEnum category;
    private ISlashCommand slashCommandInterface;
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
    @Deprecated
    public commandBuilder setHelp(String help){
        this.help = help;
        return this;
    }
    public commandBuilder setRun(Object command) throws InvalidObjectException {
        if (command instanceof ISlashCommand){
            this.slashCommandInterface = (ISlashCommand) command;
        }
        if (command instanceof ISlashCommand){
            return this;
        }
        else throw new InvalidObjectException("Object must implement either ICommand or ISlashCommand");
    }
    public commandBuilder setCategory(CategoriesEnum category){
        this.category = category;
        return this;
    }
    public Command build() throws MissingArgumentException{
        if (command == null){
            throw new MissingArgumentException("Command must not be null");
        }
        if (name == null){
            for (String w : command.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {
                name = "%s%s ".formatted(name, w);
            }
            assert name != null;
            name = name.trim();
        }
        if (category == null){
            throw new MissingArgumentException("CommandCategories cannot be null");
        }
        if (description == null){
            log.debug(command + " does not have a description");
        }
        if (description == null && slashCommandInterface != null){
            throw new MissingArgumentException("Description required for Slash Command");
        }
        command = command.toLowerCase(Locale.ROOT);
        return new Command(command,name,description,help,category,slashCommandInterface);
    }
}
