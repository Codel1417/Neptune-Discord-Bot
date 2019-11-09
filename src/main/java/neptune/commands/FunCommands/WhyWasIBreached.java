package neptune.commands.FunCommands;

import neptune.commands.CommandInterface;
import neptune.commands.commandCategories;
import neptune.storage.VariablesStorage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Random;

public class WhyWasIBreached implements CommandInterface {
    @Override
    public String getName() {
        return "Why the F*** Was I Breached";
    }

    @Override
    public String getCommand() {
        return "breached";
    }

    @Override
    public String getDescription() {
        return "Did you just lose 100m customer SSNs because your root password was \"password\", you set an S3 bucket to public or you didn't patch a well known vulnerability for 8 months? Is the media and government chewing you out because of it? Worry not! Our free excuse generator will help you develop an air-tight breach statement in no time!" +
                "\n Inspired by https://whythefuckwasibreached.com/";
    }

    @Override
    public commandCategories getCategory() {
        return commandCategories.Fun;
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
    public boolean getRequireOwner() {
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
    public boolean run(MessageReceivedEvent event, VariablesStorage variablesStorage, String messageContent) {
        String[] threatActors = {"Russians", "NSA", "FBI", "North Koreans", "Chinese", "Anonymous collective", "teenage hacking prodigies", "Iranians", "KGB", "industrial spies", "competition", "Europeans", "Americans", "cyber terrorists", "advanced persistent threats", "state actors", "rogue AIs", "APTs", "Fancy Bears", "foreign assets", "master hackers", "technology whiz kids", "script kiddies", "hacking activists", "hacking people", "security community", "internet crowd"};
        String[] methods ={"0-day exploits", "unprecedented XSS vulnerabilities", "infiltrators", "overwhelming force", "botnets", "ransomware", "DDoS attacks", "IoT malware", "advanced techniques", "hacking drones", "cyborg bees", "digital nukes", "the open door in our basement", "that one vulnerability we were going to patch next Tuesday", "that other vulnerability we were going to patch next tuesday", "something something vulnerability", "vectors we really couldn't have prevented", "vulnerabilities in a 3rd party solution", "weaknesses in our vendors", "nefarious techniques", "an issue in Wordpress 1.0", "Heartbleed", "a vulnerability in Windows XP SP1", "pen and paper based social engineering", "an open window in the server room", "30 - 50 feral hogs"};
        String[] targets = {"gain access to some data", "cause a minor disturbance", "potentially access some customer data", "cause an undetermined amount of damage", "partially disrupt our services", "breach our high security servers", "glimpse into our database", "transfer 7 petabytes of data", "extract some private keys", "do something, but we aren't quite sure what it is", "make a mess", "make us look bad", "force us to release this report", "hack the coffee maker", "install a C99"};
        String[] mitigations = {"made everyone promise to be super super careful", "gotten ISO certified", "gotten PCI certified", "worked with industry leading specialists", "upskilled our cafeteria staff", "hired external consultants", "worked with law enforcement", "bought an IDS", "twiddled with our firewall", "been pretty good about security", "hired some people with 'CISSP' after their names", "watched a YouTube video on cyber security", "told them to not do it again", "said that we are very sorry", "copy-pasted a security policy we found on Google", "hired a Russian dude", "watched the movie Hackers 8 times back to back", "sent one of our guys to Defcon", "put a rotating lock GIF on our website"};
        Random random = new Random();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("The f***ing ").append(threatActors[random.nextInt(threatActors.length)]).append(" used ").append(methods[random.nextInt(methods.length)])
                .append(" to ").append(targets[random.nextInt(targets.length)]).append(" But we have since ").append(mitigations[random.nextInt(mitigations.length)])
                .append(" , so it will never happen again.");
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(getName());
        embedBuilder.setDescription(stringBuilder);
        event.getChannel().sendMessage(embedBuilder.build()).queue();
        return true;
    }
}
