package neptune.commands.audio;

import io.sentry.Sentry;
import neptune.commands.ICommand;
import neptune.commands.ISlashCommand;
import neptune.music.AudioController;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.io.File;
import java.util.*;

public class Say implements ICommand, ISlashCommand {
    final File folder = new File("Media" + File.separator + "say");
    private AudioController AudioOut;
    private File[] listOfFiles;
    private final HashMap<String, Long> rateLimitMap = new HashMap<>();


    private Message saySingleMatch(File quote, GuildMessageReceivedEvent event, MessageBuilder builder) {

        if (event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            try {
                System.out.println("        Deleting Command Message");
                event.getMessage().delete().queue();
            } catch (IllegalStateException | InsufficientPermissionException ignored) {}
        }

        // play the sound file
        if (quote.exists()) {
            AudioOut.playSound(event, quote.getAbsolutePath());
        }
        builder.setContent(quote.getName().replace(".wav", ""));
        return builder.build();
    }
    private Message saySingleMatch(File quote, SlashCommandEvent event, MessageBuilder builder) {

        // play the sound file
        if (quote.exists()) {
            AudioOut.playSound(event, quote.getAbsolutePath());
        }
        builder.setContent(quote.getName().replace(".wav", ""));
        return builder.build();
    }
    private Queue<File> searchQuotes(String quote) {
        // store results
        Queue<File> VoiceLineTextQueue = new LinkedList<>();
        int i = 0;
        quote = quote.trim().toLowerCase();
        if (!quote.equals("")) {
            while (listOfFiles.length > i) {
                String file =
                        listOfFiles[i]
                                .getName()
                                .replace(".wav", "")
                                .trim()
                                .toLowerCase(); // moved to variable to reduce string calls

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
        } else //noinspection ConstantConditions
        if (VoiceLineTextQueue.isEmpty()) {
            VoiceLineTextQueue.addAll(Arrays.asList(listOfFiles));
        }
        return VoiceLineTextQueue;
    }

    private List<StringBuilder> prepareLargeMessage(Queue<File> files) {
        ArrayList<StringBuilder> largeMessageGroup = new ArrayList<>();
        int page = 0;
        while (!files.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            largeMessageGroup.add(stringBuilder);
            stringBuilder.append("```Found Quotes. Page ").append(page + 1).append("\n");
            while (!files.isEmpty() && stringBuilder.length() < 2000) {
                if (files.peek().getName().replace(".wav", "").length() + stringBuilder.length()
                        < 2000) {
                    stringBuilder.append(Objects.requireNonNull(files.poll()).getName().replace(".wav", "")).append("\n");
                } else {
                    break; // string will be too large if anything else is added
                }
            }
            stringBuilder.append("```");
            page++;
        }
        return largeMessageGroup;
    }

    private Message sendLargeMessage(List<StringBuilder> Message, User user) {
        for (StringBuilder stringBuilder : Message) {
            user.openPrivateChannel()
                    .queue(
                            privateChannel ->
                                    privateChannel.sendMessage(stringBuilder.toString()).queue());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Sentry.captureException(e);
            }
        }
        return null;
    }

    private boolean isRateLimited(User user) {
        // Checks if the user sent a command too quickly
        if (rateLimitMap.containsKey(user.getId())) {
            if (System.currentTimeMillis() - rateLimitMap.get(user.getId()) < 5000) {
                rateLimitMap.replace(user.getId(), System.currentTimeMillis());
                return true;
            } else {
                rateLimitMap.replace(user.getId(), System.currentTimeMillis());
                return false;
            }
        } else {
            rateLimitMap.put(user.getId(), System.currentTimeMillis());
        }
        return false;
    }

    @Override
    public Message run(GuildMessageReceivedEvent event, String messageContent, MessageBuilder builder) {
        // rate limiting
        if (isRateLimited(Objects.requireNonNull(event.getMember()).getUser())){
            return builder.setContent("You are being rate limited. Please wait a few seconds.").build();
        }
        // open audio channel
        if (AudioOut == null) {
            AudioOut = new AudioController(event.getGuild());
        }

        Queue<File> results = searchQuotes(messageContent);
        if (results.size() == 1) {
            return saySingleMatch(results.iterator().next(), event, builder);
        } else if (results.size() > 1) {
            List<StringBuilder> preparedMessages = prepareLargeMessage(results);
            Runnable runnable = () -> sendLargeMessage(preparedMessages, event.getAuthor());
            Thread thread = new Thread(runnable);
            thread.start();
            return builder.setContent("Please check your DMs.").build();
        }
        return builder.setContent("No quotes found, Please try a different search.").build();
    }
    public Say(){
        listOfFiles = folder.listFiles();
    }

    @Override
    public CommandData RegisterCommand(CommandData commandData) {
        return commandData.addOption(OptionType.STRING, "Quote", "Search for a specific quote.",false);
    }

    @Override
    public Message run(SlashCommandEvent event, MessageBuilder builder) {
        if (isRateLimited(Objects.requireNonNull(event.getMember()).getUser())){
            return builder.setContent("You are being rate limited. Please wait a few seconds.").build();
        }
        // open audio channel
        if (AudioOut == null) {
            AudioOut = new AudioController(event.getGuild());
        }
        OptionMapping optionMapping = event.getOption("Quote");
        String messageContent;
        if (optionMapping != null){
            messageContent = optionMapping.getAsString();
        }
        else messageContent = "";
        Queue<File> results = searchQuotes(messageContent);
        if (results.size() == 1) {
            return saySingleMatch(results.iterator().next(), event, builder);
        } else if (results.size() > 1) {
            List<StringBuilder> preparedMessages = prepareLargeMessage(results);
            Runnable runnable = () -> sendLargeMessage(preparedMessages, event.getUser());
            Thread thread = new Thread(runnable);
            thread.start();
            return builder.setContent("Please check your DMs.").build();
        }
        return builder.setContent("No quotes found, Please try a different search.").build();    }
}
