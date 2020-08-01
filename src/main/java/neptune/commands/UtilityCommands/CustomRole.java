package neptune.commands.UtilityCommands;

import neptune.commands.CommandInterface;
import neptune.commands.CommonMethods;
import neptune.commands.commandCategories;
import neptune.storage.MySQL.SettingsStorage;
import neptune.storage.VariablesStorage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

import java.awt.*;
import java.util.Map;

public class CustomRole extends CommonMethods implements CommandInterface{
    CustomRoleStorage customRoleStorage = new CustomRoleStorage();
    SettingsStorage settingsStorage = new SettingsStorage();

    @Override
    public String getName() {
        return "Custom Role";
    }

    @Override
    public String getCommand() {
        return "customRole";
    }

    @Override
    public String getDescription() {
        return "Allows you to set a personal role and color. This role does not grant any permissions and is disabled by default";
    }

    @Override
    public commandCategories getCategory() {
        return commandCategories.Utility;
    }

    @Override
    public String getHelp() {
        return "!nep customRole for more info";
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
        String[] command = getCommandName(messageContent);
        Map<String, String> options = settingsStorage.getGuildSettings(event.getGuild().getId());

        if (options.getOrDefault("CustomRoleEnabled","disabled").equals("enabled")) {
            switch (command[0]){
                case "create":
                case "editname": {
                    editRoleName(event,command[1]);
                    break;
                }
                case "editcolor":{
                    editRoleColor(event,command[1]);
                    break;
                }
                case "remove":{
                    removeRole(event);
                    break;
                }
                default:{
                    menu(event);
                    break;
                }
            }
        }
        else{
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(Color.RED).setTitle(getName());
            embedBuilder.setDescription("Custom roles is not enabled, Please have an admin enable it in options.");
            event.getChannel().sendMessage(embedBuilder.build()).queue();
        }
        return false;
    }
    private void menu(MessageReceivedEvent event){
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.MAGENTA);
        embedBuilder.setTitle(getName());
        embedBuilder.addField("Create Role", "!nep customRole create (Role Name)",false);
        embedBuilder.addField("Remove Role", "!nep customRole remove",false);
        embedBuilder.addField("Change Role Name", "!nep customRole editName (Role Name)",false);
        embedBuilder.addField("Change Role Color", "!nep customRole editColor (Hex Color Code)",false);
        event.getChannel().sendMessage(embedBuilder.build()).queue();

    }
    private void editRoleName(MessageReceivedEvent event, String RoleName){
        if (RoleName.equalsIgnoreCase("")){
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(getName())
                        .setColor(Color.RED)
                        .setDescription("Please include a name.");
            event.getChannel().sendMessage(embedBuilder.build()).queue();
            return;
        }
        if (getRole(event) == null){
            createRole(event,RoleName);
        }
        Role userRole = event.getGuild().getRoleById(getRole(event));
        if (userRole == null){
            createRole(event,RoleName);
        }
        else{
            try {
                userRole.getManager().setName(RoleName).queue();
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setColor(Color.MAGENTA).setTitle(getName()).setDescription("Role Updated Successfully");
                event.getChannel().sendMessage(embedBuilder.build()).queue();
            }
            catch (InsufficientPermissionException e){
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle(getName());
                embedBuilder.setColor(Color.RED);
                embedBuilder.setDescription("Neptune lacks the required permission to manage roles.");
                event.getChannel().sendMessage(embedBuilder.build()).queue();
            }
        }
    }
    private void editRoleColor(MessageReceivedEvent event, String RoleColor){
        if (RoleColor.equalsIgnoreCase("")){
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(getName())
                    .setColor(Color.RED)
                    .setDescription("Please include a Color.");
            event.getChannel().sendMessage(embedBuilder.build()).queue();
            return;
        }
        if (getRole(event) == null){
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(getName())
                    .setColor(Color.RED)
                    .setDescription("Please create a role before trying to change the color.");
            event.getChannel().sendMessage(embedBuilder.build()).queue();
            return;
        }
        Role userRole = event.getGuild().getRoleById(getRole(event));
        if (userRole == null){
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(getName())
                    .setColor(Color.RED)
                    .setDescription("Please create a role before trying to change the color.");
            event.getChannel().sendMessage(embedBuilder.build()).queue();
            return;
        }
        else{
            try {
                userRole.getManager().setColor(Integer.decode(RoleColor)).queue();
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setColor(Color.MAGENTA).setTitle(getName()).setDescription("Role Updated Successfully");
                event.getChannel().sendMessage(embedBuilder.build()).queue();
            } catch (NumberFormatException e) {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle(getName());
                embedBuilder.setColor(Color.RED);
                embedBuilder.setDescription("That is not a valid discord color");
                event.getChannel().sendMessage(embedBuilder.build()).queue();            }
            catch (InsufficientPermissionException e){
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle(getName());
                embedBuilder.setColor(Color.RED);
                embedBuilder.setDescription("Neptune lacks the required permission to manage roles.");
                event.getChannel().sendMessage(embedBuilder.build()).queue();
            }
        }
    }
    private void createRole(MessageReceivedEvent event, String RoleName){
        try {
            Role role = event.getGuild().createRole().setName(RoleName).setPermissions().complete();
            customRoleStorage.addRole(event.getMember().getId(),event.getGuild().getId(),role.getId());
            event.getGuild().addRoleToMember(event.getMember(),role).complete();
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(Color.MAGENTA).setTitle(getName()).setDescription("Role Created Successfully");
            event.getChannel().sendMessage(embedBuilder.build()).queue();
        }
        catch (InsufficientPermissionException e){
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(getName());
            embedBuilder.setColor(Color.RED);
            embedBuilder.setDescription("Neptune lacks the required permission to manage roles.");
            event.getChannel().sendMessage(embedBuilder.build()).queue();
        }
    }
    private void removeRole(MessageReceivedEvent event){
        try {
            String roleID = getRole(event);
            event.getGuild().getRoleById(roleID).delete().reason("Custom Role Remove").complete();
            customRoleStorage.removeRole(roleID);
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(Color.MAGENTA).setTitle(getName()).setDescription("Role Deleted Successfully");
            event.getChannel().sendMessage(embedBuilder.build()).queue();
        }
        catch (InsufficientPermissionException e){
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(getName());
            embedBuilder.setColor(Color.RED);
            embedBuilder.setDescription("Neptune lacks the required permission to manage roles.");
            event.getChannel().sendMessage(embedBuilder.build()).queue();
        }
    }
    private String getRole(MessageReceivedEvent event){
        return customRoleStorage.getRoleID(event.getGuild().getId(),event.getMember().getId());
    }
}
