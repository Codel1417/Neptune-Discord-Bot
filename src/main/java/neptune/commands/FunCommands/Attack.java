package neptune.commands.FunCommands;

import neptune.commands.ICommand;
import neptune.commands.RandomMediaPicker;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.File;
import java.util.List;
import java.util.Random;

public class Attack implements ICommand {
    @Override
    public void run(
            GuildMessageReceivedEvent event, String messageContent) {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        RandomMediaPicker randomMediaPicker = new RandomMediaPicker();
        List<Member> mention = event.getMessage().getMentionedMembers();
        stringBuilder.append("Neptune attacked ");
        if (mention.size() != 0) {
            Member target = mention.get(random.nextInt(mention.size()));
            stringBuilder.append(target.getAsMention()).append(" ");
        }
        stringBuilder.append("for ").append(random.nextInt(6)).append(" damage");
        // send message + audio
        event.getChannel().sendMessage(stringBuilder).queue();

        randomMediaPicker.sendMedia(
                new File("Media" + File.separator + "Attack"),
                event,
                false,
                true);
    }
}
