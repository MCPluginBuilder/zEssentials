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

public class CommandHomeCategory extends VCommand {

    public CommandHomeCategory(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(HomeModule.class);
        this.setPermission(Permission.ESSENTIALS_HOME_CATEGORY);
        this.setDescription(Message.DESCRIPTION_HOME_CATEGORY);
        this.onlyPlayers();
        this.addRequireArg("name", (sender, args) -> {
            if (sender instanceof Player player) {
                User user = plugin.getUser(player.getUniqueId());
                if (user != null) return user.getHomes().stream().map(Home::getName).toList();
            }
            return new ArrayList<>();
        });
        this.addRequireArg("category");
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        HomeModule homeModule = plugin.getModuleManager().getModule(HomeModule.class);
        if (!homeModule.isEnableCategories()) {
            message(sender, Message.COMMAND_HOME_FEATURE_DISABLED);
            return CommandResultType.DEFAULT;
        }

        String homeName = this.argAsString(0);
        String category = this.argAsString(1);

        Optional<Home> optional = this.user.getHome(homeName);
        if (optional.isEmpty()) {
            message(sender, Message.COMMAND_HOME_DOESNT_EXIST, "%name%", homeName);
            return CommandResultType.DEFAULT;
        }

        Home home = optional.get();

        if (category.equalsIgnoreCase("none") || category.equalsIgnoreCase("reset")) {
            home.setCategory(null);
            this.user.saveHomeSocial(home);
            message(sender, Message.COMMAND_HOME_CATEGORY_RESET, "%name%", home.getName());
            return CommandResultType.SUCCESS;
        }

        if (!category.matches("[a-zA-Z0-9_-]{1,32}")) {
            message(sender, Message.COMMAND_HOME_CATEGORY_INVALID);
            return CommandResultType.DEFAULT;
        }

        home.setCategory(category);
        this.user.saveHomeSocial(home);
        message(sender, Message.COMMAND_HOME_CATEGORY_SET, "%name%", home.getName(), "%category%", category);
        return CommandResultType.SUCCESS;
    }
}
