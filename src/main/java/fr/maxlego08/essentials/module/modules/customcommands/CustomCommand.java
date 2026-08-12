package fr.maxlego08.essentials.module.modules.customcommands;

import fr.maxlego08.essentials.api.messages.MessageType;
import fr.maxlego08.essentials.api.messages.messages.BossBarMessage;
import fr.maxlego08.essentials.api.messages.messages.TitleMessage;
import fr.maxlego08.menu.api.requirement.Action;

import java.util.List;

/**
 * A command created by the server owner in modules/customcommands/config.yml.
 * Displays a list of messages and/or runs a list of zMenu actions.
 *
 * @param command      The main command, without the leading slash
 * @param aliases      The aliases of the command
 * @param permission   The permission needed to use the command, null or empty for everyone
 * @param description  The description displayed in the command help
 * @param cooldown     The cooldown between two uses, in seconds, 0 to disable
 * @param messageType  How the messages are displayed
 * @param messages     The messages, used by TCHAT, CENTER, ACTION and WITHOUT_PREFIX
 * @param titleMessage The title, only used when the type is TITLE
 * @param bossBarMessage The boss bar, only used when the type is BOSSBAR
 * @param actions      The zMenu actions executed after the messages
 */
public record CustomCommand(String command, List<String> aliases, String permission, String description,
                            long cooldown, MessageType messageType, List<String> messages,
                            TitleMessage titleMessage, BossBarMessage bossBarMessage, List<Action> actions) {

    public boolean hasPermission() {
        return this.permission != null && !this.permission.isBlank();
    }

    /**
     * @return The key used to store the cooldown of this command for a player.
     */
    public String getCooldownKey() {
        return "custom-command-" + this.command;
    }
}
