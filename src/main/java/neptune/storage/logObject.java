package neptune.storage;

import org.hibernate.Session;

import javax.persistence.*;
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

    @Column(length = 1000)
    private String messageContent;
    @Version
    private int version;
    // timestamp exists for storage cleanup.
    private final Timestamp timestamp;

    @Transient
    private Session session;

    public void setSession(Session session){
        this.session = session;
    }
    public Session getSession(){
        return session;
    }
}
