package neptune.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
@Table(name= "Profiles")
public class profileStorage {
    @Column(name = "points")
    private int leaderboardPoints;
    @Id
    private String id;
    private Map<ProfileOptionsEnum, String> profileOptions;

    public profileStorage(String ID){
        id = ID;
        leaderboardPoints = 0;
        profileOptions = new HashMap<>();
    }
    public static profileStorage getProfile(String ID){
        Sentry.addBreadcrumb("Loading Profile for ID: " + ID);
        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();  
        Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build(); 
        SessionFactory factory = meta.getSessionFactoryBuilder().build();  
        Session session = factory.openSession();  
        profileStorage temp = (profileStorage) session.get("neptune.storage.profile", ID);
        session.close();
        factory.close();
        ssr.close();
        //else create profile
        if (temp == null){
            temp = new profileStorage(ID);
        }
        return temp;       
    }
    public int getPoints() {
        return leaderboardPoints;
    }
    public profileStorage incrimentPoints(){
        leaderboardPoints++;
        return this;
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
        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();  
        Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build(); 
        SessionFactory factory = meta.getSessionFactoryBuilder().build();
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();   
        session.save(this);
        transaction.commit();
        session.close();
        ssr.close();
        factory.close();
    }
}
