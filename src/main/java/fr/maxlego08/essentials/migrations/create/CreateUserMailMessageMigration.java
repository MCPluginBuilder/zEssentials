package fr.maxlego08.essentials.migrations.create;

import fr.maxlego08.sarah.database.Migration;

public class CreateUserMailMessageMigration extends Migration {

    @Override
    public void up() {
        create("%prefix%user_mail_messages", table -> {
            table.autoIncrement("id");
            table.uuid("unique_id").foreignKey("%prefix%users");
            table.uuid("sender_id").nullable();
            table.string("sender_name", 36);
            table.longText("content");
            table.bool("is_read").defaultValue(false);
            table.timestamps();
        });
    }
}
