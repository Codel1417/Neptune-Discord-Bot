package neptune.commands;

import java.awt.Color;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.sentry.Sentry;

import org.apache.commons.lang3.StringUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class CommandRegistry {
    private final Map<String, Command> commands = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private final String prefix;
    private final Helpers commandHelpers = new Helpers();
    private final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    protected final Logger log = LogManager.getLogger();
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
                if (!Objects.requireNonNull(event.getMember()).hasPermission(command.getRequiredPermissions())){
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
    public void runCommand(SlashCommandEvent event) {
        //split the message into command and params
        String commandText = event.getName();
        if (hasCommand(commandText)) {
            Command command = commands.get(commandText);
            if (command.getRequiredPermissions() != null) {
                if (!Objects.requireNonNull(event.getMember()).hasPermission(command.getRequiredPermissions())){
                    permissionException(event);
                    return;
                }
            }
            log.trace("Running Command: " + command.getName());
            Message message = command.run(event, new MessageBuilder());
            if (message != null){
                event.reply(message).queue();
            }
        }
    }
    public void RegisterSlashCommands(JDA jda){
        for (Command command : commands.values()){
            if (command.hasSlashCommand()){
                command.register(jda);
            }
        }
    }
    private void permissionException(GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage("I'm Sorry " + event.getMember().getAsMention()+ ", You Lack the Required permission to do that!").queue();
    }
    private void permissionException(SlashCommandEvent event) {
        event.reply(("I'm Sorry " + event.getMember().getAsMention()+ ", You Lack the Required permission to do that!")).queue();
    }
    @Deprecated
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
        event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
    }
    @Deprecated
    public static class commandExecutor implements Runnable {
        private final GuildMessageReceivedEvent event;
        private final String messagecontent;
        private final Command command;
        protected final Logger log = LogManager.getLogger();

        private commandExecutor(Command command, GuildMessageReceivedEvent event, String messagecontent) {
            this.event = event;
            this.messagecontent = messagecontent;
            this.command = command;
        }

        @Override
        public void run() {
            try {
                log.trace("Running Command: " + command.getName());
                Sentry.addBreadcrumb("Running Command: " + command.getName());

                command.run(event, messagecontent, new MessageBuilder());
                log.trace("Exiting Command: " + command.getName());
            }
            catch(Exception e) {
                log.error(e);
                Sentry.captureException(e);
            }
        }
    }
}
