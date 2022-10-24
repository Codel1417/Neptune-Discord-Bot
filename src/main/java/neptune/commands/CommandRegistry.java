package neptune.commands;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class CommandRegistry {
    private final Map<String, Command> commands = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    protected final Logger log = LogManager.getLogger();
    public CommandRegistry() {
        //The complete prefix before the command
        executor.prestartAllCoreThreads();
    }
    /**
     * Registers a command
     * @param command The command to register
     */
    public void registerCommand(Command command){
        if (!hasCommand(command)) {
            log.debug("Registering command: " + command.getName());
            commands.put(command.getCommand(), command);
        }
        else {
            log.warn("Command already registered: " + command.getName());
        }
    }

    /**
     * Checks if the command is registered
     * @param command The command to check
     * @return True if the command is registered
     */
    public boolean hasCommand(Command command){
        return commands.containsKey(command.getName());
    }
    /**
     * Checks if the command is registered
     * @param command The command to check
     * @return True if the command is registered
     */
    public boolean hasCommand(String command){
        return commands.containsKey(command);
    }
    public void runCommand(SlashCommandEvent event) {
        //split the message into command and params
        String commandText = event.getName();
        if (hasCommand(commandText)) {
            Command command = commands.get(commandText);
            log.debug("Running Command: " + command.getName());
            Message message = command.run(event, new MessageBuilder());
            if (message != null){
                event.reply(message).queue();
            }
        }
    }

    /**
     * Registers all slash commands in the registry with JDA and Discord
     * @param jda
     */
    public void RegisterSlashCommands(JDA jda){
        for (Command command : commands.values()){
            if (command.hasSlashCommand()){
                command.register(jda);
            }
        }
    }
}
