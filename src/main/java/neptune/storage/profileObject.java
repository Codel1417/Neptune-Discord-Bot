package neptune.storage;

import org.hibernate.Session;

import javax.persistence.*;
import java.util.TimeZone;
@Entity
public class profileObject {
    protected profileObject(){}
    private int leaderboardPoints;
    @Id
    private String id;
    protected String getId(){
        return id;
    }

    public profileObject(String ID){
        id = ID;
        leaderboardPoints = 0;
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
            bio = Bio;
            return true;
        } else return false;
    }
    private String bio = new String("Not Set");
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
