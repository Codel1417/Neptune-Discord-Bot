package neptune.commands.PassiveCommands;

import neptune.commands.CommandHandler;
import neptune.commands.RandomMediaPicker;
import neptune.storage.dao.GuildDao;
import neptune.storage.entity.GuildEntity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.sentry.Sentry;

import java.io.File;
import java.util.Arrays;
import javax.annotation.Nonnull;

// intercepts discord messages
public class MessageListener implements EventListener {
    protected static final Logger log = LogManager.getLogger();
    private final CommandHandler nepCommands = new CommandHandler();
    private final RandomMediaPicker randomMediaPicker = new RandomMediaPicker();

    @Override
    public void onEvent(@Nonnull GenericEvent event) {
        if (event instanceof ReadyEvent){
            nepCommands.RegisterSlashCommands(event.getJDA());
        }
        if (event instanceof GuildMessageReceivedEvent) {
            if (((GuildMessageReceivedEvent) event).getAuthor().isBot()){
                return;
            }
            GuildDao guildDao = new GuildDao();
            GuildEntity guildEntity;
            try {
                guildEntity = guildDao.getGuild(((GuildMessageReceivedEvent) event).getMember().getId());
                runEvent((GuildMessageReceivedEvent) event, guildEntity);
            } catch (Exception e) {
                log.error(e);
                Sentry.captureException(e);
                return;
            }
        }
        if (event instanceof SlashCommandEvent){
            nepCommands.run((SlashCommandEvent) event);
        }
    }

    private boolean isBotCalled(Message message, boolean multiplePrefix) {
        // check for Normal Commands
        if (Arrays.asList(message.getContentRaw().split(" ")).get(0).equalsIgnoreCase("!nep"))
            return true;

        // additional hidden media features for private use
        if (multiplePrefix) {
            String[] Split = message.getContentRaw().split(" "); // splits the message into an array
            for (String string : new String[] {"!nep", "=", "./"}) {
                if (Split[0].toLowerCase().contains(string.toLowerCase()) || Split[0].equalsIgnoreCase(string)) 
                        return true;
            }
        }
        return false;
    }

    public void runEvent(GuildMessageReceivedEvent event, GuildEntity guildEntity) {
        // check if the bot was called in chat
        try {
            boolean multiPrefix = guildEntity.getConfig().isCustomSoundsEnabled();
            if (isBotCalled(event.getMessage(), false)){
                nepCommands.run(event);
            }
            else if (isBotCalled(event.getMessage(), multiPrefix)) {
                randomMediaPicker.sendMedia(new File("Media" + File.separator + "Custom" + File.separator  + event.getMessage().getContentRaw().replace("=", "").replace("./", "")),event,true,true);
            }
            // return if bot was not called
        } catch (Exception e) {
            log.error(e);
            Sentry.captureException(e);
        }
    }
}
