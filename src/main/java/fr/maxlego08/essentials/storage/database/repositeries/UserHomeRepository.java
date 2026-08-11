package fr.maxlego08.essentials.storage.database.repositeries;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.dto.HomeDTO;
import fr.maxlego08.essentials.api.dto.PublicHomeDTO;
import fr.maxlego08.essentials.api.home.Home;
import fr.maxlego08.essentials.storage.database.Repository;
import fr.maxlego08.essentials.user.ZHome;
import fr.maxlego08.sarah.DatabaseConnection;
import org.bukkit.Material;

import java.util.List;
import java.util.UUID;

public class UserHomeRepository extends Repository {

    public UserHomeRepository(EssentialsPlugin plugin, DatabaseConnection connection) {
        super(plugin, connection, "user_homes");
    }

    public void upsert(UUID uuid, Home home) {
        upsert(table -> {
            table.uuid("unique_id", uuid).primary();
            table.string("name", home.getName()).primary();
            table.string("location", locationAsString(home.getLocation()));
            table.string("material", home.getMaterial() != null ? home.getMaterial().name() : null);
            table.bool("is_public", home.isPublic());
            table.string("category", home.getCategory());
            table.bool("is_favorite", home.isFavorite());
        });
    }

    public void updateSocial(UUID uuid, Home home) {
        update(table -> {
            table.bool("is_public", home.isPublic());
            table.string("category", home.getCategory());
            table.bool("is_favorite", home.isFavorite());
            table.where("unique_id", uuid);
            table.where("name", home.getName());
        });
    }

    public List<HomeDTO> select(UUID uuid) {
        return select(HomeDTO.class, schema -> schema.where("unique_id", uuid));
    }

    public List<PublicHomeDTO> selectPublicHomes() {
        return select(PublicHomeDTO.class, schema -> schema.where("is_public", true));
    }

    public void deleteHome(UUID uuid, String name) {
        delete(table -> table.where("unique_id", uuid).where("name", name));
    }

    public List<Home> getHomes(UUID uuid, String homeName) {
        // Case-insensitive to match the owner's in-memory equalsIgnoreCase lookup (SQLite name comparison is case-sensitive)
        return getHomes(uuid).stream().filter(home -> home.getName().equalsIgnoreCase(homeName)).toList();
    }

    public List<Home> getHomes(UUID uuid) {
        return select(HomeDTO.class, schema -> schema.where("unique_id", uuid)).stream().map(this::toHome).toList();
    }

    private Home toHome(HomeDTO homeDTO) {
        Material material = null;
        if (homeDTO.material() != null) {
            try {
                material = Material.valueOf(homeDTO.material());
            } catch (IllegalArgumentException ignored) {
            }
        }
        boolean isPublic = homeDTO.is_public() != null && homeDTO.is_public();
        boolean favorite = homeDTO.is_favorite() != null && homeDTO.is_favorite();
        return new ZHome(stringAsLocation(homeDTO.location()), homeDTO.name(), material, isPublic, homeDTO.category(), favorite);
    }

    public void deleteWorldData(String worldName) {
        delete(table -> table.where("location", "LIKE", "%" + worldName + "%"));
    }
}
