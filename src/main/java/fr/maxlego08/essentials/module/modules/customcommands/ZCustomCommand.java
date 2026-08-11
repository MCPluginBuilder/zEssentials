package fr.maxlego08.essentials.module.modules.customcommands;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.TimerBuilder;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import fr.maxlego08.menu.api.requirement.Action;
import fr.maxlego08.menu.api.utils.Placeholders;

import java.util.List;

/**
 * The command executed when a player runs a command defined in modules/customcommands/config.yml.
 * One instance is created for each entry of the configuration.
 */
public class ZCustomCommand extends VCommand {

    private final CustomCommand customCommand;

    public ZCustomCommand(EssentialsPlugin plugin, CustomCommand customCommand) {
        super(plugin);
        this.customCommand = customCommand;
        this.setModule(CustomCommandModule.class);

        if (customCommand.hasPermission()) {
            this.setPermission(customCommand.permission());
        }
        if (customCommand.description() != null) {
            this.setDescription(customCommand.description());
        }
    }

    public CustomCommand getCustomCommand() {
        return this.customCommand;
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        if (!checkCooldown()) return CommandResultType.COOLDOWN;

        sendMessages();
        executeActions(plugin);

        return CommandResultType.SUCCESS;
    }

    /**
     * Checks the cooldown of the command and registers a new one if the player can use it.
     *
     * @return true if the command can be executed
     */
    private boolean checkCooldown() {

        long cooldown = this.customCommand.cooldown();
        if (cooldown <= 0 || this.user == null) return true;

        if (this.user.hasPermission(Permission.ESSENTIALS_BYPASS_COOLDOWN) && this.configuration.isEnableCooldownBypass()) {
            return true;
        }

        String key = this.customCommand.getCooldownKey();
        if (this.user.isCooldown(key)) {
            message(this.sender, Message.COOLDOWN, "%cooldown%", TimerBuilder.getStringTime(this.user.getCooldown(key) - System.currentTimeMillis()));
            return false;
        }

        this.user.addCooldown(key, cooldown);
        return true;
    }

    private void sendMessages() {

        List<String> messages = this.customCommand.messages();

        switch (this.customCommand.messageType()) {
            case NONE -> {
            }
            case TITLE -> {
                if (this.player != null) {
                    this.componentMessage.sendTitle(this.player, this.customCommand.titleMessage());
                }
            }
            case BOSSBAR -> {
                if (this.player != null) {
                    this.componentMessage.sendBossBar(this.plugin, this.player, this.customCommand.bossBarMessage());
                }
            }
            case ACTION -> {
                if (this.player != null) {
                    this.componentMessage.sendActionBar(this.player, papi(String.join(" ", messages), this.player));
                }
            }
            // Placeholders must be resolved before centering the message, otherwise the length used
            // to center it would be the length of the placeholder and not the length of its value.
            case CENTER ->
                    messages.forEach(line -> this.componentMessage.sendMessage(this.sender, getCenteredMessage(papi(line, this.player))));
            default -> messages.forEach(line -> this.componentMessage.sendMessage(this.sender, line));
        }
    }

    /**
     * Executes the zMenu actions of the command. Actions need a player, they are skipped for the console.
     */
    private void executeActions(EssentialsPlugin plugin) {

        List<Action> actions = this.customCommand.actions();
        if (actions.isEmpty() || this.player == null) return;

        Placeholders placeholders = new Placeholders();
        placeholders.register("player", this.player.getName());

        var fakeInventory = plugin.getInventoryManager().getFakeInventory();
        actions.forEach(action -> action.preExecute(this.player, null, fakeInventory, placeholders));
    }
}
