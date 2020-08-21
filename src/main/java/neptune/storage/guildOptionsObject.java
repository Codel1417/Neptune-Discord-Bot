package neptune.storage;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import neptune.storage.Enum.options;
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY)
public class guildOptionsObject {
    @JsonProperty("GuildOptions")
    private Map<options, Boolean> GuildOptions;

    public guildOptionsObject(){
        GuildOptions = new HashMap<>();
    }
    
    @JsonIgnore
    public boolean getOption(options Option){
        return GuildOptions.getOrDefault(Option,false);
    }
    @JsonIgnore
    public void setOption(options Option, Boolean value){
        GuildOptions.put(Option,value);
    }
}