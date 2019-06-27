package Neptune.Storage;

import net.dv8tion.jda.core.entities.Guild;

import java.util.HashMap;
import java.util.Map;


public class StorageControllerCached implements StorageCommands{
    private HashMap<String, Boolean> CustomSoundEnabled = new HashMap<>();
    private HashMap<String, Boolean> TTSSEnabled = new HashMap<>();
    private HashMap<String, Boolean> GuildStored = new HashMap<>();

    private StorageControllerFirestore storageControllerFirestore = new StorageControllerFirestore();
    public StorageControllerCached() {
        System.out.println("Cache Enabled");
    }
    @Override
    public boolean addGuild(Guild Guild) {
        GuildStored.put(Guild.getId(), true);
        return storageControllerFirestore.addGuild(Guild);
    }

    @Override
    public boolean isGuildStored(Guild Guild) {
        if (GuildStored.containsKey(Guild.getId())) {
            return GuildStored.get(Guild.getId());
        }
        else {
            GuildStored.put(Guild.getId(), storageControllerFirestore.isGuildStored(Guild));
            return GuildStored.get(Guild.getId());
        }
    }

    @Override
    public boolean setCustomSoundsEnabled(Guild Guild, boolean value) {
        CustomSoundEnabled.put(Guild.getId(),value);
        return storageControllerFirestore.setCustomSoundsEnabled(Guild,value);
    }

    @Override
    public boolean getCustomSoundsEnabled(Guild Guild) {
        if (CustomSoundEnabled.containsKey(Guild.getId())) {
            return CustomSoundEnabled.get(Guild.getId());
        }
        else {
            CustomSoundEnabled.put(Guild.getId(), storageControllerFirestore.getCustomSoundsEnabled(Guild));
            return CustomSoundEnabled.get(Guild.getId());

        }
    }

    @Override
    public boolean incrementAnalyticForCommand(String type, String command) {
        return storageControllerFirestore.incrementAnalyticForCommand(type, command);
    }

    @Override
    public String printCollectionList(String Collection) {
        return storageControllerFirestore.printCollectionList(Collection);
    }

    @Override
    public String printDocumentValues(String collection, String document) {
        return storageControllerFirestore.printDocumentValues(collection, document);
    }

    @Override
    public boolean getTtsEnabled(Guild Guild) {
        if (TTSSEnabled.containsKey(Guild.getId())) {
            return TTSSEnabled.get(Guild.getId());
        }
        else {
            TTSSEnabled.put(Guild.getId(), storageControllerFirestore.getTtsEnabled(Guild));
            return TTSSEnabled.get(Guild.getId());
        }
    }

    @Override
    public boolean setTtsEnabled(Guild Guild, boolean value) {
        TTSSEnabled.put(Guild.getId(),value);
        return storageControllerFirestore.setTtsEnabled(Guild,value);
    }

    @Override
    public Map<String, Object> getBotInfo() {
        return storageControllerFirestore.getBotInfo();
    }


}
