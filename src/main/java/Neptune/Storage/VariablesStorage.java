package Neptune.Storage;

import java.io.File;
import java.util.Map;

public class VariablesStorage {
    //store global variables here
    private final boolean devMode = true; //uses dev key and call command when true
    private String DiscordBotToken;
    private String DiscordBotTokenDev;
    private String CallBot = "!Nep";  //not case sensitive
    private String OwnerID;
    private int MessageCooldownSeconds = 5; //set to zero to disable
    
    //files
    private final File MediaFolder = new File("Media" + File.separator);
    public String getDiscordBotToken() {
        if (devMode) {
            return DiscordBotTokenDev;
        }
        else return DiscordBotToken;
    }

    public int getMessageCooldownSeconds() {
        return MessageCooldownSeconds;
    }

    public File getMediaFolder() {
        return MediaFolder;
    }

    public String getCallBot() {
        if (devMode) {
            return CallBot + "Dev";
        }
        else return CallBot;
    }

    public boolean getDevMode() {
        return devMode;
    }

    public String getOwnerID() {
        return OwnerID;
    }

    public void Init(Map BotInfo) {
        if (BotInfo.containsKey("token")) DiscordBotToken = (String) BotInfo.get("token");
        if (BotInfo.containsKey("dev-token")) DiscordBotTokenDev = (String) BotInfo.get("dev-token");
        if (BotInfo.containsKey("owner-id")) OwnerID = (String) BotInfo.get("owner-id");

    }
}