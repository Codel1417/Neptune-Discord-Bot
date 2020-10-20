package neptune.storage.Guild;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import neptune.storage.Enum.GuildOptionsEnum;
import neptune.storage.Enum.LoggingOptionsEnum;

public class guildObjectDeserializer extends JsonDeserializer<guildObject> {
    TypeReference<HashMap<GuildOptionsEnum, Boolean>> typeRefGuildOptions
    = new TypeReference<HashMap<GuildOptionsEnum, Boolean>>() {};
    TypeReference<HashMap<LoggingOptionsEnum, Boolean>> typeRefLogOptions
    = new TypeReference<HashMap<LoggingOptionsEnum, Boolean>>() {};
    TypeReference<HashMap<String, Integer>> typeRefLeaderboard
    = new TypeReference<HashMap<String, Integer>>() {};
    TypeReference<HashMap<String, String>> typeRefCustomRole
    = new TypeReference<HashMap<String, String>>() {};


    @Override
    public guildObject deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        JsonNode node = p.getCodec().readTree(p);
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader GuildOptionsReader = mapper.readerFor(typeRefGuildOptions);
        ObjectReader LoggingOptionsReader = mapper.readerFor(typeRefLogOptions);
        ObjectReader LeaderboardReader = mapper.readerFor(typeRefLeaderboard);
        ObjectReader CustomRoleReader = mapper.readerFor(typeRefCustomRole);

        String guildID = node.get("guildID").asText();
        Map<GuildOptionsEnum,Boolean> guildOptions = GuildOptionsReader.readValue(node.get("guildOptions"));
        Map<LoggingOptionsEnum,Boolean> logOptions = LoggingOptionsReader.readValue(node.get("logOptions"));
        Map<String,Integer> leaderboard = LeaderboardReader.readValue(node.get("leaderboard"));
        Map<String,String> customRole = CustomRoleReader.readValue(node.get("customRole"));

        int version = node.get("version").asInt();
        String channel = node.get("Channel").asText();

        return new guildObject(guildID,guildOptions,logOptions,leaderboard, customRole, channel,version);
    }
    
}
