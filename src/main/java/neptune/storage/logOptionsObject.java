package neptune.storage;

import java.util.HashMap;
import java.util.Map;

import neptune.storage.Enum.LoggingOptionsEnum;
public class logOptionsObject{
    private Map<LoggingOptionsEnum, Boolean> loggingOptions;
    private String Channel;
    public logOptionsObject(){
        loggingOptions = new HashMap<>();
        Channel = null;
    }
    public boolean getOption(LoggingOptionsEnum option){
        return loggingOptions.getOrDefault(option,false);
    }
    public void setOption(LoggingOptionsEnum option, Boolean value){
        loggingOptions.put(option,value);
    }
    public String getChannel(){
        return Channel;
    }
    public void setChannel(String channelID){
        Channel = channelID;
    }
}