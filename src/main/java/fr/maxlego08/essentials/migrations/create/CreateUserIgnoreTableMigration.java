package fr.maxlego08.essentials.migrations.create;

import fr.maxlego08.sarah.database.Migration;

public class CreateUserIgnoreTableMigration extends Migration {
    @Override
    public void up() {
        create("%prefix%user_ignores", table -> {
            table.uuid("unique_id").primary().foreignKey("%prefix%users");
            table.uuid("ignored_id").primary();
            table.timestamps();
        });
    }
}
