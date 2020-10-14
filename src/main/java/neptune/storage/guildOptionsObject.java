package neptune.storage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import neptune.storage.Enum.GuildOptionsEnum;

import java.util.HashMap;
import java.util.Map;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class guildOptionsObject {
    @JsonProperty("GuildOptionsEntity")
    private Map<GuildOptionsEnum, Boolean> GuildOptions;

    public guildOptionsObject() {
        GuildOptions = new HashMap<>();
    }

    @JsonIgnore
    public boolean getOption(GuildOptionsEnum Option) {
        return GuildOptions.getOrDefault(Option, false);
    }

    @JsonIgnore
    public void setOption(GuildOptionsEnum Option, Boolean value) {
        GuildOptions.put(Option, value);
    }
}
