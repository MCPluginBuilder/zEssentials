package fr.maxlego08.essentials.commands.commands.chat;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.module.modules.chat.ChatModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

/**
 * Command to toggle the chat ping sound.
 * Allows players to enable/disable the sound played when they are pinged in chat.
 */
public class CommandPingSound extends VCommand {

    public CommandPingSound(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(ChatModule.class);
        this.setPermission(Permission.ESSENTIALS_PING_SOUND);
        this.setDescription(Message.DESCRIPTION_PING_SOUND);
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        this.user.setOption(Option.PLAYER_PING_SOUND_DISABLE, !this.user.getOption(Option.PLAYER_PING_SOUND_DISABLE));
        boolean isDisabled = this.user.getOption(Option.PLAYER_PING_SOUND_DISABLE);

        Message messageKey = isDisabled ? Message.COMMAND_PING_SOUND_DISABLE : Message.COMMAND_PING_SOUND_ENABLE;
        message(sender, messageKey, "%player%", Message.YOU.getMessageAsString());

        return CommandResultType.SUCCESS;
    }
}
