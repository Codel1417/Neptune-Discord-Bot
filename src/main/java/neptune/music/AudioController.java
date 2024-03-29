package neptune.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.bandcamp.BandcampAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AudioController {
    private Guild guild;
    private GuildMusicManager mng;
    private final AudioPlayerManager playerManager;
    private final Map<String, GuildMusicManager> musicManagers;
    private static final int DEFAULT_VOLUME = 35; // (0 - 150, where 100 is default max volume)
    protected static final Logger log = LogManager.getLogger();

    public AudioController(Guild guild) {
        this.playerManager = new DefaultAudioPlayerManager();
        playerManager.registerSourceManager(new YoutubeAudioSourceManager());
        // playerManager.registerSourceManager(new SoundCloudAudioSourceManager());
        playerManager.registerSourceManager(new BandcampAudioSourceManager());
        playerManager.registerSourceManager(new VimeoAudioSourceManager());
        playerManager.registerSourceManager(new TwitchStreamAudioSourceManager());
        playerManager.registerSourceManager(new HttpAudioSourceManager());
        playerManager.registerSourceManager(new LocalAudioSourceManager());

        musicManagers = new HashMap<>();
        mng = getMusicManager(guild);
    }

    public void playSound(GuildMessageReceivedEvent event, String audioURL) {
        VoiceChannel chan = Objects.requireNonNull(event.getMember().getVoiceState()).getChannel();
        guild = event.getGuild();
        mng = getMusicManager(event.getGuild());
        guild.getAudioManager().setSendingHandler(mng.sendHandler);

        if (!guild.getAudioManager().isConnected()
                && event.getMember().getVoiceState().getChannel() != null) {
            try {
                guild.getAudioManager().openAudioConnection(chan);
            } catch (PermissionException ignored) {
            }
        }
        loadAndPlay(mng, audioURL);
        log.debug("Playing audio file " + audioURL);
    }
    public void playSound(SlashCommandEvent event, String audioURL) {
        VoiceChannel chan = Objects.requireNonNull(event.getMember().getVoiceState()).getChannel();
        guild = event.getGuild();
        mng = getMusicManager(Objects.requireNonNull(event.getGuild()));
        guild.getAudioManager().setSendingHandler(mng.sendHandler);

        if (!guild.getAudioManager().isConnected()
                && event.getMember().getVoiceState().getChannel() != null) {
            try {
                guild.getAudioManager().openAudioConnection(chan);
            } catch (PermissionException ignored) {
            }
        }
        loadAndPlay(mng, audioURL);
        log.debug("Playing audio file " + audioURL);
    }
    private void loadAndPlay(GuildMusicManager mng, String url) {
        playerManager.loadItemOrdered(
                mng,
                url,
                new AudioLoadResultHandler() {
                    @Override
                    public void trackLoaded(AudioTrack track) {
                        mng.scheduler.queue(track);
                    }

                    @Override
                    public void playlistLoaded(AudioPlaylist playlist) {
                        AudioTrack firstTrack = playlist.getSelectedTrack();

                        if (firstTrack == null) {
                            firstTrack = playlist.getTracks().get(0);
                        }

                        mng.scheduler.queue(firstTrack);
                    }

                    @Override
                    public void noMatches() {}

                    @Override
                    public void loadFailed(FriendlyException exception) {
                        log.error("AudioController: Load Failed: File: " + exception);
                    }
                });
    }

    private GuildMusicManager getMusicManager(Guild guild) {
        String guildId = guild.getId();
        GuildMusicManager mng = musicManagers.get(guildId);
        if (mng == null) {
            synchronized (musicManagers) {
                mng = musicManagers.get(guildId);
                if (mng == null) {
                    mng = new GuildMusicManager(playerManager);
                    mng.player.setVolume(DEFAULT_VOLUME);
                    musicManagers.put(guildId, mng);
                }
            }
        }
        return mng;
    }
}
