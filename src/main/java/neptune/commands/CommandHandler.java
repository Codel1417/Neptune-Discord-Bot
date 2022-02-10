package neptune.commands;

import neptune.commands.AdminCommands.*;
import neptune.commands.FunCommands.*;
import neptune.commands.GeneralCommands.*;
import neptune.commands.ImageCommands.*;
import neptune.commands.ImageCommands.Tenor.*;
import neptune.commands.UtilityCommands.*;
import neptune.commands.audio.*;
import neptune.exceptions.MissingArgumentException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.sentry.Sentry;

import java.io.InvalidObjectException;

// handles neptune base commands
public class CommandHandler extends Helpers {
    protected static final Logger log = LogManager.getLogger();
    final CommandRegistry commandRegistry = new CommandRegistry("!nep");
    private boolean ready;
    public CommandHandler() {
        // Add all commands;
        try{
            commandRegistry.registerCommand(new commandBuilder().setCommand("Nep").setCategory(CategoriesEnum.Fun).setRun(new Nep()).setDescription("Can you out-nep Nep?").build());
            commandRegistry.registerCommand(new commandBuilder().setCommand("Say").setCategory(CategoriesEnum.Audio).setRun(new Say()).setDescription("AUDIO: Listen to quotes from neptune").build());
            commandRegistry.registerCommand(new commandBuilder().setCommand("Translate").setCategory(CategoriesEnum.Fun).setRun(new Translate()).setDescription("Translate anything into Nepenese.").build());
            commandRegistry.registerCommand(new commandBuilder().setCommand("Options").setCategory(CategoriesEnum.Admin).setRequiredPermissions(new Permission[]{Permission.MANAGE_SERVER}).setDescription("Neptune Admin Options").setRun(new Admin()).build());
            commandRegistry.registerCommand(new commandBuilder().setCommand("About").setCategory(CategoriesEnum.General).setRun(new About()).setDescription("About this bot").build());
            commandRegistry.registerCommand(new commandBuilder().setCommand("Leave").setCategory(CategoriesEnum.Audio).setRun(new Leave()).setDescription("Disconnect from the current voice channel").build());
            commandRegistry.registerCommand(new commandBuilder().setCommand("UwU").setCategory(CategoriesEnum.Fun).setRun(new UWU_Translater()).setDescription("Convert text into UwU").build());
            commandRegistry.registerCommand(new commandBuilder().setCommand("Flip").setCategory(CategoriesEnum.Fun).setRun(new CoinFlip()).setDescription("Heads or Tails?").build());
            commandRegistry.registerCommand(new commandBuilder().setCommand("Roll").setCategory(CategoriesEnum.Fun).setRun(new RollDie()).setDescription("Roll a Die of any size").build());
            commandRegistry.registerCommand(new commandBuilder().setCommand("GSK").setName("Great Sleep King").setCategory(CategoriesEnum.Fun).setRun(new GreatSleepKing()).setDescription("How much sleep will the Sleep King grant you tonight").build());
            commandRegistry.registerCommand(new commandBuilder().setCommand("F").setCategory(CategoriesEnum.Fun).setRun(new PayRespect()).setDescription("Quickly and easily pay respect").build());
            commandRegistry.registerCommand(new commandBuilder().setCommand("Attack").setCategory(CategoriesEnum.Fun).setRun(new Attack()).setDescription("Attack other server Members").build());
            commandRegistry.registerCommand(new commandBuilder().setCommand("MC").setCategory(CategoriesEnum.General).setRun(new MinecraftServerStatus()).setDescription("Check the state of a Minecraft server").build());
            commandRegistry.registerCommand(new commandBuilder().setCommand("ServerInfo").setCategory(CategoriesEnum.General).setRun(new GuildInfo()).setDescription("Some info about the current server").build());
            commandRegistry.registerCommand(new commandBuilder().setCommand("Log").setCategory(CategoriesEnum.Admin).setRequiredPermissions(new Permission[]{Permission.MANAGE_SERVER}).setRun(new Logging()).setDescription("Manage server logging").build());
            commandRegistry.registerCommand(new commandBuilder().setCommand("D10K").setCategory(CategoriesEnum.Fun).setDescription("10,000 Magical Effects").setRun(new D10K()).build());
            commandRegistry.registerCommand(new commandBuilder().setCommand("Powerlevel").setCategory(CategoriesEnum.Fun).setDescription("What does the scouter say about their power level?").setRun(new PowerLevel()).build());
            commandRegistry.registerCommand(new commandBuilder().setCommand("Senko").setCategory(CategoriesEnum.Image).setRun(new Senko()).setDescription("Mofu Mofu Forever ~").build());
            commandRegistry.registerCommand(new commandBuilder().setCommand("Nya").setCategory(CategoriesEnum.Audio).setRun(new Nya()).setDescription("AUDIO: Neptune goes nya ~ ").build());
            commandRegistry.registerCommand(new commandBuilder().setCommand("Breached").setDescription("Why the F*** was i breached").setCategory(CategoriesEnum.Fun).setRun(new WhyWasIBreached()).build());
            commandRegistry.registerCommand(new commandBuilder().setCommand("IsCaliforniaOnFire").setCategory(CategoriesEnum.Fun).setRun(new IsCaliforniaOnFire()).setDescription("Is California currently on fire?").build());
            commandRegistry.registerCommand(new commandBuilder().setCommand("Leaderboards").setCategory(CategoriesEnum.Utility).setRun(new Leaderboard()).setDescription("Guild Leaderboards.").build());
            commandRegistry.registerCommand(new commandBuilder().setCommand("8Ball").setName("Magic 8 Ball").setCategory(CategoriesEnum.Fun).setDescription("Let fate decide.").setRun(new Magic8Ball()).build());
            commandRegistry.registerCommand(new commandBuilder().setCommand("Awoo").setCategory(CategoriesEnum.Audio).setRun(new Awoo()).setDescription("AUDIO: Legalize Awoo!").build());
            commandRegistry.registerCommand(new commandBuilder().setCommand("Wan").setCategory(CategoriesEnum.Audio).setRun(new Wan()).setDescription("Wam Wam~").build());
            commandRegistry.registerCommand(new commandBuilder().setCommand("jpeg").setCategory(CategoriesEnum.Image).setRun(new moreJpeg()).setDescription("Do I look like I know what a JPEG is?").build());
            commandRegistry.registerCommand(new commandBuilder().setCommand("Ocr").setCategory(CategoriesEnum.Image).setRun(new ocr()).setDescription("Convert an image into text.").build());
            commandRegistry.registerCommand(new commandBuilder().setCommand("Profile").setCategory(CategoriesEnum.Utility).setRun(new profile()).setDescription("View and edit your profile").build());
            commandRegistry.registerCommand(new commandBuilder().setCommand("a").setCategory(CategoriesEnum.Audio).setRun(new a()).setDescription("AUDIO: A").build());
            commandRegistry.registerCommand(new commandBuilder().setCommand("Bonk").setCategory(CategoriesEnum.Audio).setRun(new bonk()).setDescription("\"Bonk!\"~ Scout, TF2").build());
            commandRegistry.registerCommand(new commandBuilder().setCommand("metaverse").setCategory(CategoriesEnum.Fun).setRun(new metaverse()).setDescription("The 7 rules of metaverses").build());
            //i was supposed to finish this command and got sidetracked.
            //commandRegistry.registerCommand(new commandBuilder().setCommand("BonkImage").setCategory(commandCategories.Image).setRun(new bonkImage()).build());
            ready = true;
        }
        catch (MissingArgumentException | InvalidObjectException e){
            log.error(e);
            Sentry.captureException(e);
            ready = false;
        }
    }

    public void run(GuildMessageReceivedEvent event) {
        commandRegistry.runCommand(event);
    }
    public void run(SlashCommandEvent event) {
        commandRegistry.runCommand(event);
    }
    public void RegisterSlashCommands(JDA jda){
        commandRegistry.RegisterSlashCommands(jda);
    }
    public boolean isReady(){
        return ready;
    }
}
