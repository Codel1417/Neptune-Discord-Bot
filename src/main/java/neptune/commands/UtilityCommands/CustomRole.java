package neptune.commands.UtilityCommands;

import neptune.commands.ICommand;
import neptune.commands.CommandHelpers;
import neptune.storage.Enum.GuildOptionsEnum;
import neptune.storage.Guild.GuildStorageHandler;
import neptune.storage.Guild.guildObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

import java.awt.Color;
import java.io.IOException;

public class CustomRole extends CommandHelpers implements ICommand {
    protected static final Logger log = LogManager.getLogger();

    @Override
    public void run(
            GuildMessageReceivedEvent event, String messageContent) {
        String[] command = getCommandName(messageContent);
        try {
            if (GuildStorageHandler.getInstance().readFile(event.getGuild().getId()).getGuildOptions().getOption(GuildOptionsEnum.CustomRoleEnabled)) {
                switch (command[0]) {
                    case "create":
                        GuildStorageHandler.getInstance().writeFile(createRole(event, command[1], GuildStorageHandler.getInstance().readFile(event.getGuild().getId())));
                        break;
                    case "editname":
                        {
                            GuildStorageHandler.getInstance().writeFile(editRoleName(event, command[1], GuildStorageHandler.getInstance().readFile(event.getGuild().getId())));
                            break;
                        }
                    case "editcolor":
                        {
                            GuildStorageHandler.getInstance().writeFile(editRoleColor(event, command[1], GuildStorageHandler.getInstance().readFile(event.getGuild().getId())));
                            break;
                        }
                    case "remove":
                        {
                            GuildStorageHandler.getInstance().writeFile(removeRole(event, GuildStorageHandler.getInstance().readFile(event.getGuild().getId())));
                            break;
                        }
                    default:
                        {
                            menu(event);
                            break;
                        }
                }
            } else {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setColor(Color.RED).setTitle("Custom Role");
                embedBuilder.setDescription(
                        "Custom roles is not enabled, Please have an admin enable it in options.");
                event.getChannel().sendMessage(embedBuilder.build()).queue();
            }
        } catch (IOException e) {
            log.error(e);
        }
    }

    private void menu(GuildMessageReceivedEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.MAGENTA);
        embedBuilder.setTitle("Custom Role");
        embedBuilder.addField("Create Role", "!nep customRole create (Role Name)", false);
        embedBuilder.addField("Remove Role", "!nep customRole remove", false);
        embedBuilder.addField("Change Role Name", "!nep customRole editName (Role Name)", false);
        embedBuilder.addField(
                "Change Role Color", "!nep customRole editColor (Hex Color Code)", false);
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }

    private guildObject editRoleName(
            GuildMessageReceivedEvent event, String RoleName, guildObject guildEntity) {
        if (RoleName.equalsIgnoreCase("")) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder
                    .setTitle("Custom Role")
                    .setColor(Color.RED)
                    .setDescription("Please include a name.");
            event.getChannel().sendMessage(embedBuilder.build()).queue();
            return guildEntity;
        }
        if (getRole(event, guildEntity) == null) {
            return createRole(event, RoleName, guildEntity);
        }
        Role userRole = event.getGuild().getRoleById(getRole(event, guildEntity));
        if (userRole == null) {
            return createRole(event, RoleName, guildEntity);
        } else {
            try {
                userRole.getManager().setName(RoleName).queue();
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder
                        .setColor(Color.MAGENTA)
                        .setTitle("Custom Role")
                        .setDescription("Role Updated Successfully");
                event.getChannel().sendMessage(embedBuilder.build()).queue();
            } catch (InsufficientPermissionException e) {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("Custom Role");
                embedBuilder.setColor(Color.RED);
                embedBuilder.setDescription(
                        "Neptune lacks the required permission to manage roles.");
                event.getChannel().sendMessage(embedBuilder.build()).queue();
            }
            return guildEntity;
        }
    }

    private guildObject editRoleColor(
            GuildMessageReceivedEvent event, String RoleColor, guildObject guildEntity) {
        if (RoleColor.equalsIgnoreCase("")) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder
                    .setTitle("Custom Role")
                    .setColor(Color.RED)
                    .setDescription("Please include a Color.");
            event.getChannel().sendMessage(embedBuilder.build()).queue();
            return guildEntity;
        }
        if (getRole(event, guildEntity) == null) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder
                    .setTitle("Custom Role")
                    .setColor(Color.RED)
                    .setDescription("Please create a role before trying to change the color.");
            event.getChannel().sendMessage(embedBuilder.build()).queue();
            return guildEntity;
        }
        Role userRole = event.getGuild().getRoleById(getRole(event, guildEntity));
        if (userRole == null) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder
                    .setTitle("Custom Role")
                    .setColor(Color.RED)
                    .setDescription("Please create a role before trying to change the color.");
            event.getChannel().sendMessage(embedBuilder.build()).queue();
            return guildEntity;
        } else {
            try {
                userRole.getManager().setColor(Integer.decode(RoleColor)).queue();
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder
                        .setColor(Color.MAGENTA)
                        .setTitle("Custom Role")
                        .setDescription("Role Updated Successfully");
                event.getChannel().sendMessage(embedBuilder.build()).queue();
            } catch (NumberFormatException e) {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("Custom Role");
                embedBuilder.setColor(Color.RED);
                embedBuilder.setDescription("That is not a valid discord color");
                event.getChannel().sendMessage(embedBuilder.build()).queue();
            } catch (InsufficientPermissionException e) {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("Custom Role");
                embedBuilder.setColor(Color.RED);
                embedBuilder.setDescription(
                        "Neptune lacks the required permission to manage roles.");
                event.getChannel().sendMessage(embedBuilder.build()).queue();
            }
            return guildEntity;
        }
    }

    private guildObject createRole(
            GuildMessageReceivedEvent event, String RoleName, guildObject guildEntity) {
        try {
            Role role = event.getGuild().createRole().setName(RoleName).setPermissions().complete();
            event.getGuild().addRoleToMember(event.getMember(), role).complete();
            guildEntity.getCustomRole().addRole(event.getMember().getId(), role.getId());
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder
                    .setColor(Color.MAGENTA)
                    .setTitle("Custom Role")
                    .setDescription("Role Created Successfully");
            event.getChannel().sendMessage(embedBuilder.build()).queue();
        } catch (InsufficientPermissionException e) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Custom Role");
            embedBuilder.setColor(Color.RED);
            embedBuilder.setDescription("Neptune lacks the required permission to manage roles.");
            event.getChannel().sendMessage(embedBuilder.build()).queue();
        }
        return guildEntity;
    }

    private guildObject removeRole(GuildMessageReceivedEvent event, guildObject guildEntity) {
        try {
            String roleID = getRole(event, guildEntity);
            event.getGuild().getRoleById(roleID).delete().reason("Custom Role Remove").complete();
            guildEntity.getCustomRole().removeRole(event.getMember().getId());
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder
                    .setColor(Color.MAGENTA)
                    .setTitle("Custom Role")
                    .setDescription("Role Deleted Successfully");
            event.getChannel().sendMessage(embedBuilder.build()).queue();
        } catch (InsufficientPermissionException e) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Custom Role");
            embedBuilder.setColor(Color.RED);
            embedBuilder.setDescription("Neptune lacks the required permission to manage roles.");
            event.getChannel().sendMessage(embedBuilder.build()).queue();
        }
        return guildEntity;
    }

    private String getRole(GuildMessageReceivedEvent event, guildObject guildEntity) {
        return guildEntity.getCustomRole().getRoleID(event.getMember().getId());
    }
}
