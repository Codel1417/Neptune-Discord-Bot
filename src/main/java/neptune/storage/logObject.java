package neptune.storage;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.Version;
import java.sql.Timestamp;

@Entity
public class logObject {
    public logObject() {
        timestamp = new Timestamp(System.currentTimeMillis());
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public String getGuildID() {
        return guildID;
    }

    public void setGuildID(String guildID) {
        this.guildID = guildID;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public int getVersion() {
        return version;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    private String memberID;
    private String channelID;
    private String guildID;
    @Id
    private String messageID;
    private String messageContent;
    @Version
    private int version;
    // timestamp exists for storage cleanup.
    private final Timestamp timestamp;
}
