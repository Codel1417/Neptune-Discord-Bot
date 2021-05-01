package neptune.scheduler.action;

import neptune.scheduler.entry.AbstractEntry;
import neptune.scheduler.entry.IAction;

public class VoiceDisconnect extends AbstractEntry implements IAction{

    @Override
    public void runTask() {
        getGuild().kickVoiceMember(getTaskTargetMember());  
    }
}
