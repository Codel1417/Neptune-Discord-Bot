package neptune.storage;

import java.io.File;

public class VariablesStorage {
    // files
    private final File MediaFolder = new File("Media" + File.separator);

    public File getMediaFolder() {
        return MediaFolder;
    }
}
