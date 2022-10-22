package neptune.storage.entity;

import jakarta.persistence.*;

import java.util.TimeZone;
@Entity
@Table(name= "Profiles")
@Cacheable
public class ProfileEntity {
    protected ProfileEntity(){}
    private int leaderboardPoints;
    @Id
    private String id;
    public String getId(){
        return id;
    }

    public ProfileEntity(String ID){
        id = ID;
        leaderboardPoints = 0;
    }

    public int getPoints() {
        return leaderboardPoints;
    }
    public void incrimentPoints(){
        leaderboardPoints++;
    }
    public boolean setBio(String Bio) {
        if (Bio.length() <= 700) { // leaves 300 characters for other profile options
            bio = Bio;
            return true;
        } else return false;
    }
    private String bio = "Not Set";
    public String getBio(){
        return bio;
    }

    private String language = "Not Set";
    public boolean setLanguage(String Language) {
        if (Language.length() <= 50) { // leaves 300 characters for other profile options
            language = Language;
            return true;
        } else return false;
    }
    public String getLanguage(){
        return language;
    }
    private String timeZone = "Not Set";

    public TimeZone getTimeZone() {
        if (!timeZone.equalsIgnoreCase("Not Set")) {
            return TimeZone.getTimeZone(timeZone);
        }
        return null;
    }

    public boolean setTimeZone(String Timezone) {
        TimeZone zone = TimeZone.getTimeZone(Timezone);
        if (zone != null) {
            timeZone = Timezone;
            return true;
        } else return false;
    }
    @ManyToOne
    @JoinColumn(name = "guildID")
    private GuildEntity guildEntity;

    public int getLeaderboardPoints() {
        return leaderboardPoints;
    }

    public void setLeaderboardPoints(int leaderboardPoints) {
        this.leaderboardPoints = leaderboardPoints;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GuildEntity getGuildEntity() {
        return guildEntity;
    }

    public void setGuildEntity(GuildEntity guildEntity) {
        this.guildEntity = guildEntity;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Version
    private Integer version;
}
