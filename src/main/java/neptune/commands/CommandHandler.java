package neptune.commands;

import neptune.commands.AdminCommands.*;
import neptune.commands.FunCommands.*;
import neptune.commands.GeneralCommands.*;
import neptune.commands.HelpCommands.Help;
import neptune.commands.ImageCommands.*;
import neptune.commands.ImageCommands.Tenor.*;
import neptune.commands.UtilityCommands.*;
import neptune.commands.audio.*;
import neptune.storage.Guild.guildObject;
import neptune.storage.VariablesStorage;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import org.apache.commons.cli.MissingArgumentException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// handles neptune base commands
public class CommandHandler extends CommandHelpers {
    protected static final Logger log = LogManager.getLogger();
    private ArrayList<Command> commands = new ArrayList<>();
    ExecutorService executor = Executors.newCachedThreadPool();
    public CommandHandler() {
        VariablesStorage variablesStorage = new VariablesStorage();
        // Add all commands;
        try{
            commands.add(new commandBuilder().setCommand("Nep").setCategory(commandCategories.Fun).setRun(new Nep()).build());
            commands.add(new commandBuilder().setCommand("Say").setCategory(commandCategories.Audio).setRun(new Say(new File(variablesStorage.getMediaFolder() + File.separator + "say"))).build());
            commands.add(new commandBuilder().setCommand("Translate").setCategory(commandCategories.Fun).setRun(new Translate()).setDescription("Translate anything into Nepenese.").build());
            commands.add(new commandBuilder().setCommand("Options").setCategory(commandCategories.Admin).setRequiredPermissions(new Permission[]{Permission.MANAGE_SERVER}).setRun(new AdminOptions()).build());
            commands.add(new commandBuilder().setCommand("About").setCategory(commandCategories.General).setRun(new About()).build());
            commands.add(new commandBuilder().setCommand("Leavee").setCategory(commandCategories.Audio).setRun(new Leave()).setDescription("Disconnect from the current voice channel").build());
            commands.add(new commandBuilder().setCommand("UwU").setCategory(commandCategories.Fun).setRun(new UWU_Translater()).build());
            commands.add(new commandBuilder().setCommand("Uptime").setCategory(commandCategories.General).setRun(new Uptime()).build());
            commands.add(new commandBuilder().setCommand("Flip").setCategory(commandCategories.Fun).setRun(new CoinFlip()).setDescription("Heads or Tails?").build());
            commands.add(new commandBuilder().setCommand("Roll").setCategory(commandCategories.Fun).setRun(new RollDie()).setDescription("Roll a Die of any size").build());
            commands.add(new commandBuilder().setCommand("GSK").setName("Great Sleep King").setCategory(commandCategories.Fun).setRun(new GreatSleepKing()).setDescription("How much sleep will the Sleep King grant you tonight").build());
            commands.add(new commandBuilder().setCommand("F").setCategory(commandCategories.Fun).setRun(new PayRespect()).setDescription("Quickly and easily pay respect").build());
            commands.add(new commandBuilder().setCommand("Attack").setCategory(commandCategories.Fun).setRun(new Attack()).setDescription("Attack other server Members").build());
            commands.add(new commandBuilder().setCommand("Ping").setCategory(commandCategories.Fun).setRun(new Ping()).setDescription("Pong").build());
            commands.add(new commandBuilder().setCommand("MC").setCategory(commandCategories.General).setRun(new MinecraftServerStatus()).setDescription("Check the state of a Minecraft server").build());
            commands.add(new commandBuilder().setCommand("ServerInfo").setCategory(commandCategories.General).setRun(new GuildInfo()).setDescription("Some info about the current server").build());
            commands.add(new commandBuilder().setCommand("Help").setCategory(commandCategories.General).setRun(new Help()).setDescription("Display Help Page").build());
            commands.add(new commandBuilder().setCommand("Log").setCategory(commandCategories.Admin).setRequiredPermissions(new Permission[]{Permission.MANAGE_SERVER}).setRun(new Logging()).setDescription("Manage server logging").build());
            commands.add(new commandBuilder().setCommand("Pat").setCategory(commandCategories.Image).setRun(new Pat()).build());
            commands.add(new commandBuilder().setCommand("Hug").setCategory(commandCategories.Image).setRun(new Hug()).build());
            commands.add(new commandBuilder().setCommand("Poke").setCategory(commandCategories.Image).setRun(new Poke()).build());
            commands.add(new commandBuilder().setCommand("Cuddle").setCategory(commandCategories.Image).setRun(new Cuddle()).build());
            commands.add(new commandBuilder().setCommand("Nom").setCategory(commandCategories.Image).setRun(new Nom()).build());
            commands.add(new commandBuilder().setCommand("Confused").setCategory(commandCategories.Image).setRun(new Confused()).build());
            commands.add(new commandBuilder().setCommand("D10K").setCategory(commandCategories.Fun).setDescription("10,000 Magical Effects").setRun(new D10K()).build());
            commands.add(new commandBuilder().setCommand("Powerlevel").setCategory(commandCategories.Fun).setDescription("What does the scouter say about their power level?").setRun(new PowerLevel()).build());
            commands.add(new commandBuilder().setCommand("Pout").setCategory(commandCategories.Image).setRun(new Pout()).build());
            commands.add(new commandBuilder().setCommand("Senko").setCategory(commandCategories.Image).setRun(new Senko()).build());
            commands.add(new commandBuilder().setCommand("Stare").setCategory(commandCategories.Image).setRun(new Stare()).build());
            commands.add(new commandBuilder().setCommand("Neko").setCategory(commandCategories.Image).setRun(new Neko()).build());
            commands.add(new commandBuilder().setCommand("Shocked").setCategory(commandCategories.Image).setRun(new Shocked()).build());
            commands.add(new commandBuilder().setCommand("Nya").setCategory(commandCategories.Audio).setRun(new Nya()).build());
            commands.add(new commandBuilder().setCommand("Sleepy").setCategory(commandCategories.Image).setRun(new Sleepy()).build());
            commands.add(new commandBuilder().setCommand("Breached").setName("Why the F*** was i breached").setCategory(commandCategories.Fun).setRun(new WhyWasIBreached()).build());
            commands.add(new commandBuilder().setCommand("IsCaliforniaOnFire").setCategory(commandCategories.Fun).setRun(new IsCaliforniaOnFire()).build());
            commands.add(new commandBuilder().setCommand("CustomRole").setCategory(commandCategories.Utility).setRun(new CustomRole()).build());
            commands.add(new commandBuilder().setCommand("Leaderboards").setCategory(commandCategories.Utility).setRun(new Leaderboard()).build());
            commands.add(new commandBuilder().setCommand("8Ball").setName("Magic 8 Ball").setCategory(commandCategories.Fun).setRun(new Magic8Ball()).build());
            commands.add(new commandBuilder().setCommand("Awoo").setCategory(commandCategories.Audio).setRun(new Awoo()).build());
            commands.add(new commandBuilder().setCommand("Wan").setCategory(commandCategories.Audio).setRun(new Wan()).build());
            commands.add(new commandBuilder().setCommand("jpeg").setCategory(commandCategories.Image).setRun(new moreJpeg()).build());
            commands.add(new commandBuilder().setCommand("UnixTime").setCategory(commandCategories.General).setRun(new unixTime()).build());
            commands.add(new commandBuilder().setCommand("Ocr").setCategory(commandCategories.Image).setRun(new ocr()).build());
            commands.add(new commandBuilder().setCommand("Profile").setCategory(commandCategories.Utility).setRun(new profile()).build());
            commands.add(new commandBuilder().setCommand("a").setCategory(commandCategories.Audio).setRun(new a()).build());
            commands.add(new commandBuilder().setCommand("Bonk").setCategory(commandCategories.Audio).setRun(new bonk()).build());
            commands.add(new commandBuilder().setCommand("BonkImage").setCategory(commandCategories.Image).setRun(new bonkImage()).build());
        }
        catch (MissingArgumentException e){
            log.error(e);
        }
    }

