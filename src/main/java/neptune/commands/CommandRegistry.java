package neptune.commands;

import java.awt.Color;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.sentry.Sentry;

import org.apache.commons.lang3.StringUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class CommandRegistry {
    private Map<String, Command> commands = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private String prefix;
    private Helpers commandHelpers = new Helpers();
    private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    public CommandRegistry(String prefix) {
        //The complete prefix before the command
        this.prefix = prefix;
        executor.prestartAllCoreThreads();
    }
    public void registerCommand(Command command){
        if (!hasCommand(command)) {
            commands.put(command.getCommand(), command);
        }
    }
    public boolean hasCommand(Command command){
        return commands.containsKey(command.getName());
    }
    public boolean hasCommand(String command){
        return commands.containsKey(command);
    }
    public void runCommand(GuildMessageReceivedEvent event) {
        //split the message into command and params
        String MessageContent = StringUtils.replaceOnceIgnoreCase(event.getMessage().getContentRaw(), prefix, "");
        String[] commandText = commandHelpers.getCommandName(MessageContent);

        if (hasCommand(commandText[0])) {
            Command command = commands.get(commandText[0]);
            if (command.getRequiredPermissions() != null) {
                if (!event.getMember().hasPermission(command.getRequiredPermissions())){
                    permissionException(event);
                    return;
                }
            }
            commandExecutor exec = new commandExecutor(command, event, commandText[1]);
            executor.execute(exec);
        }
        else { //display help on missing/invalid command
            displayHelp(event);
        }
    }
    private void permissionException(GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage("I'm Sorry " + event.getAuthor().getAsMention()+ ", You Lack the Required permission to do that!").queue();
    }
    private void displayHelp(GuildMessageReceivedEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder().setTitle( prefix + ": Available Commands").setColor(Color.MAGENTA);
        Map<String, StringBuilder> CommandsSortedCategory = new TreeMap<>();
        for (Command command : commands.values()) {
                if (!CommandsSortedCategory.containsKey(command.getCategory().name())) {
                    CommandsSortedCategory.put(command.getCategory().name(), new StringBuilder());
                }
                StringBuilder stringBuilder = CommandsSortedCategory.get(command.getCategory().name());
                stringBuilder.append("`").append(command.getCommand()).append("` ");
                CommandsSortedCategory.put(command.getCategory().name(), stringBuilder);
        }
        // creates the embed
        for (Map.Entry<String, StringBuilder> entry : CommandsSortedCategory.entrySet()) {
            embedBuilder.addField(entry.getKey(), entry.getValue().toString(), false);
        }
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }

    public class commandExecutor implements Runnable {
        private GuildMessageReceivedEvent event;
        private String messagecontent;
        private Command command;
        protected final Logger log = LogManager.getLogger();

        private commandExecutor(
                Command command,
                GuildMessageReceivedEvent event,
                String messagecontent) {
            this.event = event;
            this.messagecontent = messagecontent;
            this.command = command;
        }

        @Override
        public void run() {
            try {
                log.trace("Running Command: " + command.getName());
                Sentry.addBreadcrumb("Running Command: " + command.getName());
                command.run(event, messagecontent);
                log.trace("Exiting Command: " + command.getName());
            }
            catch(Exception e) {
                log.error(e);
                Sentry.captureException(e);
            }
        }
    }
}
