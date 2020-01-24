package neptune.storage;

import neptune.Main;
import neptune.storage.SQLite.GetAuthToken;

import java.io.File;

public class VariablesStorage {
    GetAuthToken getAuthToken  = new GetAuthToken();
    //store global variables here
    public VariablesStorage(){
        if (Main.mode == 0){
            devMode = false;
        }
    }
    private boolean devMode = true; //uses dev key and call command when true
    private String CallBot = "!Nep";  //not case sensitive
    private String OwnerID = getAuthToken.GetToken("discord-owner-id");
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

}