package neptune.commands.audio;

import neptune.commands.CommandInterface;
import neptune.commands.commandCategories;
import neptune.storage.guildObject;
import neptune.music.AudioController;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

import java.io.File;
import java.util.*;

public class Say implements CommandInterface {
    File folder;
    private AudioController AudioOut;
    private File[] listOfFiles;
    private HashMap<String, Long> rateLimitMap = new HashMap<>();
    public Say(File folder){
        this.folder = folder;
    }
    @Override
    public String getName() {
        return "Say";
    }

    @Override
    public String getCommand() {
        return "say";
    }

    @Override
    public String getDescription() {
        return "Neptune speaks! She can join the current voice channel and say one of her quotes from the games";
    }

    @Override
    public commandCategories getCategory() {
        return commandCategories.Audio;
    }

    @Override
    public String getHelp() {
        return getCommand() + " <Quote> | Leave blank for a list of quotes";
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
    public guildObject run(GuildMessageReceivedEvent event, String messageContent, guildObject guildEntity) {
        //rate limiting
        if (isRateLimited(event.getMember().getUser())) return guildEntity;

        //open audio channel
        if (event.getGuild() != null && AudioOut == null) {
            AudioOut = new AudioController(event);
        }
        listOfFiles = folder.listFiles();
        System.out.println("Say Files: " + listOfFiles.length);
        Queue<File> results = searchQuotes(messageContent);
        if (results.size() == 1) {
            saySingleMatch(results.iterator().next(), event);
        }
        else if (results.size() > 1 ) {
            List<StringBuilder> preparedMessages = prepareLargeMessage(results);
            Runnable runnable = () ->
                    sendLargeMessage(preparedMessages, event.getAuthor());
            Thread thread = new Thread(runnable);
            thread.start();
        }
        return guildEntity;
    }

    private void saySingleMatch(File quote, GuildMessageReceivedEvent event) {
        //storageController.incrementAnalyticForCommand("Say", quote.getName().replace("."," ").replace(" wav",""));

        if (event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            try {
                System.out.println("        Deleting Command Message");
                event.getMessage().delete().queue();
            } catch (IllegalStateException | InsufficientPermissionException ignored) {
            }
        }

        //play the sound file
        if (quote.exists() && event.getGuild() != null) {
            if (event.getGuild().getAudioManager() != null || event.getMember().getVoiceState().getChannel() != null) {
                AudioOut.playSound(event, quote.getAbsolutePath());
            }
        }

        if (event.getMember().hasPermission(Permission.MESSAGE_WRITE)) {
            MessageBuilder builder = new MessageBuilder();
            builder.append(quote.getName().replace(".wav", ""));
            event.getChannel().sendMessage(builder.build()).queue();

        }
    }

    private Queue<File> searchQuotes(String quote){
        //store results
        Queue<File> VoiceLineTextQueue = new LinkedList<>();
        int i = 0;
        quote = quote.trim().toLowerCase();
        if (!quote.equals("")) {
            while (listOfFiles.length > i) {
                String file = listOfFiles[i].getName().replace(".wav", "").trim().toLowerCase(); //moved to variable to reduce string calls

                if (file.equalsIgnoreCase(quote)) {
                    VoiceLineTextQueue.clear();
                    VoiceLineTextQueue.offer(listOfFiles[i]);
                    break;
                }
                if (file.contains(quote)) {
                    VoiceLineTextQueue.offer(listOfFiles[i]);
                }
                i++;
            }
        }
        else //noinspection ConstantConditions
            if (VoiceLineTextQueue.isEmpty()) {
            VoiceLineTextQueue.addAll(Arrays.asList(listOfFiles));
        }
        return VoiceLineTextQueue;
    }
    private List<StringBuilder> prepareLargeMessage(Queue<File> files){
        ArrayList<StringBuilder> largeMessageGroup = new ArrayList<>();
        int page = 0;
        while (!files.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            largeMessageGroup.add(stringBuilder);
            stringBuilder.append("```Found Quotes. Page ").append(page + 1).append("\n");
            while (!files.isEmpty() && stringBuilder.length() < 2000) {
                if (files.peek().getName().replace(".wav","").length() + stringBuilder.length() < 2000) {
                    stringBuilder.append(files.poll().getName().replace(".wav","")).append("\n");
                }
                else {
                    break; //string will be too large if anything else is added
                }
            }
            stringBuilder.append("```");
            page++;
        }
        return largeMessageGroup;
    }

    private void sendLargeMessage(List<StringBuilder> Message, User user) {
        for (StringBuilder stringBuilder : Message) {
            user.openPrivateChannel().queue(privateChannel ->
                    privateChannel.sendMessage(stringBuilder.toString()).queue());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isRateLimited(User user) {
        //Checks if the user sent a command too quickly
            if(rateLimitMap.containsKey(user.getId())) {
                if (System.currentTimeMillis() - rateLimitMap.get(user.getId()) < 5000) {
                    rateLimitMap.replace(user.getId(), System.currentTimeMillis());
                    return true;
                } else {
                    rateLimitMap.replace(user.getId(), System.currentTimeMillis());
                    return false;
                }
            }
            else {
                rateLimitMap.put(user.getId(), System.currentTimeMillis());
            }
        return false;
    }
}
