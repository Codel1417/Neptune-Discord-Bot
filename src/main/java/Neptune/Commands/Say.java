package Neptune.Commands;

import Neptune.Storage.StorageControllerCached;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;

import java.io.File;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class Say {
    private AudioController AudioOut;
    private File folder;
    private File[] listOfFiles;
    //I Need to redo this mess
    Say(File folder){
        this.folder = folder;
        listOfFiles = this.folder.listFiles();
        System.out.println("Say Files: " + Objects.requireNonNull(listOfFiles).length);
    }
    void sayQuoteFileList(String Quote, MessageReceivedEvent event, StorageControllerCached storageController) {
        //open audio channel
        if (event.getGuild() != null && AudioOut == null) {
            AudioOut = new AudioController(event);
        }
        //store results
        Queue<File> VoiceLineTextQueue = new LinkedList<>();
        int i = 0;
        if (!Quote.trim().equals("")) {
            while (listOfFiles.length > i) {
                if (listOfFiles[i].toString().substring((folder.toString() + "\"").length(), listOfFiles[i].toString().indexOf(".wav")).equalsIgnoreCase(Quote.toLowerCase().trim())) {
                    VoiceLineTextQueue.clear();
                    VoiceLineTextQueue.offer(listOfFiles[i]);
                    break;
                }
                if (listOfFiles[i].toString().substring((folder.toString()  + "\"").length(), listOfFiles[i].toString().indexOf(".wav")).toLowerCase().contains(Quote.toLowerCase().trim())) {
                    VoiceLineTextQueue.offer(listOfFiles[i]);
                }
                i++;
            }
        }
        else if (VoiceLineTextQueue.isEmpty()) {
            while (listOfFiles.length > i) {
                VoiceLineTextQueue.offer(listOfFiles[i]);
                i++;
            }
        }
        if (VoiceLineTextQueue.size() == 1 ) {
            storageController.incrementAnalyticForCommand("Say", VoiceLineTextQueue.peek().getName().replace("."," ").replace(" wav",""));
            try {
                event.getMessage().delete().queue();
            } catch (IllegalStateException Ignored) {
                //cant delete private messages, or when the permissions don't allow it. Do nothing instead
            } catch (InsufficientPermissionException e) {
                System.out.println("        Permissions: Unable to Delete Messages");
            }

            //play the sound file
            if (VoiceLineTextQueue.size() == 1 && event.getGuild() != null && VoiceLineTextQueue.peek() != null) {
                if (event.getGuild().getAudioManager() != null || event.getMember().getVoiceState().getChannel() != null) {
                    if (VoiceLineTextQueue.peek() != null) {
                        AudioOut.playSound(event, VoiceLineTextQueue.peek().getAbsolutePath(), false);
                    }
                }
            }
            if (VoiceLineTextQueue.peek() != null && VoiceLineTextQueue.size() == 1) {
                    MessageBuilder builder = new MessageBuilder();
                    if (event.getGuild().getAudioManager() == null || event.getMember().getVoiceState().getChannel() == null) {
                        if (event.getMember().hasPermission(Permission.MESSAGE_TTS) && event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_TTS)) {
                            builder.setTTS(storageController.getTtsEnabled(event.getGuild()));
                        }
                    }
                    else builder.setTTS(false);
                    builder.append(VoiceLineTextQueue.peek().toString().substring((folder.toString().trim() + "\"").length(), VoiceLineTextQueue.poll().toString().indexOf(".wav")).trim());
                    builder.sendTo(event.getChannel()).queue();
            }
        }
        else  {
            StringBuilder TempReturnLine = new StringBuilder("```Found Voice Lines: \n");
            while (!VoiceLineTextQueue.isEmpty()) {
                if (TempReturnLine.length() <= 500) {
                    TempReturnLine.append(VoiceLineTextQueue.peek().toString().substring((folder.toString().trim() + "\"").length(), VoiceLineTextQueue.poll().toString().indexOf(".wav"))).append("\n");
                }
                else {
                    String finalTempReturnLine = TempReturnLine.toString();
                    event.getAuthor().openPrivateChannel().queue((channel) ->
                            channel.sendMessage(finalTempReturnLine + "```").queue());
                    TempReturnLine = new StringBuilder("```");
                }
            }

            String finalTempReturnLine = TempReturnLine.toString();
            event.getAuthor().openPrivateChannel().queue((channel) ->
                    channel.sendMessage(finalTempReturnLine + "```").queue());
        }
    }
}
