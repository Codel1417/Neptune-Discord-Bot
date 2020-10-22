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
import neptune.storage.Enum.ProfileOptionsEnum;

public class guildObjectDeserializer extends JsonDeserializer<guildObject> {
    TypeReference<HashMap<GuildOptionsEnum, Boolean>> typeRefGuildOptions
    = new TypeReference<HashMap<GuildOptionsEnum, Boolean>>() {};
    TypeReference<HashMap<LoggingOptionsEnum, Boolean>> typeRefLogOptions
    = new TypeReference<HashMap<LoggingOptionsEnum, Boolean>>() {};
    TypeReference<HashMap<String, Integer>> typeRefLeaderboard
    = new TypeReference<HashMap<String, Integer>>() {};
    TypeReference<HashMap<String, String>> typeRefCustomRole
    = new TypeReference<HashMap<String, String>>() {};
    TypeReference<HashMap<String,HashMap<ProfileOptionsEnum, String>>> typeRefProfileOptions
    = new TypeReference<HashMap<String,HashMap<ProfileOptionsEnum, String>>>() {};
    TypeReference<HashMap<String, Byte[]>> typeRefIcons
    = new TypeReference<HashMap<String, Byte[]>>() {};

    @Override
    public guildObject deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        JsonNode node = p.getCodec().readTree(p);
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader GuildOptionsReader = mapper.readerFor(typeRefGuildOptions);
        ObjectReader LoggingOptionsReader = mapper.readerFor(typeRefLogOptions);
        ObjectReader LeaderboardReader = mapper.readerFor(typeRefLeaderboard);
        ObjectReader CustomRoleReader = mapper.readerFor(typeRefCustomRole);
        ObjectReader ProfileOptionsReader = mapper.readerFor(typeRefProfileOptions);
        ObjectReader IconsReader = mapper.readerFor(typeRefIcons);

        String guildID = node.get("guildID").asText();
        int version = node.get("version").asInt();

        Map<GuildOptionsEnum,Boolean> guildOptions;
        Map<LoggingOptionsEnum,Boolean> logOptions;
        Map<String,Integer> leaderboard;
        Map<String,String> customRole;
        Map<String,HashMap<ProfileOptionsEnum, String>> ProfileOptions;
        Map<String, Byte[]> IconMap;
        String channel;

        guildOptions = GuildOptionsReader.readValue(node.get("guildOptions"));
        logOptions = LoggingOptionsReader.readValue(node.get("logOptions"));
        leaderboard = LeaderboardReader.readValue(node.get("leaderboard"));
        customRole = CustomRoleReader.readValue(node.get("customRole"));
        if (node.has("profiles")){
            ProfileOptions = ProfileOptionsReader.readValue(node.get("profiles"));
        }
        else ProfileOptions = new HashMap<>();
        if (node.has("icons")){
            IconMap = IconsReader.readValue("icons");
        }
        else IconMap = new HashMap<>();
        channel = node.get("Channel").asText();

        return new guildObject(guildID,guildOptions,logOptions,leaderboard, customRole, channel,version,ProfileOptions,IconMap);
    }
}
