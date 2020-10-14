package neptune.commands.nameGenCommands;

import neptune.commands.CommandInterface;
import neptune.commands.commandCategories;
import neptune.storage.guildObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Random;

public class Aarakocra implements CommandInterface {
  @Override
  public String getName() {
    return "Aarakocra Name Generator";
  }

  @Override
  public String getCommand() {
    return "aarakocra";
  }

  @Override
  public String getDescription() {
    return "";
  }

  @Override
  public commandCategories getCategory() {
    return commandCategories.NameGen;
  }

  @Override
  public String getHelp() {
    return "";
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
    Random random = new Random();
    String[] nm1 = {
      "", "", "", "", "", "c", "cl", "cr", "d", "g", "gr", "h", "k", "kh", "kl", "kr", "q", "qh",
      "ql", "qr", "r", "rh", "s", "y", "z"
    };
    String[] nm2 = {
      "a", "e", "i", "u", "a", "e", "i", "u", "a", "e", "i", "u", "a", "e", "i", "u", "a", "e", "i",
      "u", "a", "e", "i", "u", "a", "e", "i", "u", "ae", "aia", "ee", "oo", "ou", "ua", "uie"
    };
    String[] nm3 = {"c", "cc", "k", "kk", "l", "ll", "q", "r", "rr"};
    String[] nm4 = {
      "a", "e", "i", "a", "e", "i", "a", "e", "i", "a", "e", "i", "a", "e", "i", "aa", "ea", "ee",
      "ia", "ie"
    };
    String[] nm5 = {
      "", "", "", "", "c", "ck", "d", "f", "g", "hk", "k", "l", "r", "rr", "rc", "rk", "rrk", "s",
      "ss"
    };
    String result = "";

    int rnd = random.nextInt(nm1.length);
    int rnd2 = random.nextInt(nm2.length);
    int rnd3 = random.nextInt(nm5.length);
    int rnd4 = random.nextInt(nm3.length);
    int rnd5 = random.nextInt(nm4.length);

    if (random.nextInt(10) < 5) {
      while (nm1[rnd] == nm5[rnd3]) {
        rnd3 = random.nextInt(nm5.length);
      }
      result = nm1[rnd] + nm2[rnd2] + nm5[rnd3];
    } else {

      result = nm1[rnd] + nm2[rnd2] + nm3[rnd4] + nm4[rnd5] + nm5[rnd3];
    }

    // message
    EmbedBuilder embedBuilder = new EmbedBuilder();
    embedBuilder.setTitle(getName());
    embedBuilder.setDescription(result);
    event.getChannel().sendMessage(embedBuilder.build()).queue();

    return guildEntity;
  }
}
