package neptune.commands.AdminCommands;

import neptune.commands.AdminCommands.Options.disableLevelUpNotifications;
import neptune.commands.AdminCommands.Options.enableLevelUpNotifications;
import neptune.commands.CategoriesEnum;
import neptune.commands.CommandRegistry;
import neptune.commands.ICommand;
import neptune.commands.commandBuilder;
import neptune.exceptions.MissingArgumentException;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.sentry.Sentry;

import java.io.InvalidObjectException;

public class Admin implements ICommand {
    final CommandRegistry commandRegistry = new CommandRegistry("!nep options");
    protected static final Logger log = LogManager.getLogger();

    public Admin(){
        try {
            //commandRegistry.registerCommand(new commandBuilder().setCommand("disableCustomRole").setCategory(CategoriesEnum.Admin).setRun(new disableCustomRole()).build());
            //commandRegistry.registerCommand(new commandBuilder().setCommand("status").setCategory(CategoriesEnum.Admin).setRun(new status()).build());

            commandRegistry.registerCommand(new commandBuilder().setCommand("enableLevelUpNotifications").setCategory(CategoriesEnum.Admin).setRun(new enableLevelUpNotifications()).build());
            commandRegistry.registerCommand(new commandBuilder().setCommand("disableLevelUpNotifications").setCategory(CategoriesEnum.Admin).setRun(new disableLevelUpNotifications()).build());
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
