package neptune.storage;

import neptune.storage.MySQL.GetAuthToken;

import java.io.File;

public class VariablesStorage {
    GetAuthToken getAuthToken  = new GetAuthToken();
    //store global variables here
    public VariablesStorage(){

    }
    private String OwnerID = getAuthToken.GetToken("discord-owner-id");
    //files
    private final File MediaFolder = new File("Media" + File.separator);


    public File getMediaFolder() {
        return MediaFolder;
    }


    public String getOwnerID() {
        return OwnerID;
    }

}