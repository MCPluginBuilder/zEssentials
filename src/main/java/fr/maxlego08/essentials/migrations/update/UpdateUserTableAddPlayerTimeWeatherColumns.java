package fr.maxlego08.essentials.migrations.update;

import fr.maxlego08.sarah.MigrationManager;
import fr.maxlego08.sarah.SchemaBuilder;
import fr.maxlego08.sarah.database.DatabaseType;
import fr.maxlego08.sarah.database.Migration;

public class UpdateUserTableAddPlayerTimeWeatherColumns extends Migration {
    @Override
    public void up() {
        if (MigrationManager.getDatabaseConfiguration().getDatabaseType() == DatabaseType.SQLITE) {
            // Forced to make the query one by one otherwise SQLITE will not appreciate
            SchemaBuilder.alter(this, "%prefix%users", schema -> schema.bigInt("player_time").defaultValue(0));
            SchemaBuilder.alter(this, "%prefix%users", schema -> schema.string("player_weather", 32).nullable());
        } else {
            SchemaBuilder.alter(this, "%prefix%users", schema -> {
                schema.bigInt("player_time").defaultValue(0);
                schema.string("player_weather", 32).nullable();
            });
        }
    }
}
