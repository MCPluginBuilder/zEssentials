package fr.maxlego08.essentials.commands.commands.home;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.home.Home;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.modules.HomeModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class CommandHomeUnshare extends VCommand {

    public CommandHomeUnshare(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(HomeModule.class);
        this.setPermission(Permission.ESSENTIALS_HOME_SHARE);
        this.setDescription(Message.DESCRIPTION_HOME_UNSHARE);
        this.onlyPlayers();
        this.addRequireArg("name", (sender, args) -> {
            if (sender instanceof Player player) {
                User user = plugin.getUser(player.getUniqueId());
                if (user != null) return user.getHomes().stream().map(Home::getName).toList();
            }
            return new ArrayList<>();
        });
        this.addRequireArg("player", (sender, args) -> {
            if (sender instanceof Player player && args.length >= 1) {
                User user = plugin.getUser(player.getUniqueId());
                if (user != null) {
                    return user.getHomeShares(args[0]).stream()
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

        String homeName = this.argAsString(0);
        String targetName = this.argAsString(1);

        Optional<Home> optional = this.user.getHome(homeName);
        if (optional.isEmpty()) {
            message(sender, Message.COMMAND_HOME_DOESNT_EXIST, "%name%", homeName);
            return CommandResultType.DEFAULT;
        }

        String canonicalName = optional.get().getName();

        fetchUniqueId(targetName, targetUuid -> {
            if (targetUuid == null) {
                message(sender, Message.PLAYER_NOT_FOUND, "%player%", targetName);
                return;
            }

            if (this.user.removeHomeShare(canonicalName, targetUuid)) {
                message(sender, Message.COMMAND_HOME_SHARE_REMOVE, "%name%", canonicalName, "%player%", targetName);
            } else {
                message(sender, Message.COMMAND_HOME_SHARE_NOT, "%name%", canonicalName, "%player%", targetName);
            }
        });

        return CommandResultType.SUCCESS;
    }
}
