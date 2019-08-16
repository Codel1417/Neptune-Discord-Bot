package Neptune.Storage;

import net.dv8tion.jda.core.entities.Guild;

import java.util.Map;

//Data expected in JSON format
public interface StorageCommands {
    boolean addData(String Id, String Data) throws MissingDataException;
    String getData(String Id);
    boolean updateData(String Id, String Data) throws MissingDataException;
}
