package neptune.commands.UtilityCommands;

import java.awt.Color;

import neptune.commands.CommandInterface;
import neptune.commands.CommonMethods;
import neptune.commands.commandCategories;
import neptune.storage.customRoleObject;
import neptune.storage.guildObject;
import neptune.storage.guildOptionsObject;
import neptune.storage.Enum.options;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

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
    public boolean getHideCommand() {
        return false;
    }

    @Override
    public boolean getRequireManageUsers() {
        return false;
    }

    @Override
    public guildObject run(MessageReceivedEvent event, String messageContent, guildObject guildEntity) {
        String[] command = getCommandName(messageContent);
        guildOptionsObject guildOptionsEntity = guildEntity.getGuildOptions();
        if (guildOptionsEntity.getOption(options.CustomRoleEnabled)) {
            switch (command[0]){
                case "create":
                    createRole(event, command[1], guildEntity);
                    break;
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
        return guildEntity;
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
        return guildEntity;
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
        Role userRole = event.getGuild().getRoleById(getRole(event,guildEntity));
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
            customRoleObject customRoleEntity = guildEntity.getCustomRole();
            event.getGuild().addRoleToMember(event.getMember(),role).complete();
            customRoleEntity.addRole(event.getMember().getId(), role.getId());
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
            customRoleEntity.removeRole(event.getMember().getId());
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
        return guildEntity;
    }
    private String getRole(MessageReceivedEvent event, guildObject guildEntity){
        customRoleObject customRoleEntity = guildEntity.getCustomRole();
        return customRoleEntity.getRoleID(event.getMember().getId());
    }
}
