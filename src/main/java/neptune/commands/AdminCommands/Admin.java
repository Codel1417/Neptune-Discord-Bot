package neptune.commands.AdminCommands;

import neptune.commands.CategoriesEnum;
import neptune.commands.CommandRegistry;
import neptune.commands.ICommand;
import neptune.commands.commandBuilder;
import neptune.commands.AdminCommands.Options.disableCustomRole;
import neptune.commands.AdminCommands.Options.disableLevelUpNotifications;
import neptune.commands.AdminCommands.Options.enableCustomRole;
import neptune.commands.AdminCommands.Options.enableLevelUpNotifications;
import neptune.commands.AdminCommands.Options.status;
import neptune.exceptions.MissingArgumentException;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Admin implements ICommand {
    CommandRegistry commandRegistry = new CommandRegistry("!nep options");
    protected static final Logger log = LogManager.getLogger();

    public Admin(){
        try {
            commandRegistry.registerCommand(new commandBuilder().setCommand("enableCustomRole").setCategory(CategoriesEnum.Admin).setRun(new enableCustomRole()).build());
            commandRegistry.registerCommand(new commandBuilder().setCommand("disableCustomRole").setCategory(CategoriesEnum.Admin).setRun(new disableCustomRole()).build());
            commandRegistry.registerCommand(new commandBuilder().setCommand("status").setCategory(CategoriesEnum.Admin).setRun(new status()).build());

            //commandRegistry.registerCommand(new commandBuilder().setCommand("enableLevelUpNotifications").setCategory(CategoriesEnum.Admin).setRun(new enableLevelUpNotifications()).build());
            //commandRegistry.registerCommand(new commandBuilder().setCommand("disableLevelUpNotifications").setCategory(CategoriesEnum.Admin).setRun(new disableLevelUpNotifications()).build());
        } catch (MissingArgumentException e) {
            log.error(e);
        }
    }
    @Override
    public void run(GuildMessageReceivedEvent event, String messageContent) {
        commandRegistry.runCommand(event);
    }
    
}
