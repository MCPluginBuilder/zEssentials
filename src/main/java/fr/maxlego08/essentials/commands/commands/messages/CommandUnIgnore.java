package fr.maxlego08.essentials.commands.commands.messages;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.modules.MessageModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Objects;

public class CommandUnIgnore extends VCommand {

    public CommandUnIgnore(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(MessageModule.class);
        this.setPermission(Permission.ESSENTIALS_IGNORE);
        this.setDescription(Message.DESCRIPTION_UNIGNORE);
        this.onlyPlayers();
        this.addRequireArg("player", (sender, args) -> {
            if (sender instanceof Player player) {
                User user = plugin.getUser(player.getUniqueId());
                if (user != null) {
                    return user.getIgnoredPlayers().stream()
                            .map(uuid -> Bukkit.getOfflinePlayer(uuid).getName())
                            .filter(Objects::nonNull)
                            .toList();
                }
            }
            return new ArrayList<>();
        });
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String userName = this.argAsString(0);

        fetchUniqueId(userName, uuid -> {
            if (uuid == null) {
                message(sender, Message.PLAYER_NOT_FOUND, "%player%", userName);
                return;
            }

            if (this.user.removeIgnore(uuid)) {
                message(sender, Message.COMMAND_IGNORE_REMOVE, "%player%", userName);
            } else {
                message(sender, Message.COMMAND_IGNORE_NOT, "%player%", userName);
            }
        });

        return CommandResultType.SUCCESS;
    }
}
