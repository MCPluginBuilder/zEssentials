package fr.maxlego08.essentials.migrations.create;

import fr.maxlego08.sarah.database.Migration;

public class CreateUserHomeShareTableMigration extends Migration {
    @Override
    public void up() {
        create("%prefix%user_home_shares", table -> {
            table.uuid("owner_id").primary().foreignKey("%prefix%users", "unique_id", true);
            table.string("home_name", 255).primary();
            table.uuid("target_id").primary();
            table.timestamps();
        });
    }
}
