package Neptune.Storage;

import net.dv8tion.jda.core.entities.Guild;

import java.util.Map;

public interface StorageCommands {
    boolean addGuild(Guild Guild);
    boolean isGuildStored(Guild Guild);
    boolean setCustomSoundsEnabled(Guild Guild, boolean value);
    boolean getCustomSoundsEnabled(Guild Guild);
    boolean incrementAnalyticForCommand(String type, String command);
    String printCollectionList(String Collection);
    String printDocumentValues(String collection, String document);
    boolean getTtsEnabled(Guild Guild);
    boolean setTtsEnabled(Guild Guild, boolean value);
    Map<String, Object> getBotInfo();
}
