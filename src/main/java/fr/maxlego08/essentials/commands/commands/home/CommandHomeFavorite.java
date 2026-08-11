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

public class CommandHomeFavorite extends VCommand {

    public CommandHomeFavorite(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(HomeModule.class);
        this.setPermission(Permission.ESSENTIALS_HOME_FAVORITE);
        this.setDescription(Message.DESCRIPTION_HOME_FAVORITE);
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

        Home home = optional.get();
        boolean newState = !home.isFavorite();
        home.setFavorite(newState);
        this.user.saveHomeSocial(home);
        message(sender, newState ? Message.COMMAND_HOME_FAVORITE_ADD : Message.COMMAND_HOME_FAVORITE_REMOVE, "%name%", home.getName());
        return CommandResultType.SUCCESS;
    }
}
