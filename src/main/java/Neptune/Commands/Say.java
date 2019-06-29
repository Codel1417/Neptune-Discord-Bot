package Neptune.Commands;

import Neptune.Storage.StorageControllerCached;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;

import java.io.File;
import java.util.*;

public class Say {
    private AudioController AudioOut;
    private File[] listOfFiles;
    //I Need to redo this mess
    Say(File folder){
        listOfFiles = folder.listFiles();
        System.out.println("Say Files: " + Objects.requireNonNull(listOfFiles).length);
    }

    void sayQuoteFileList(String Quote, MessageReceivedEvent event, StorageControllerCached storageController) {
        //open audio channel
        if (event.getGuild() != null && AudioOut == null) {
            AudioOut = new AudioController(event);
        }
        Queue<File> results = searchQuotes(Quote);
        if (results.size() == 1) {
            saySingleMatch(results.iterator().next(), event, storageController);
        }
        else if (results.size() > 1 ) {
            List<StringBuilder> preparedMessages = prepareLargeMessage(results);
            Runnable runnable = () ->
                    sendLargeMessage(preparedMessages, event.getAuthor());
            Thread thread = new Thread(runnable);
            thread.start();
        }

    }
    private void saySingleMatch(File quote, MessageReceivedEvent event, StorageControllerCached storageController) {
        storageController.incrementAnalyticForCommand("Say", quote.getName().replace("."," ").replace(" wav",""));

        if (event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            try {
                event.getMessage().delete().queue();
            } catch (IllegalStateException | InsufficientPermissionException ignored) {
            }
        }

        //play the sound file
        if (quote.exists() && event.getGuild() != null) {
            if (event.getGuild().getAudioManager() != null || event.getMember().getVoiceState().getChannel() != null) {
                AudioOut.playSound(event, quote.getAbsolutePath(), false);
            }
        }
        if (event.getMember().hasPermission(Permission.MESSAGE_WRITE)) {
            MessageBuilder builder = new MessageBuilder();
            if (event.getGuild().getAudioManager() == null || event.getMember().getVoiceState().getChannel() == null) {
                if (event.getMember().hasPermission(Permission.MESSAGE_TTS) && event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_TTS)) {
                    builder.setTTS(storageController.getTtsEnabled(event.getGuild()));
                }
            }
            else builder.setTTS(false);
            builder.append(quote.getName().replace(".wav", ""));
            builder.sendTo(event.getChannel()).queue();
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
        else if (VoiceLineTextQueue.isEmpty()) {
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
}
