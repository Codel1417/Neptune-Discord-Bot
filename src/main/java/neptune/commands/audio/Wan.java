package neptune.commands.audio;

import neptune.commands.CommandInterface;
import neptune.commands.RandomMediaPicker;
import neptune.commands.commandCategories;
import neptune.storage.VariablesStorage;
import neptune.storage.guildObject;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.File;

public class Wan implements CommandInterface {
    @Override
    public String getName() {
        return "Wan";
    }

    @Override
    public String getCommand() {
        return "wan";
    }

    @Override
    public String getDescription() {
        return "WanWan sounds";
    }

    @Override
    public commandCategories getCategory() {
        return commandCategories.Audio;
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public boolean getRequireManageServer() {
        return false;
    }

    @Override
    public boolean getHideCommand() {
        return false;
    }

    @Override
    public boolean getRequireManageUsers() {
        return false;
    }

    @Override
    public guildObject run(GuildMessageReceivedEvent event, String messageContent, guildObject guildEntity) {
        VariablesStorage variablesStorage = new VariablesStorage();
        RandomMediaPicker randomMediaPicker = new RandomMediaPicker();
        randomMediaPicker.sendMedia(new File(variablesStorage.getMediaFolder() + File.separator + "Custom" + File.separator + getCommand()), event, false, true);
        return guildEntity;
    }
}
