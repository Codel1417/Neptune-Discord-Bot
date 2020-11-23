package neptune.storage.Guild;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class guildObjectSerializer extends JsonSerializer<guildObject> {
    protected static final Logger log = LogManager.getLogger();

    @Override
    public void serialize(guildObject value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        try {
            gen.writeStartObject();
            gen.writeStringField("guildID", value.getGuildID());
            gen.writeObjectField("guildOptions", value.getGuildOptions().getGuildOptions());
            gen.writeObjectField("logOptions", value.getLogOptions().getloggingOptionsMap());
            gen.writeObjectField("leaderboard", value.getLeaderboard().getLeaderboards());
            gen.writeNumberField("version", 2);
            gen.writeStringField("Channel", value.getLogOptions().getChannel());
            gen.writeObjectField("customRole", value.getCustomRole().getCustomRoles());
            gen.writeObjectField("profiles", value.getProfiles().getProfilesMap());
            gen.writeObjectField("icons", value.getProfiles().getIconsMap());

            gen.writeEndObject();
            gen.close();
        } catch (Exception e) {
            log.error(e);
        }
    }
}
