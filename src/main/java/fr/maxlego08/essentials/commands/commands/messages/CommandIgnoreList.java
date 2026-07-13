package fr.maxlego08.essentials.commands.commands.messages;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.MessageModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.UUID;

public class CommandIgnoreList extends VCommand {

    public CommandIgnoreList(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(MessageModule.class);
        this.setPermission(Permission.ESSENTIALS_IGNORE_LIST);
        this.setDescription(Message.DESCRIPTION_IGNORE_LIST);
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        List<UUID> ignored = this.user.getIgnoredPlayers();
        if (ignored.isEmpty()) {
            message(sender, Message.COMMAND_IGNORE_LIST_EMPTY);
            return CommandResultType.SUCCESS;
        }

        message(sender, Message.COMMAND_IGNORE_LIST_HEADER, "%count%", ignored.size());
        for (UUID uuid : ignored) {
            String name = Bukkit.getOfflinePlayer(uuid).getName();
            message(sender, Message.COMMAND_IGNORE_LIST_LINE, "%player%", name == null ? uuid.toString() : name);
        }

        return CommandResultType.SUCCESS;
    }
}
