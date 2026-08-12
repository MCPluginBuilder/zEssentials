package fr.maxlego08.essentials.commands.commands.mail;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.MailBoxModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public class CommandMailSend extends VCommand {

    public CommandMailSend(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(MailBoxModule.class);
        this.setDescription(Message.DESCRIPTION_MAIL_SEND);
        this.setPermission(Permission.ESSENTIALS_MAIL_SEND);
        this.addSubCommand("send");
        this.addRequireOfflinePlayerNameArg();
        this.addRequireArg("message", (a, b) -> new ArrayList<>());
        this.setExtendedArgs(true);
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        MailBoxModule module = plugin.getModuleManager().getModule(MailBoxModule.class);
        String userName = this.argAsString(0);
        String content = getArgs(1);

        CommandSender commandSender = this.sender;
        fetchUniqueId(userName, uuid -> module.sendMailMessage(commandSender, uuid, userName, content));

        return CommandResultType.SUCCESS;
    }
}
