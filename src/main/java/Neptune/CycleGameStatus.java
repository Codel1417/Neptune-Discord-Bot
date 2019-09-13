package Neptune;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class CycleGameStatus implements EventListener {
    ArrayList<Activity> MessageLoop;
    public CycleGameStatus(){
        MessageLoop = new ArrayList<>();
        MessageLoop.add(Activity.playing("!Nep Help"));
        MessageLoop.add(Activity.listening("Nep Nep Nep Nep Nep"));
        MessageLoop.add(Activity.playing("!Nep UwU"));
        MessageLoop.add(Activity.listening("Nepu Nep Nep"));

    }

    private void onReady(ReadyEvent event){
        while (true) {
            try {
                for (Activity activity : MessageLoop){
                    Thread.sleep(1000*15); //wait 15 seconds to avoid ratelimit
                    event.getJDA().getShardManager().setActivity(activity);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onEvent(@Nonnull GenericEvent event) {
        if (event instanceof  ReadyEvent){
            onReady((ReadyEvent) event);
        }
    }
}
