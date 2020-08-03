package neptune.commands.UtilityCommands;

import neptune.commands.CommandInterface;
import neptune.commands.CommonMethods;
import neptune.commands.commandCategories;
import neptune.storage.MySQL.SettingsStorage;
import neptune.storage.guildObject.customRoleObject;
import neptune.storage.guildObject.guildOptionsObject;
import neptune.storage.GuildStorageHandler;
import neptune.storage.VariablesStorage;
import neptune.storage.guildObject;
import neptune.storage.options;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

import java.awt.*;
import java.io.IOException;
import java.util.Map;

public class CustomRole extends CommonMethods implements CommandInterface {

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
        GuildStorageHandler guildStorageHandler = new GuildStorageHandler();
        guildObject guildEntity;
        try {
            guildEntity = guildStorageHandler.readFile(event.getGuild().getId());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        guildOptionsObject guildOptionsEntity = guildEntity.getGuildOptions();
        if (guildOptionsEntity.getOption(options.CustomRoleEnabled)) {
            switch (command[0]){
                case "create":
                    //TODO: add role
                case "editname": {
                    editRoleName(event,command[1],guildEntity);
                    break;
                }
                case "editcolor":{
                    editRoleColor(event,command[1],guildEntity);
                    break;
                }
                case "remove":{
                    removeRole(event,guildEntity);
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
    private guildObject editRoleName(MessageReceivedEvent event, String RoleName, guildObject guildEntity){
        if (RoleName.equalsIgnoreCase("")){
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(getName())
                        .setColor(Color.RED)
                        .setDescription("Please include a name.");
            event.getChannel().sendMessage(embedBuilder.build()).queue();
            return guildEntity;
        }
        if (getRole(event,guildEntity) == null){
            return createRole(event,RoleName,guildEntity);
        }
        Role userRole = event.getGuild().getRoleById(getRole(event,guildEntity));
        if (userRole == null){
            return createRole(event,RoleName,guildEntity);
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
    private guildObject editRoleColor(MessageReceivedEvent event, String RoleColor, guildObject guildEntity){
        if (RoleColor.equalsIgnoreCase("")){
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(getName())
                    .setColor(Color.RED)
                    .setDescription("Please include a Color.");
            event.getChannel().sendMessage(embedBuilder.build()).queue();
            return guildEntity;
        }
        if (getRole(event,guildEntity) == null){
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(getName())
                    .setColor(Color.RED)
                    .setDescription("Please create a role before trying to change the color.");
            event.getChannel().sendMessage(embedBuilder.build()).queue();
            return guildEntity;
        }
        Role userRole = event.getGuild().getRoleById(getRole(event));
        if (userRole == null){
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(getName())
                    .setColor(Color.RED)
                    .setDescription("Please create a role before trying to change the color.");
            event.getChannel().sendMessage(embedBuilder.build()).queue();
            return guildEntity;
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
            return guildEntity;
        }
    }
    private guildObject createRole(MessageReceivedEvent event, String RoleName, guildObject guildEntity){
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
        return guildEntity;
    }
    private guildObject removeRole(MessageReceivedEvent event, guildObject guildEntity){
        try {
            customRoleObject customRoleEntity = guildEntity.getCustomRole();
            String roleID = getRole(event,guildEntity);
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
    private String getRole(MessageReceivedEvent event, guildObject guildEntity){
        customRoleObject customRoleEntity = guildEntity.getCustomRole();
        return customRoleEntity.getRoleID(event.getMember().getId());
    }
}