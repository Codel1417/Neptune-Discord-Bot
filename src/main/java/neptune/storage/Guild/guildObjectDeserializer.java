package neptune.storage.Guild;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import neptune.storage.Enum.GuildOptionsEnum;
import neptune.storage.Enum.LoggingOptionsEnum;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class guildObjectDeserializer extends JsonDeserializer<guildObject> {
    final TypeReference<HashMap<GuildOptionsEnum, Boolean>> typeRefGuildOptions =
            new TypeReference<>() {
            };
    final TypeReference<HashMap<LoggingOptionsEnum, Boolean>> typeRefLogOptions =
            new TypeReference<>() {
            };
    final TypeReference<HashMap<String, String>> typeRefCustomRole =
            new TypeReference<>() {
            };

    @Override
    public guildObject deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {

        JsonNode node = p.getCodec().readTree(p);
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader GuildOptionsReader = mapper.readerFor(typeRefGuildOptions);
        ObjectReader LoggingOptionsReader = mapper.readerFor(typeRefLogOptions);
        ObjectReader CustomRoleReader = mapper.readerFor(typeRefCustomRole);

        String guildID = node.get("guildID").asText();
        int version = node.get("version").asInt();

        Map<GuildOptionsEnum, Boolean> guildOptions;
        Map<LoggingOptionsEnum, Boolean> logOptions;
        Map<String, String> customRole;
        String channel;

        guildOptions = GuildOptionsReader.readValue(node.get("guildOptions"));
        logOptions = LoggingOptionsReader.readValue(node.get("logOptions"));
        customRole = CustomRoleReader.readValue(node.get("customRole"));

        channel = node.get("Channel").asText();
        if (channel.equalsIgnoreCase("null")) {
            channel = null;
        }

        return new guildObject(
                guildID,
                guildOptions,
                logOptions,
                customRole,
                channel,
                version);
    }
}
