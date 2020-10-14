package neptune.storage;

import java.io.File;

public class VariablesStorage {
  // store global variables here
  public VariablesStorage() {}

  private String OwnerID =
      commandLineOptionsSingleton.getInstance().getOptions().getOptionValue("o");
  // files
  private final File MediaFolder = new File("Media" + File.separator);

  public File getMediaFolder() {
    return MediaFolder;
  }

  public String getOwnerID() {
    return OwnerID;
  }
}
