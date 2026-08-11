package fr.maxlego08.essentials.module.modules.customcommands;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.configuration.NonLoadable;
import fr.maxlego08.essentials.api.messages.MessageType;
import fr.maxlego08.essentials.api.messages.messages.BossBarMessage;
import fr.maxlego08.essentials.api.messages.messages.TitleMessage;
import fr.maxlego08.essentials.commands.ZCommandManager;
import fr.maxlego08.essentials.module.ZModule;
import fr.maxlego08.menu.api.requirement.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * Allows the server owner to create his own commands, to display a link, the rules, a placeholder,
 * or to run zMenu actions. Commands are defined in modules/customcommands/config.yml and are
 * registered when the module is loaded.
 */
public class CustomCommandModule extends ZModule {

    @NonLoadable
    private final List<ZCustomCommand> customCommands = new ArrayList<>();

    public CustomCommandModule(ZEssentialsPlugin plugin) {
        super(plugin, "customcommands");
    }

    @Override
    public void loadConfiguration() {
        super.loadConfiguration();

        ZCommandManager commandManager = (ZCommandManager) this.plugin.getCommandManager();

        // Commands of the previous load must be removed, /ezreload must not duplicate them
        this.customCommands.forEach(commandManager::unregisterCommand);
        this.customCommands.clear();

        if (this.isEnable) {
            List<Map<?, ?>> mapList = getConfiguration().getMapList("commands");
            for (int index = 0; index != mapList.size(); index++) {
                loadCustomCommand(commandManager, mapList.get(index), index);
            }
        }

        commandManager.refreshPlayerCommands();
    }

    public List<ZCustomCommand> getCustomCommands() {
        return this.customCommands;
    }

    private void loadCustomCommand(ZCommandManager commandManager, Map<?, ?> map, int index) {

        String path = "commands[" + index + "]";

        String command = sanitizeCommand(getString(map, "command", null));
        if (command == null) {
            this.plugin.getLogger().severe("Custom command at " + path + " has no valid 'command', it will be ignored.");
            return;
        }

        if (commandManager.isEssentialsCommand(command)) {
            this.plugin.getLogger().severe("The custom command /" + command + " is already a zEssentials command, it will be ignored.");
            return;
        }

        List<String> aliases = new ArrayList<>();
        for (String alias : getStringList(map, "aliases")) {
            String sanitizedAlias = sanitizeCommand(alias);
            if (sanitizedAlias == null || sanitizedAlias.equals(command) || aliases.contains(sanitizedAlias)) continue;
            if (commandManager.isEssentialsCommand(sanitizedAlias)) {
                this.plugin.getLogger().severe("The alias /" + sanitizedAlias + " of the custom command /" + command + " is already a zEssentials command, it will be ignored.");
                continue;
            }
            aliases.add(sanitizedAlias);
        }

        MessageType messageType = MessageType.fromString(getString(map, "type", "TCHAT"));
        if (messageType == null) {
            messageType = MessageType.TCHAT;
            this.plugin.getLogger().severe("Message type was not found for the custom command /" + command + ", use TCHAT by default.");
        }

        List<String> messages = getStringList(map, "messages");

        TitleMessage titleMessage = null;
        BossBarMessage bossBarMessage = null;

        if (messageType == MessageType.TITLE) {

            titleMessage = new TitleMessage(getString(map, "title", ""), getString(map, "subtitle", ""), getLong(map, "start", 100), getLong(map, "time", 2800), getLong(map, "end", 100));

        } else if (messageType == MessageType.BOSSBAR) {

            bossBarMessage = new BossBarMessage(getString(map, "text", ""), getString(map, "color", "WHITE").toUpperCase(Locale.ROOT), getString(map, "overlay", "PROGRESS").toUpperCase(Locale.ROOT), getStringList(map, "flags").stream().map(flag -> flag.toUpperCase(Locale.ROOT)).toList(), getLong(map, "duration", 60), getBoolean(map, "static", false));

            if (!bossBarMessage.isValid(this.plugin)) {
                this.plugin.getLogger().severe("The boss bar of the custom command /" + command + " is invalid, it will be ignored.");
                return;
            }
        }

        List<Action> actions = loadActions(map, path);

        if (messages.isEmpty() && titleMessage == null && bossBarMessage == null && actions.isEmpty()) {
            this.plugin.getLogger().severe("The custom command /" + command + " has nothing to display and no action, it will be ignored.");
            return;
        }

        CustomCommand customCommand = new CustomCommand(command, aliases, getString(map, "permission", null), getString(map, "description", null), getLong(map, "cooldown", 0), messageType, messages, titleMessage, bossBarMessage, actions);

        ZCustomCommand zCustomCommand = new ZCustomCommand(this.plugin, customCommand);
        this.customCommands.add(zCustomCommand);

        // commands.yml is indexed by class name, every custom command shares the same class, so the
        // configuration file must not be used here
        commandManager.registerCommand(this.plugin, command, zCustomCommand, aliases, false);
    }

    @SuppressWarnings("unchecked")
    private List<Action> loadActions(Map<?, ?> map, String path) {
        Object object = map.get("actions");
        if (!(object instanceof List<?> list) || list.isEmpty()) return new ArrayList<>();
        return this.plugin.getButtonManager().loadActions((List<Map<String, Object>>) list, path + ".actions", getConfigurationFile());
    }

    /**
     * Removes the leading slash and the case of a command name.
     *
     * @param command The command name written in the configuration
     * @return The command name, or null if it cannot be used
     */
    private String sanitizeCommand(String command) {
        if (command == null) return null;

        String result = command.toLowerCase(Locale.ROOT).trim();
        while (result.startsWith("/")) {
            result = result.substring(1).trim();
        }

        if (result.isEmpty() || result.contains(" ")) return null;
        return result;
    }

    private String getString(Map<?, ?> map, String key, String defaultValue) {
        Object object = map.get(key);
        return object == null ? defaultValue : String.valueOf(object);
    }

    private long getLong(Map<?, ?> map, String key, long defaultValue) {
        Object object = map.get(key);
        return object instanceof Number number ? number.longValue() : defaultValue;
    }

    private boolean getBoolean(Map<?, ?> map, String key, boolean defaultValue) {
        Object object = map.get(key);
        return object instanceof Boolean value ? value : defaultValue;
    }

    private List<String> getStringList(Map<?, ?> map, String key) {
        Object object = map.get(key);
        if (!(object instanceof List<?> list)) return new ArrayList<>();
        return list.stream().filter(Objects::nonNull).map(String::valueOf).toList();
    }
}
