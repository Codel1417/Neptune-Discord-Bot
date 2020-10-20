package neptune.commands.FunCommands;

import neptune.commands.CommandInterface;
import neptune.commands.commandCategories;
import neptune.storage.Guild.guildObject;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;
import java.util.Random;

public class PowerLevel implements CommandInterface {
    @Override
    public String getName() {
        return "PowerLevel";
    }

    @Override
    public String getCommand() {
        return "powerLevel";
    }

    @Override
    public String getDescription() {
        return "Vegeta, What does the scouter say about his power level?";
    }

    @Override
    public commandCategories getCategory() {
        return commandCategories.Fun;
    }

    @Override
    public String getHelp() {
        return "Mention a user to check their power level, or dont.";
    }

    @Override
    public boolean getRequireManageServer() {
        return false;
    }

    @Override
    public boolean getHideCommand() {
        return false;
    }

    @Override
    public boolean getRequireManageUsers() {
        return false;
    }

    @Override
    public guildObject run(
            GuildMessageReceivedEvent event, String messageContent, guildObject guildEntity) {
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
        embedBuilder.setTitle(getName());
        if (powerlevel > 9000) {
            embedBuilder.appendDescription("\nIt's over 9000!");
        } else {
            embedBuilder.appendDescription("\nIt's, " + powerlevel);
            float radiz = (float) powerlevel / (float) 1500;
            embedBuilder.appendDescription(
                    " or, " + String.format(java.util.Locale.US, "%.2f", radiz) + " Raditz");
        }

        event.getChannel().sendMessage(embedBuilder.build()).queue();

        return guildEntity;
    }
}
