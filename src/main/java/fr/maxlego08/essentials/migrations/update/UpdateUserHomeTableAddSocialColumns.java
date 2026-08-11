package fr.maxlego08.essentials.migrations.update;

import fr.maxlego08.sarah.MigrationManager;
import fr.maxlego08.sarah.SchemaBuilder;
import fr.maxlego08.sarah.database.DatabaseType;
import fr.maxlego08.sarah.database.Migration;

public class UpdateUserHomeTableAddSocialColumns extends Migration {
    @Override
    public void up() {
        if (MigrationManager.getDatabaseConfiguration().getDatabaseType() == DatabaseType.SQLITE) {
            // Forced to make the query one by one otherwise SQLITE will not appreciate
            SchemaBuilder.alter(this, "%prefix%user_homes", schema -> schema.bool("is_public").nullable());
            SchemaBuilder.alter(this, "%prefix%user_homes", schema -> schema.string("category", 32).nullable());
            SchemaBuilder.alter(this, "%prefix%user_homes", schema -> schema.bool("is_favorite").nullable());
        } else {
            SchemaBuilder.alter(this, "%prefix%user_homes", schema -> {
                schema.bool("is_public").nullable();
                schema.string("category", 32).nullable();
                schema.bool("is_favorite").nullable();
            });
        }
    }
}