    public ArrayList<Command> getCommandList() {
        return commands;
    }

    public boolean run(GuildMessageReceivedEvent event, guildObject guildEntity) {
        String[] CommandArray =
                getCommandName(event.getMessage().getContentRaw().replaceFirst("!nep", ""));
        Command command = null;

        /*
            Check for Command
            This allows me to check for the command regardless of case
            commands.get(key) is case sensitive

        */

        for (Command set : commands) {
            if (CommandArray[0].equalsIgnoreCase(set.getCommand())) {
                command = set;
                break;
            }
        }
        if (command != null) {
            if (command.getRequiredPermissions() != null) {
                if (!event.getMember().hasPermission(command.getRequiredPermissions())){
                    permissionException(event);
                    return false;
                }
            }
            log.info("NEPTUNE: Running Command: " + command.getName());
            commandExecutor exec =
                    new commandExecutor(command, event, CommandArray[1]);
            executor.execute(exec);
            return true;
        }
        return false;
    }

    private void permissionException(GuildMessageReceivedEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.RED);
        embedBuilder.setDescription("You Lack the Required permission to do that!");
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }

    /*
        Due to some commands taking more longer to run, commands are now run in their own thread
    */
    public class commandExecutor implements Runnable {
        GuildMessageReceivedEvent event;
        String messagecontent;
        Command command;
        protected final Logger log = LogManager.getLogger();

        public commandExecutor(
                Command command,
                GuildMessageReceivedEvent event,
                String messagecontent) {
            this.event = event;
            this.messagecontent = messagecontent;
            this.command = command;
        }

        @Override
        public void run() {
            command.run(event, messagecontent);
        }
    }
}
