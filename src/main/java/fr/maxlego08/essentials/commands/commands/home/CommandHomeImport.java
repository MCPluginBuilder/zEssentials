package fr.maxlego08.essentials.commands.commands.home;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.utils.SafeLocation;
import fr.maxlego08.essentials.module.modules.HomeModule;
import fr.maxlego08.essentials.user.ZHome;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * Imports homes from another plugin (EssentialsX) into zEssentials.
 */
public class CommandHomeImport extends VCommand {

    public CommandHomeImport(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(HomeModule.class);
        this.setPermission(Permission.ESSENTIALS_HOME_IMPORT);
        this.setDescription(Message.DESCRIPTION_HOME_IMPORT);
        this.addRequireArg("source", (sender, args) -> List.of("essentialsx"));
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String source = this.argAsString(0);
        if (!source.equalsIgnoreCase("essentialsx") && !source.equalsIgnoreCase("essentials")) {
            message(sender, Message.COMMAND_HOME_IMPORT_UNKNOWN, "%source%", source);
            return CommandResultType.DEFAULT;
        }

        message(sender, Message.COMMAND_HOME_IMPORT_START, "%source%", source);

        CommandSender commandSender = this.sender;
        plugin.getScheduler().runAsync(wrappedTask -> importEssentials(plugin, commandSender));

        return CommandResultType.SUCCESS;
    }

    private void importEssentials(EssentialsPlugin plugin, CommandSender sender) {

        File userdata = new File(plugin.getDataFolder().getParentFile(), "Essentials/userdata");
        if (!userdata.isDirectory()) {
            message(sender, Message.COMMAND_HOME_IMPORT_ERROR);
            return;
        }

        File[] files = userdata.listFiles((dir, name) -> name.toLowerCase().endsWith(".yml"));
        if (files == null) {
            message(sender, Message.COMMAND_HOME_IMPORT_ERROR);
            return;
        }

        int imported = 0;
        int skipped = 0;
        int errors = 0;
        IStorage iStorage = plugin.getStorageManager().getStorage();

        for (File file : files) {
            try {
                UUID uuid = UUID.fromString(file.getName().substring(0, file.getName().length() - 4));
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                ConfigurationSection homes = config.getConfigurationSection("homes");
                if (homes == null) continue;

                for (String homeName : homes.getKeys(false)) {
                    String path = "homes." + homeName + ".";
                    String worldName = config.getString(path + "world-name", config.getString(path + "world"));
                    World world = worldName == null ? null : Bukkit.getWorld(worldName);
                    if (world == null) {
                        skipped++;
                        continue;
                    }

                    double x = config.getDouble(path + "x");
                    double y = config.getDouble(path + "y");
                    double z = config.getDouble(path + "z");
                    float yaw = (float) config.getDouble(path + "yaw");
                    float pitch = (float) config.getDouble(path + "pitch");

                    Location location = new Location(world, x, y, z, yaw, pitch);
                    iStorage.upsertHome(uuid, new ZHome(new SafeLocation(location), homeName, null));
                    imported++;
                }
            } catch (Exception exception) {
                errors++;
                plugin.getLogger().warning("Failed to import homes from " + file.getName() + ": " + exception.getMessage());
            }
        }

        message(sender, Message.COMMAND_HOME_IMPORT_DONE, "%imported%", imported, "%skipped%", skipped, "%errors%", errors);
    }
}
