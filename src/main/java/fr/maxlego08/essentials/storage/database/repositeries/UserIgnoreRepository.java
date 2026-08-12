package fr.maxlego08.essentials.storage.database.repositeries;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.dto.IgnoreDTO;
import fr.maxlego08.essentials.storage.database.Repository;
import fr.maxlego08.sarah.DatabaseConnection;

import java.util.List;
import java.util.UUID;

public class UserIgnoreRepository extends Repository {

    public UserIgnoreRepository(EssentialsPlugin plugin, DatabaseConnection connection) {
        super(plugin, connection, "user_ignores");
    }

    public void upsert(UUID uuid, UUID ignoredId) {
        upsert(table -> {
            table.uuid("unique_id", uuid).primary();
            table.uuid("ignored_id", ignoredId).primary();
        });
    }

    public List<IgnoreDTO> select(UUID uuid) {
        return select(IgnoreDTO.class, schema -> schema.where("unique_id", uuid));
    }

    public void delete(UUID uuid, UUID ignoredId) {
        delete(table -> table.where("unique_id", uuid).where("ignored_id", ignoredId));
    }
}
