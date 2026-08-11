package fr.maxlego08.essentials.commands.commands.home;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.home.Home;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.modules.HomeModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class CommandHomeShare extends VCommand {

    public CommandHomeShare(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(HomeModule.class);
        this.setPermission(Permission.ESSENTIALS_HOME_SHARE);
        this.setDescription(Message.DESCRIPTION_HOME_SHARE);
        this.onlyPlayers();
        this.addRequireArg("name", (sender, args) -> {
            if (sender instanceof Player player) {
                User user = plugin.getUser(player.getUniqueId());
                if (user != null) return user.getHomes().stream().map(Home::getName).toList();
            }
            return new ArrayList<>();
        });
        this.addRequireOfflinePlayerNameArg();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        HomeModule homeModule = plugin.getModuleManager().getModule(HomeModule.class);
        if (!homeModule.isEnableSharedHomes()) {
            message(sender, Message.COMMAND_HOME_FEATURE_DISABLED);
            return CommandResultType.DEFAULT;
        }

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

            if (targetUuid.equals(this.user.getUniqueId())) {
                message(sender, Message.COMMAND_HOME_SHARE_SELF);
                return;
            }

            int max = homeModule.getMaxSharedPerHome();
            Set<UUID> current = this.user.getHomeShares(canonicalName);
            if (max >= 0 && !current.contains(targetUuid) && current.size() >= max) {
                message(sender, Message.COMMAND_HOME_SHARE_LIMIT, "%max%", max);
                return;
            }

            if (this.user.addHomeShare(canonicalName, targetUuid)) {
                message(sender, Message.COMMAND_HOME_SHARE_ADD, "%name%", canonicalName, "%player%", targetName);
            } else {
                message(sender, Message.COMMAND_HOME_SHARE_ALREADY, "%name%", canonicalName, "%player%", targetName);
            }
        });

        return CommandResultType.SUCCESS;
    }
}
