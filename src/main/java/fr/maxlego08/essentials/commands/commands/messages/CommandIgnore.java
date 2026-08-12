package fr.maxlego08.essentials.commands.commands.messages;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.MessageModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandIgnore extends VCommand {

    public CommandIgnore(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(MessageModule.class);
        this.setPermission(Permission.ESSENTIALS_IGNORE);
        this.setDescription(Message.DESCRIPTION_IGNORE);
        this.onlyPlayers();
        this.addRequireOfflinePlayerNameArg();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String userName = this.argAsString(0);

        fetchUniqueId(userName, uuid -> {
            if (uuid == null) {
                message(sender, Message.PLAYER_NOT_FOUND, "%player%", userName);
                return;
            }

            if (uuid.equals(this.user.getUniqueId())) {
                message(sender, Message.COMMAND_IGNORE_SELF);
                return;
            }

            if (this.user.addIgnore(uuid)) {
                message(sender, Message.COMMAND_IGNORE_ADD, "%player%", userName);
            } else {
                message(sender, Message.COMMAND_IGNORE_ALREADY, "%player%", userName);
            }
        });

        return CommandResultType.SUCCESS;
    }
}
