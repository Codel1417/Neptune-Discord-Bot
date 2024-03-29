package neptune.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

public class Command implements Comparable<Command> {
    protected static final Logger log = LogManager.getLogger();

    private final String command;
    @Deprecated
    private final String name;
    private final String description;
    @Deprecated
    private final String help;
    @Deprecated
    private final CategoriesEnum category;
    private final ISlashCommand slashCommandInterface;

    protected Command(String command, String name, String description, String help, CategoriesEnum category, ISlashCommand slashCommandInterface){
        this.command = command;
        this.name = name;
        this.description = description;
        this.help = help;
        this.category = category;
        this.slashCommandInterface = slashCommandInterface;
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
    @Nullable
    @Deprecated
    public String getHelp() {
        return help;
    }
    @Deprecated
    public CategoriesEnum getCategory() {
        return category;
    }
    public boolean hasSlashCommand(){
        return slashCommandInterface != null;
    }
    public Message run(SlashCommandEvent event, MessageBuilder builder) {
        return slashCommandInterface.run(event, builder);
    }
    protected Command register(JDA jda){
        try {
            if (hasSlashCommand()){
                log.trace("Registering Slash Command: " + getName());
                CommandData commandData = new CommandData(getCommand(),getDescription());
                CommandData commandData2 = slashCommandInterface.RegisterCommand(commandData);
                if (commandData2 != null){
                    commandData = commandData2;
                }
                CommandCreateAction commandCreateAction = jda.upsertCommand(commandData);
                commandCreateAction.queue();
            }
        }
        catch (Exception e){
            log.error(e);
        }
        return this;
    }
    @Override
    public int compareTo(Command command) {
        return this.command.compareToIgnoreCase(command.command);
    }
}
    