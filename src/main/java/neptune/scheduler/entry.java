package neptune.scheduler;

import org.apache.commons.cli.MissingArgumentException;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.VoiceChannel;

//Every scheduled event will use this class.
public class entry {
    // Time the task should be run
    long taskRunTimeMS;
    // if the task repeats, count it
    int runTimes = 0;
    // limit how many times the task is run;
    int repeat = 1;
    // Delay between tasks
    long taskDelayTimeMS;
    long lastRunTimeMS;
    Member taskInitiatorMember; // null for system/internal, used for logging/auditing
    Member taskTargetMember;
    // For tasks that affect roles, track it;
    Role[] rolesAdded;
    Role[] rolesRemoved;
    Guild guild; //target guild
    // For tasks that require a voice channel, ex: defean timeout
    VoiceChannel currentVoiceChannel;
    // Should the entry be stored and be persistent across reboots
    // tasks that effect roles should be persistent
    boolean persistent = false;
    entryTasksEnum taskType;
    private entry(){}
    //voiceDefeanTimout
    public entry(Member taskInitiatorMember ,Member taskTargetMember, int timeoutMinutes)
        throws MissingArgumentException {
        guild = taskTargetMember.getGuild();
        taskType = entryTasksEnum.voiceDefeanTimout;
        this.taskTargetMember = taskTargetMember;
        this.taskInitiatorMember = taskInitiatorMember;
        this.taskRunTimeMS = System.currentTimeMillis() + (timeoutMinutes * 1000);
        if (taskInitiatorMember == null || taskTargetMember == null){
            throw new MissingArgumentException("Member cannot be null");
        }
    }
    public void runTask(){
        switch (taskType){
            case voiceDefeanTimout: {
                guild.kickVoiceMember(taskTargetMember);
                break;
            }
        }
    }
}