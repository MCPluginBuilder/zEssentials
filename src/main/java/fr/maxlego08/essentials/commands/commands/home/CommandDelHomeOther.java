package fr.maxlego08.essentials.commands.commands.home;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.home.HomeManager;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.HomeModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

/**
 * Administrator command to delete a specific home of another player.
 * Works for both online and offline players.
 * Usage: /delhome-other &lt;player&gt; &lt;home&gt;
 */
public class CommandDelHomeOther extends VCommand {

    public CommandDelHomeOther(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(HomeModule.class);
        this.setPermission(Permission.ESSENTIALS_DEL_HOME_OTHER);
        this.setDescription(Message.DESCRIPTION_DEL_HOME_OTHER);
        this.addRequireOfflinePlayerNameArg();
        this.addRequireArg("name");
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        HomeManager homeManager = plugin.getHomeManager();
        homeManager.deleteHome(this.sender, this.argAsString(0), this.argAsString(1));

        return CommandResultType.SUCCESS;
    }
}
