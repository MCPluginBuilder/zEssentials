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
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class CommandHomeShares extends VCommand {

    public CommandHomeShares(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(HomeModule.class);
        this.setPermission(Permission.ESSENTIALS_HOME_SHARE);
        this.setDescription(Message.DESCRIPTION_HOME_SHARES);
        this.onlyPlayers();
        this.addRequireArg("name", (sender, args) -> {
            if (sender instanceof Player player) {
                User user = plugin.getUser(player.getUniqueId());
                if (user != null) return user.getHomes().stream().map(Home::getName).toList();
            }
            return new ArrayList<>();
        });
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String homeName = this.argAsString(0);

        Optional<Home> optional = this.user.getHome(homeName);
        if (optional.isEmpty()) {
            message(sender, Message.COMMAND_HOME_DOESNT_EXIST, "%name%", homeName);
            return CommandResultType.DEFAULT;
        }

        String canonicalName = optional.get().getName();
        Set<UUID> targets = this.user.getHomeShares(canonicalName);

        if (targets.isEmpty()) {
            message(sender, Message.COMMAND_HOME_SHARE_LIST_EMPTY, "%name%", canonicalName);
            return CommandResultType.SUCCESS;
        }

        message(sender, Message.COMMAND_HOME_SHARE_LIST_HEADER, "%name%", canonicalName, "%count%", targets.size());
        for (UUID target : targets) {
            String name = Bukkit.getOfflinePlayer(target).getName();
            message(sender, Message.COMMAND_HOME_SHARE_LIST_LINE, "%player%", name == null ? target.toString() : name);
        }

        return CommandResultType.SUCCESS;
    }
}
