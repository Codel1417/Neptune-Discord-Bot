package neptune.commands.AdminCommands;

import neptune.commands.ICommand;
import neptune.commands.commandBuilder;
import neptune.commands.AdminCommands.LoggingOptions.disableJoinLeaveLogging;
import neptune.commands.AdminCommands.LoggingOptions.disableLogging;
import neptune.commands.AdminCommands.LoggingOptions.enableJoinLeaveLogging;
import neptune.commands.AdminCommands.LoggingOptions.enableLogging;
import neptune.commands.AdminCommands.LoggingOptions.status;
import neptune.exceptions.MissingArgumentException;
import neptune.commands.CategoriesEnum;
import neptune.commands.CommandRegistry;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.sentry.Sentry;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.InvalidObjectException;

public class Logging implements ICommand {
    protected static final Logger log = LogManager.getLogger();
    final CommandRegistry commandRegistry = new CommandRegistry("!nep log");
    public Logging(){
        try {
            commandRegistry.registerCommand(new commandBuilder().setCommand("disableLogging").setCategory(CategoriesEnum.Admin).setRun(new disableLogging()).build());
            commandRegistry.registerCommand(new commandBuilder().setCommand("enableLogging").setCategory(CategoriesEnum.Admin).setRun(new enableLogging()).build());
            commandRegistry.registerCommand(new commandBuilder().setCommand("status").setCategory(CategoriesEnum.Admin).setRun(new status()).build());
            commandRegistry.registerCommand(new commandBuilder().setCommand("enableJoinLeaveLogging").setCategory(CategoriesEnum.Admin).setRun(new enableJoinLeaveLogging()).build());
            commandRegistry.registerCommand(new commandBuilder().setCommand("disableJoinLeaveLogging").setCategory(CategoriesEnum.Admin).setRun(new disableJoinLeaveLogging()).build());
        } catch (MissingArgumentException | InvalidObjectException e) {
            log.error(e);
            Sentry.captureException(e);
        }
    }
    @Override
    public Message run(GuildMessageReceivedEvent event, String messageContent, MessageBuilder builder) {
        commandRegistry.runCommand(event);
        return null;
    }
}
