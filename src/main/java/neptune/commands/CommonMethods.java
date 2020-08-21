package neptune.commands;

public abstract class CommonMethods {

    protected String[] getCommandName(String MessageContent){
        String[] splitStr = MessageContent.trim().split("\\s+");
        String[] returnText = new String[2];
        if (splitStr.length == 1) {
            returnText[0] = splitStr[0].trim();
            returnText[1] = "";
        } else {
            returnText[0] = splitStr[0];
            returnText[1] = MessageContent.trim().substring(splitStr[0].length()).trim();
        }
        return returnText;
    }
    protected String getEnabledDisabledIcon(Boolean value){
        String enabled = "\u2705";
        String disabled = "\u274C";

        if(value){
            return enabled;
        }
        else return disabled;
    }
    protected String getEnabledDisabledIconText(Boolean value){
        String enabled = "\u2705 Enabled";
        String disabled = "\u274C Disabled";

        if(value){
            return enabled;
        }
        else return disabled;
    }
}
