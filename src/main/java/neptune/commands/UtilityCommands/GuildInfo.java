package neptune.commands.UtilityCommands;

import neptune.commands.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.time.format.DateTimeFormatter;

public class GuildInfo implements ICommand {
    @Override
    public void run(
            GuildMessageReceivedEvent event, String messageContent) {
        Guild guild = event.getGuild();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        StringBuilder TextChannelList = new StringBuilder();
        StringBuilder VoiceChannelList = new StringBuilder();
        StringBuilder RoleList = new StringBuilder();
        for (TextChannel channel : guild.getTextChannels()) {
            TextChannelList.append(channel.getName()).append("\n");
        }
        for (VoiceChannel channel : guild.getVoiceChannels()) {
            VoiceChannelList.append(channel.getName()).append("\n");
        }
        for (Role role : guild.getRoles()) {
            RoleList.append(role.getName()).append("\n");
        }

        embedBuilder.setTitle("Server Info");
        embedBuilder.setColor(Color.MAGENTA);
        embedBuilder.addField("Server Owner", guild.getOwner().getUser().getName(), true);
        embedBuilder.addField("Server Region", guild.getRegion().toString(), true);
        embedBuilder.addField("Members", String.valueOf(guild.getMembers().size()), true);
        // embedBuilder.addField("Text Channels", TextChannelList.toString(),true);
        // embedBuilder.addField("Voice Channels", VoiceChannelList.toString(),true);
        // embedBuilder.addField("Roles",RoleList.toString(),true);
        embedBuilder.addField(
                "Server Created",
                guild.getTimeCreated().format(DateTimeFormatter.ISO_DATE_TIME),
                true);
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
