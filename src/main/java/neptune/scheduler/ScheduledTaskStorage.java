package neptune.scheduler;

import java.util.ArrayList;
import neptune.scheduler.entry.IAction;
public class ScheduledTaskStorage {
    private static volatile ScheduledTaskStorage _instance;
    protected static synchronized ScheduledTaskStorage getInstance() {
        if (_instance == null) {
            synchronized (ScheduledTaskStorage.class) {
                if (_instance == null) _instance = new ScheduledTaskStorage();
            }
        }
        return _instance;
    }

    private ArrayList<IAction> entries = new ArrayList<IAction>();

    public void addEntry(IAction entry){
        entries.add(entry);
    }
    public ArrayList<IAction> getEntries(){
        return entries;
    }
    public void removeEntry(IAction entry){
        entries.remove(entry);
    }
}
