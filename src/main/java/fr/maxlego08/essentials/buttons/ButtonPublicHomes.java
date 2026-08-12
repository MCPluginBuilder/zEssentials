package fr.maxlego08.essentials.buttons;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.dto.PublicHomeDTO;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.modules.HomeModule;
import fr.maxlego08.menu.api.MenuItemStack;
import fr.maxlego08.menu.api.button.PaginateButton;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.engine.Pagination;
import fr.maxlego08.menu.api.utils.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ButtonPublicHomes extends PaginateButton {

    private final EssentialsPlugin plugin;

    public ButtonPublicHomes(Plugin plugin) {
        this.plugin = (EssentialsPlugin) plugin;
    }

    @Override
    public boolean hasSpecialRender() {
        return true;
    }

    @Override
    public void onRender(Player player, InventoryEngine inventory) {

        User user = plugin.getUser(player.getUniqueId());
        if (user == null) return;

        HomeModule homeModule = plugin.getModuleManager().getModule(HomeModule.class);
        List<PublicHomeDTO> homes = homeModule.getCachedPublicHomes(player.getUniqueId());

        Pagination<PublicHomeDTO> pagination = new Pagination<>();
        AtomicInteger atomicInteger = new AtomicInteger(0);
        pagination.paginate(homes, this.slots.size(), inventory.getPage()).forEach(dto -> displayHome(this.slots.get(atomicInteger.getAndIncrement()), dto, player, user, inventory, homeModule));
    }

    private void displayHome(int slot, PublicHomeDTO dto, Player player, User user, InventoryEngine inventory, HomeModule homeModule) {

        String ownerName = Bukkit.getOfflinePlayer(dto.unique_id()).getName();
        if (ownerName == null) ownerName = dto.unique_id().toString();

        MenuItemStack menuItemStack = this.getItemStack();
        Placeholders placeholders = new Placeholders();
        placeholders.register("player", ownerName);
        placeholders.register("name", dto.name());
        placeholders.register("category", dto.category() == null ? "" : dto.category());
        placeholders.register("material", dto.material() == null ? homeModule.getDefaultHomeMaterial() : dto.material());

        String finalOwnerName = ownerName;
        inventory.addItem(slot, menuItemStack.build(player, false, placeholders)).setClick(event -> {
            player.closeInventory();
            homeModule.visitHome(user, finalOwnerName, dto.name());
        });
    }

    @Override
    public boolean hasPermission() {
        return true;
    }

    @Override
    public int getPaginationSize(Player player) {
        HomeModule homeModule = plugin.getModuleManager().getModule(HomeModule.class);
        return homeModule.getCachedPublicHomes(player.getUniqueId()).size();
    }
}
