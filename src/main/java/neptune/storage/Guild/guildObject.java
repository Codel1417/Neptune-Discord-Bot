package neptune.storage.Guild;

import neptune.storage.Enum.GuildOptionsEnum;
import neptune.storage.Enum.LoggingOptionsEnum;
import org.hibernate.Session;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.*;

//Stop using Subclasses
@Entity  
@Table(name= "Guilds")
@Cacheable
public class guildObject {
    public guildObject() {

    }

    public logOptionsObject getLogOptions() {
        return logOptions;
    }


    public guildOptionsObject getGuildOptions() {
        return guildOptions;
    }

    @Embedded
    private logOptionsObject logOptions;
    @Embedded
    private guildOptionsObject guildOptions;

    public guildObject(String GuildID) {
        guildID = GuildID;
        logOptions = new logOptionsObject();
        guildOptions = new guildOptionsObject();
    }

    public guildObject(String GuildID,Map<GuildOptionsEnum, Boolean> guildOptionsMap,Map<LoggingOptionsEnum, Boolean> logOptionsMap,Map<String, String> customRoleMap,String loggingChannel,int version) {
        guildID = GuildID;
        logOptions = new logOptionsObject(loggingChannel, logOptionsMap);
        guildOptions = new guildOptionsObject(guildOptionsMap);
    }

    public String getGuildID() {
        return guildID;
    }

    protected guildObject setGuildID(String guildID) {
        this.guildID = guildID;
        return this;
    }

    @Id
    private String guildID;
    @Embeddable
    public static class guildOptionsObject {
        @ElementCollection
        @MapKeyEnumerated(EnumType.STRING)
        @CollectionTable(name="GuildOptions", joinColumns=@JoinColumn(name="GuildID"))
        private final Map<GuildOptionsEnum, Boolean> GuildOptionsHashMap;

        public guildOptionsObject() {
            GuildOptionsHashMap = new HashMap<>();
            GuildOptionsHashMap.put(GuildOptionsEnum.leaderboardEnabled,true);
            GuildOptionsHashMap.put(GuildOptionsEnum.customSounds,false);
            GuildOptionsHashMap.put(GuildOptionsEnum.LeaderboardLevelUpNotification,false);
            GuildOptionsHashMap.put(GuildOptionsEnum.LoggingEnabled,false);
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
    @Embeddable
    public static class logOptionsObject {
        @ElementCollection
        @CollectionTable(name="LoggingOptions", joinColumns=@JoinColumn(name="GuildID"))
        @MapKeyEnumerated(EnumType.STRING)
        private final Map<LoggingOptionsEnum, Boolean> loggingOptions;

        @Column(nullable = false)
        private String Channel = "";

        public logOptionsObject() {
            loggingOptions = new HashMap<>();
            loggingOptions.put(LoggingOptionsEnum.GlobalLogging,false);
            loggingOptions.put(LoggingOptionsEnum.MemberActivityLogging,true);
            loggingOptions.put(LoggingOptionsEnum.TextChannelLogging,true);
            loggingOptions.put(LoggingOptionsEnum.ServerModificationLogging,true);
            loggingOptions.put(LoggingOptionsEnum.VoiceChannelLogging,true);

            Channel = "";
        }

        protected logOptionsObject(String channel, Map<LoggingOptionsEnum, Boolean> loggingOptionsMap) {
            loggingOptions = loggingOptionsMap;
            setChannel(channel);
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
            if (channelID == null){
                Channel = "";
            }
            else Channel = channelID;
        }
    }

    @Version
    private int version;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Session getSession() {
        return session;
    }

    protected void setSession(Session session) {
        this.session = session;
    }
    @Transient
    private Session session;
    public void closeSession(){
        if (session != null && session.isOpen()){
            if (writeOnClose){
                GuildStorageHandler guildStorageHandler = neptune.storage.Guild.GuildStorageHandler.getInstance();
                guildStorageHandler.writeFile(this);
            }
            else session.close();
        }
    }
    @Transient
    private boolean writeOnClose = false;
    protected void setWriteOnClose(boolean bool){
        writeOnClose = bool;
    }
}
