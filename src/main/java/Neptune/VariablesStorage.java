package Neptune;

import java.io.File;
import java.util.Map;

public class VariablesStorage {
    //store global variables here
    private final boolean devMode = true; //uses dev key and call command when true
    private String CharacterName;
    private String DiscordBotToken;
    private String DiscordBotTokenDev;
    private String CallBot = "!Nep";  //not case sensitive
    private String OwnerID;
    private int MessageCooldownSeconds; //set to zero to disable
    
    //files
    private final File MediaFolder = new File("Media" + File.separator);
    private final File SoundFolder_Say = new File(getMediaFolder() + File.separator + "say");
    private final File attackSoundsFolder = new File(getMediaFolder() + File.separator + "Attack");
    private final File CustomSoundsFolder = new File(getMediaFolder() + File.separator + "Custom");
    String getDiscordBotToken() {
        if (devMode) {
            return DiscordBotTokenDev;
        }
        else return DiscordBotToken;
    }
    File getCustomSoundsFolder(){ return CustomSoundsFolder;}

    public int getMessageCooldownSeconds() {
        return MessageCooldownSeconds;
    }

    private File getMediaFolder() {
        return MediaFolder;
    }

    public File getSoundFolder_Say() {
        return SoundFolder_Say;
    }

    public String getCallBot() {
        if (devMode) {
            return CallBot + "Dev";
        }
        else return CallBot;
    }

    public String getCharacterName() {
        if (devMode) {
            return CharacterName + "Dev";
        }
        else return CharacterName;
    }

    boolean getDevMode() {
        return devMode;
    }

    public String getOwnerID() {
        return OwnerID;
    }

    public File getAttackSoundsFolder() {
        return attackSoundsFolder;
    }

    void Init(Map BotInfo) {
        if (BotInfo.containsKey("Token")) DiscordBotToken = (String) BotInfo.get("Token");
        if (BotInfo.containsKey("TokenDev")) DiscordBotTokenDev = (String) BotInfo.get("TokenDev");
        if (BotInfo.containsKey("Name")) CharacterName = (String) BotInfo.get("Name");
        if (BotInfo.containsKey("Prefix")) CallBot = (String) BotInfo.get("Prefix");
        if (BotInfo.containsKey("OwnerID")) OwnerID = (String) BotInfo.get("OwnerID");
        if (BotInfo.containsKey("DefaultCoolDownSecond")) MessageCooldownSeconds = Math.toIntExact((Long) BotInfo.get("DefaultCoolDownSecond"));

    }
}