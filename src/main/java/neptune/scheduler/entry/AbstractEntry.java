package neptune.scheduler.entry;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.VoiceChannel;

//Every scheduled event will use this class.
public abstract class AbstractEntry {
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
    Guild guild; // target guild
    // For tasks that require a voice channel, ex: defean timeout
    VoiceChannel currentVoiceChannel;
    // Should the entry be stored and be persistent across reboots
    // tasks that effect roles should be persistent
    boolean persistent = false;
    public long getTaskRunTimeMS() {
        return taskRunTimeMS;
    }
    public void setTaskRunTimeMS(long taskRunTimeMS) {
        this.taskRunTimeMS = taskRunTimeMS;
    }
    public int getRunTimes() {
        return runTimes;
    }
    public void setRunTimes(int runTimes) {
        this.runTimes = runTimes;
    }
    public int getRepeat() {
        return repeat;
    }
    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }
    public long getTaskDelayTimeMS() {
        return taskDelayTimeMS;
    }
    public void setTaskDelayTimeMS(long taskDelayTimeMS) {
        this.taskDelayTimeMS = taskDelayTimeMS;
    }
    public long getLastRunTimeMS() {
        return lastRunTimeMS;
    }
    public void setLastRunTimeMS(long lastRunTimeMS) {
        this.lastRunTimeMS = lastRunTimeMS;
    }
    public Member getTaskInitiatorMember() {
        return taskInitiatorMember;
    }
    public void setTaskInitiatorMember(Member taskInitiatorMember) {
        this.taskInitiatorMember = taskInitiatorMember;
    }
    public Member getTaskTargetMember() {
        return taskTargetMember;
    }
    public void setTaskTargetMember(Member taskTargetMember) {
        this.taskTargetMember = taskTargetMember;
    }
    public Role[] getRolesAdded() {
        return rolesAdded;
    }
    public void setRolesAdded(Role[] rolesAdded) {
        this.rolesAdded = rolesAdded;
    }
    public Role[] getRolesRemoved() {
        return rolesRemoved;
    }
    public void setRolesRemoved(Role[] rolesRemoved) {
        this.rolesRemoved = rolesRemoved;
    }
    public Guild getGuild() {
        return guild;
    }
    public void setGuild(Guild guild) {
        this.guild = guild;
    }
    public VoiceChannel getCurrentVoiceChannel() {
        return currentVoiceChannel;
    }
    public void setCurrentVoiceChannel(VoiceChannel currentVoiceChannel) {
        this.currentVoiceChannel = currentVoiceChannel;
    }
    public boolean isPersistent() {
        return persistent;
    }
    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }
}