package neptune.storage;

import java.io.File;
import java.util.Map;

public class VariablesStorage {
    //store global variables here
    private final boolean devMode = true; //uses dev key and call command when true
    private String CallBot = "!Nep";  //not case sensitive
    private String OwnerID;
    //files
    private final File MediaFolder = new File("Media" + File.separator);


    public File getMediaFolder() {
        return MediaFolder;
    }

    public String getCallBot() {
        if (devMode) {
            return CallBot + "Dev";
        }
        else return CallBot;
    }

    public String getOwnerID() {
        return OwnerID;
    }

    //TODO, Hardcode owner-id since im the ownly owner and user id's are not secure
    public void Init(Map BotInfo) {
        if (BotInfo.containsKey("owner-id")) OwnerID = (String) BotInfo.get("owner-id");
    }
}