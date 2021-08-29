package neptune.storage;

import neptune.storage.Enum.ProfileOptionsEnum;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
@Entity
@Table(name= "Profiles")
public class profileObject {
    protected profileObject(){}
    private int leaderboardPoints;
    @Id
    private String id;
    protected String getId(){
        return id;
    }
    @ElementCollection
    private Map<ProfileOptionsEnum, String> profileOptions;
    public profileObject(String ID){
        id = ID;
        leaderboardPoints = 0;
        profileOptions = new HashMap<>();
    }
    @Transient
    private Session session;

    public Session getSession(){
        return session;
    }
    protected void setSession(Session session){
        this.session = session;
    }
    public void closeSession(){
        if (session != null && session.isOpen()){
            if (writeOnClose){
                profileStorage profileStorage = neptune.storage.profileStorage.getInstance();
                profileStorage.serialize(this);
            }
            else session.close();
        }
    }

    public int getPoints() {
        return leaderboardPoints;
    }
    public void incrimentPoints(){
        leaderboardPoints++;
    }
    public boolean setBio(String Bio) {
        if (Bio.length() <= 700) { // leaves 300 characters for other profile options
            profileOptions.put(ProfileOptionsEnum.Bio, Bio);
            return true;
        } else return false;
    }
    public String getBio(){
        return profileOptions.getOrDefault(ProfileOptionsEnum.Bio, "Not Set");
    }

    public boolean setLanguage(String Language) {
        if (Language.length() <= 50) { // leaves 300 characters for other profile options
            profileOptions.put(ProfileOptionsEnum.Language, Language);
            return true;
        } else return false;
    }
    public String getLanguage(){
        return profileOptions.getOrDefault(ProfileOptionsEnum.Language, "Not Set");
    }
    public TimeZone getTimeZone() {
        if (profileOptions.containsKey(ProfileOptionsEnum.Timezone)) {
            return TimeZone.getTimeZone(profileOptions.get(ProfileOptionsEnum.Timezone));
        }
        return null;
    }

    public boolean setTimeZone(String Timezone) {
        TimeZone zone = TimeZone.getTimeZone(Timezone);
        if (zone != null) {
            profileOptions.put(ProfileOptionsEnum.Timezone, Timezone);
            return true;
        } else return false;
    }
    @Transient
    private boolean writeOnClose = false;
    protected void setWriteOnClose(boolean bool){
        writeOnClose = bool;
    }

    @Version
    private Integer version;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
