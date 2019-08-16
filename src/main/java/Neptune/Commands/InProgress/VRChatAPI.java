package Neptune.Commands.InProgress;

import Neptune.Commands.CommandInterface;
import Neptune.Commands.commandCategories;
import Neptune.Storage.StorageController;
import Neptune.Storage.VariablesStorage;
import io.github.vrchatapi.SearchType;
import io.github.vrchatapi.VRCCredentials;
import io.github.vrchatapi.VRCRemoteConfig;
import io.github.vrchatapi.VRCUser;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class VRChatAPI implements CommandInterface {
    @Override
    public String getName() {
        return "VRChat";
    }

    @Override
    public String getCommand() {
        return "vrc";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public commandCategories getCategory() {
        return commandCategories.Utility;
    }

    @Override
    public String getHelp() {
        return "";
    }

    @Override
    public boolean getRequireManageServer() {
        return false;
    }

    @Override
    public boolean getRequireOwner() {
        return true;
    }

    @Override
    public boolean run(MessageReceivedEvent event, StorageController storageController, VariablesStorage variablesStorage, String messageContent) {
        VRCRemoteConfig.init();
        boolean init = VRCRemoteConfig.isInitialized();
        System.out.println("VRCHAt init = " + init);
        VRCUser.login();

        return false;
    }
}
