package fr.maxlego08.essentials.commands.commands.mail;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.MailBoxModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.command.CommandSender;

public class CommandMailClearMessages extends VCommand {

    public CommandMailClearMessages(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(MailBoxModule.class);
        this.setDescription(Message.DESCRIPTION_MAIL_CLEAR_MESSAGES);
        this.setPermission(Permission.ESSENTIALS_MAIL_CLEAR_MESSAGES);
        this.addSubCommand("clearmessages");
        this.addOptionalOfflinePlayerNameArg();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        MailBoxModule module = plugin.getModuleManager().getModule(MailBoxModule.class);
        CommandSender commandSender = this.sender;

        String userName = this.argAsString(0, null);

        // Without an argument the sender deletes his own mails
        if (userName == null) {
            if (this.player == null) {
                message(commandSender, Message.COMMAND_NO_CONSOLE);
                return CommandResultType.DEFAULT;
            }
            module.clearMailMessages(commandSender, this.player.getUniqueId(), this.player.getName());
            return CommandResultType.SUCCESS;
        }

        String finalUserName = userName;
        fetchUniqueId(finalUserName, uuid -> module.clearMailMessages(commandSender, uuid, finalUserName));

        return CommandResultType.SUCCESS;
    }
}
