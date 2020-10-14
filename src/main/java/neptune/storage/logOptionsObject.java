package neptune.storage;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import neptune.storage.Enum.LoggingOptionsEnum;

import java.util.HashMap;
import java.util.Map;

public class logOptionsObject {
    @JsonProperty("loggingOptions")
    private Map<LoggingOptionsEnum, Boolean> loggingOptions;

    @JsonProperty("Channel")
    private String Channel = null;

    public logOptionsObject() {
        loggingOptions = new HashMap<>();
        Channel = null;
    }

    @JsonIgnore
    public boolean getOption(LoggingOptionsEnum option) {
        return loggingOptions.getOrDefault(option, false);
    }

    @JsonIgnore
    public void setOption(LoggingOptionsEnum option, Boolean value) {
        loggingOptions.put(option, value);
    }

    @JsonGetter("Channel")
    public String getChannel() {
        return Channel;
    }

    @JsonSetter("Channel")
    public void setChannel(String channelID) {
        Channel = channelID;
    }
}
