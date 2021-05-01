package neptune.commands.FunCommands;

import neptune.commands.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import java.util.List;
import java.util.Random;

public class PowerLevel implements ICommand {

    @Override
    public void run(
            GuildMessageReceivedEvent event, String messageContent) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        Random random = new Random();
        int powerlevel = random.nextInt(random.nextInt(9001)) + 1;
        StringBuilder stringBuilder = new StringBuilder();
        List<Member> mention = event.getMessage().getMentionedMembers();
        stringBuilder.append("Vegeta, What does the scouter say about ");
        if (mention.size() != 0) {
            Member target = mention.get(random.nextInt(mention.size()));
            stringBuilder.append(target.getAsMention()).append("'s power level?");
        } else {
            stringBuilder.append("your power level?");
        }
        embedBuilder.setDescription(stringBuilder);
        embedBuilder.setTitle("PowerLevel");
        if (powerlevel > 9000) {
            embedBuilder.appendDescription("\nIt's over 9000!");
        } else {
            embedBuilder.appendDescription("\nIt's, " + powerlevel);
            float radiz = (float) powerlevel / (float) 1500;
            embedBuilder.appendDescription(
                    " or, " + String.format(java.util.Locale.US, "%.2f", radiz) + " Raditz");
        }
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
