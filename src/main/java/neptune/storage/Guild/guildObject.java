package neptune.storage.Guild;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import neptune.storage.Enum.GuildOptionsEnum;
import neptune.storage.Enum.LoggingOptionsEnum;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.*;

//Stop using Subclasses
@Entity  
@Table(name= "Guilds")
@JsonDeserialize(using = guildObjectDeserializer.class)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
public class guildObject {
    public guildObject() {

    }

    public logOptionsObject getLogOptions() {
        return logOptions;
    }

    public customRoleObject getCustomRole() {
        return customRole;
    }

    public guildOptionsObject getGuildOptions() {
        return guildOptions;
    }

    public guildObject setLogOptionsEntity(logOptionsObject logOptionsEntity) {
        logOptions = logOptionsEntity;
        return this;
    }
    @Transient
    private logOptionsObject logOptions;
    @Transient
    private customRoleObject customRole;
    @Transient
    private guildOptionsObject guildOptions;

    public guildObject(String GuildID) {
        guildID = GuildID;
        version = 3;
        logOptions = new logOptionsObject();
        customRole = new customRoleObject();
        guildOptions = new guildOptionsObject();
    }

    public guildObject(String GuildID,Map<GuildOptionsEnum, Boolean> guildOptionsMap,Map<LoggingOptionsEnum, Boolean> logOptionsMap,Map<String, String> customRoleMap,String loggingChannel,int version) {
        guildID = GuildID;
        version = this.version;
        logOptions = new logOptionsObject(loggingChannel, logOptionsMap);
        customRole = new customRoleObject(customRoleMap);
        guildOptions = new guildOptionsObject(guildOptionsMap);
    }

    public String getGuildID() {
        return guildID;
    }

    protected guildObject setGuildID(String guildID) {
        this.guildID = guildID;
        return this;
    }

    public int getVersion() {
        return version;
    }

    protected guildObject setVersion(int version) {
        this.version = version;
        return this;
    }
    @Id
    private String guildID;
    @Version int version;
    @Entity
    public static class guildOptionsObject extends guildObject {
        @ElementCollection
        private final Map<GuildOptionsEnum, Boolean> GuildOptionsHashMap;

        public guildOptionsObject() {
            GuildOptionsHashMap = new HashMap<>();
        }

        protected guildOptionsObject(Map<GuildOptionsEnum, Boolean> GuildOptionsMap) {
            GuildOptionsHashMap = GuildOptionsMap;
        }

        public boolean getOption(GuildOptionsEnum Option) {
            return GuildOptionsHashMap.getOrDefault(Option, false);
        }

        public void setOption(GuildOptionsEnum Option, Boolean value) {
            GuildOptionsHashMap.put(Option, value);
        }

    }
    @Entity
    public static class logOptionsObject extends guildObject{
        @ElementCollection
        private final Map<LoggingOptionsEnum, Boolean> loggingOptions;

        private String Channel = null;

        public logOptionsObject() {
            loggingOptions = new HashMap<>();
            Channel = null;
        }

        protected logOptionsObject(
                String channel, Map<LoggingOptionsEnum, Boolean> loggingOptionsMap) {
            loggingOptions = loggingOptionsMap;
            Channel = channel;
        }

        public boolean getOption(LoggingOptionsEnum option) {
            return loggingOptions.getOrDefault(option, false);
        }

        public void setOption(LoggingOptionsEnum option, Boolean value) {
            loggingOptions.put(option, value);
        }

        public String getChannel() {
            return Channel;
        }

        public void setChannel(String channelID) {
            Channel = channelID;
        }
    }
    @Entity
    public static class customRoleObject extends guildObject{
        @ElementCollection
        private final Map<String, String> customRoles;

        public customRoleObject() {
            customRoles = new HashMap<>();
        }

        protected customRoleObject(Map<String, String> customRolesMap) {
            customRoles = customRolesMap;
        }

        public String getRoleID(String MemberID) {
            return customRoles.getOrDefault(MemberID, null);
        }

        public void addRole(String MemberID, String RoleID) {
            customRoles.put(MemberID, RoleID);
        }

        public void removeRole(String MemberID) {
            customRoles.remove(MemberID);
        }

    }

}
