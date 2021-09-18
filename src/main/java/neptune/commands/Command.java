package neptune.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Command implements Comparable<Command> {
    private final String command;
    @Deprecated
    private final String name;
    private final String description;
    @Deprecated
    private final String help;
    @Deprecated
    private final CategoriesEnum category;
    private final Permission[] requiredPermissions;
    @Deprecated
    private final ICommand commandInterface;
    private final ISlashCommand slashCommandInterface;

    protected Command(String command, String name, String description,String help, Permission[] requiredPermissions, ICommand commandInterface, CategoriesEnum category, ISlashCommand slashCommandInterface){
        this.requiredPermissions = requiredPermissions;
        this.command = command;
        this.name = name;
        this.description = description;
        this.commandInterface = commandInterface;
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
    @Nullable
    public Permission[] getRequiredPermissions() {
        return requiredPermissions;
    }
    public boolean hasSlashCommand(){
        return slashCommandInterface != null;
    }
    @Deprecated
    public boolean hasLegacyCommand(){
        return commandInterface != null;
    }
    @Deprecated
    public void run(GuildMessageReceivedEvent event, String messageContent, MessageBuilder builder) {
        commandInterface.run(event, messageContent, builder);
    }
    public Message run(SlashCommandEvent event, MessageBuilder builder) {
        return slashCommandInterface.run(event, builder);
    }
    protected Command register(JDA jda){
        if (hasSlashCommand()){
            CommandData commandData = new CommandData(getCommand(),getDescription());
            CommandData commandData2 = slashCommandInterface.RegisterCommand(commandData);
            List<OptionData> data = commandData2.getOptions();
            List<OptionData> data2 = new ArrayList<>();
            for (OptionData op : data){
                op.setName(op.getName().toLowerCase(Locale.ROOT));
                data2.add(op) ;
            }
            commandData.addOptions(data2);
            CommandCreateAction commandCreateAction = jda.upsertCommand(commandData);
            commandCreateAction.queue();
        }
        return this;
    }
    @Override
    public int compareTo(Command command) {
        return this.command.compareToIgnoreCase(command.command);
    }
}
    