package neptune.commands.PassiveCommands;

import neptune.CycleGameStatus;
import neptune.messageInterprter;
import neptune.serverLogging.GuildLogging;
import neptune.storage.GuildStorageHandler;
import neptune.storage.VariablesStorage;
import neptune.storage.guildObject;
import neptune.storage.Enum.LoggingOptionsEnum;
import neptune.storage.guildObject.logOptionsObject;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.channel.text.GenericTextChannelEvent;
import net.dv8tion.jda.api.events.channel.voice.GenericVoiceChannelEvent;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.guild.member.GenericGuildMemberEvent;
import net.dv8tion.jda.api.events.guild.update.GenericGuildUpdateEvent;
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;

import javax.annotation.Nonnull;

import java.io.IOException;

//intercepts discord messages
public class Listener implements EventListener {
    private final neptune.messageInterprter messageInterprter;
    private final GuildLogging guildLogging = new GuildLogging();
    private Runnable CycleActivity;
    private boolean ActivityThread;

    GuildStorageHandler guildStorageHandler = new GuildStorageHandler();

    public Listener(VariablesStorage variableStorageRead) {
        messageInterprter = new messageInterprter(variableStorageRead);
    }

    @Override
    public void onEvent(@Nonnull GenericEvent event) {
        // Cycle Activity Status Thread
        if (event instanceof ReadyEvent && !ActivityThread) {
            CycleActivity = new CycleGameStatus((ReadyEvent) event);
            Thread CycleActivityThread = new Thread(CycleActivity);
            CycleActivityThread.setName("CycleActivityThread");
            CycleActivityThread.start();
            ActivityThread = true;
        }

        // Leaderboard
        if (event instanceof GuildMessageReceivedEvent) {
            if (((GuildMessageReceivedEvent) event).getAuthor().isBot())
                return;
            guildObject guildEntity;
            try {
                guildEntity = guildStorageHandler.readFile(((GuildMessageReceivedEvent) event).getGuild().getId());
                guildEntity.getLeaderboard().incrimentPoint(((GuildMessageReceivedEvent) event).getMember().getId());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Commands
        if (event instanceof MessageReceivedEvent){
            if (((MessageReceivedEvent) event).getAuthor().isBot()) return;
            messageInterprter.runEvent((MessageReceivedEvent) event);

        }

        //logging
        if (event instanceof GenericGuildEvent){
            String GuildID = ((GenericGuildEvent) event).getGuild().getId();

            if (GuildID == null) return;
            guildObject guildEntity;
            try {
                guildEntity = guildStorageHandler.readFile(GuildID);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            logOptionsObject logOptionsEntity = guildEntity.getLogOptions();
            logOptionsEntity.getChannel();
            if (logOptionsEntity.getChannel() == null) return;
            if(!logOptionsEntity.getOption(LoggingOptionsEnum.GlobalLogging)) return;

            if(event instanceof GenericGuildVoiceEvent){
                guildLogging.GuildVoice((GenericGuildVoiceEvent) event,logOptionsEntity);
            }
            else if(event instanceof GenericGuildMessageEvent){
                guildLogging.GuildText((GenericGuildMessageEvent) event,logOptionsEntity);
            }
            else if(event instanceof GenericGuildMemberEvent){
                guildLogging.GuildMember((GenericGuildMemberEvent) event,logOptionsEntity);
            }
            else if (event instanceof GenericGuildUpdateEvent){
                guildLogging.GuildSettings((GenericGuildUpdateEvent) event,logOptionsEntity);
            }
            else if (event instanceof GenericTextChannelEvent){
                guildLogging.GuildTextChannel((GenericTextChannelEvent) event,logOptionsEntity);
            }
            else if (event instanceof GenericVoiceChannelEvent){
                guildLogging.GuildVoiceChannel((GenericVoiceChannelEvent) event,logOptionsEntity);
            }
        }
    }
}



