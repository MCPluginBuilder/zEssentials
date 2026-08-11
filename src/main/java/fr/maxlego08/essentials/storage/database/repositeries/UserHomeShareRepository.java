package fr.maxlego08.essentials.storage.database.repositeries;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.dto.HomeShareDTO;
import fr.maxlego08.essentials.storage.database.Repository;
import fr.maxlego08.sarah.DatabaseConnection;

import java.util.List;
import java.util.UUID;

public class UserHomeShareRepository extends Repository {

    public UserHomeShareRepository(EssentialsPlugin plugin, DatabaseConnection connection) {
        super(plugin, connection, "user_home_shares");
    }

    public void upsert(UUID owner, String homeName, UUID target) {
        upsert(table -> {
            table.uuid("owner_id", owner).primary();
            table.string("home_name", homeName).primary();
            table.uuid("target_id", target).primary();
        });
    }

    public void delete(UUID owner, String homeName, UUID target) {
        delete(table -> table.where("owner_id", owner).where("home_name", homeName).where("target_id", target));
    }

    public void deleteAll(UUID owner, String homeName) {
        delete(table -> table.where("owner_id", owner).where("home_name", homeName));
    }

    public List<HomeShareDTO> selectByOwner(UUID owner) {
        return select(HomeShareDTO.class, schema -> schema.where("owner_id", owner));
    }

    public List<HomeShareDTO> selectSharedWith(UUID target) {
        return select(HomeShareDTO.class, schema -> schema.where("target_id", target));
    }

    public boolean isSharedWith(UUID owner, String homeName, UUID target) {
        return !select(HomeShareDTO.class, schema -> schema.where("owner_id", owner).where("home_name", homeName).where("target_id", target)).isEmpty();
    }
}
