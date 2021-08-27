package neptune.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.persistence.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import io.sentry.Sentry;
import neptune.storage.Enum.ProfileOptionsEnum;

@Entity
public class profileStorage {
    private int leaderboardPoints;
    @Id
    private String id;
    @ElementCollection
    private Map<ProfileOptionsEnum, String> profileOptions;
    private static  StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
    private static Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();
    private static SessionFactory factory = meta.getSessionFactoryBuilder().build();
    public profileStorage(String ID){
        id = ID;
        leaderboardPoints = 0;
        profileOptions = new HashMap<>();
    }

    public profileStorage() {

    }
    private Session session;
    public void closeSession(){
        if (session != null && session.isOpen()){
            session.close();
        }
    }
    public static profileStorage getProfile(String ID){
        Sentry.addBreadcrumb("Loading Profile for ID: " + ID);
        Session session = factory.openSession();
        profileStorage temp = (profileStorage) session.get("neptune.storage.profileStorage", ID);
        if (temp != null){
            temp.session = session;
        }
        else {
            temp = new profileStorage(ID);
            temp.session = session;
        }
        return temp;       
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

    public void serialize(){
        Sentry.addBreadcrumb("Saving Profile for ID: " + id);
        Transaction transaction = session.beginTransaction();
        session.save(this);
        transaction.commit();
        session.close();
    }
}
