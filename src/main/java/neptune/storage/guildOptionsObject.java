package neptune.storage;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import neptune.storage.Enum.options;

public class guildOptionsObject {
    @JsonProperty
    private Map<options, Boolean> GuildOptions;
    public guildOptionsObject(){
        GuildOptions = new HashMap<>();
    }
    public boolean getOption(options Option){
        return GuildOptions.getOrDefault(Option,false);
    }
    public void setOption(options Option, Boolean value){
        GuildOptions.put(Option,value);
    }
}