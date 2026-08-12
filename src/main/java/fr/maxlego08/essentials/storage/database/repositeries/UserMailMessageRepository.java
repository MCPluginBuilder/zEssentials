package fr.maxlego08.essentials.storage.database.repositeries;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.dto.MailMessageDTO;
import fr.maxlego08.essentials.api.mailbox.MailMessage;
import fr.maxlego08.essentials.storage.database.Repository;
import fr.maxlego08.sarah.DatabaseConnection;

import java.util.List;
import java.util.UUID;

public class UserMailMessageRepository extends Repository {

    public UserMailMessageRepository(EssentialsPlugin plugin, DatabaseConnection connection) {
        super(plugin, connection, "user_mail_messages");
    }

    public List<MailMessageDTO> select(UUID uuid) {
        return this.select(MailMessageDTO.class, table -> table.where("unique_id", uuid));
    }

    public void insert(MailMessage mailMessage) {
        this.insert(table -> {
            table.uuid("unique_id", mailMessage.getUniqueId());
            table.uuid("sender_id", mailMessage.getSenderId());
            table.string("sender_name", mailMessage.getSenderName());
            table.string("content", mailMessage.getContent());
            table.bool("is_read", mailMessage.isRead());
        }, mailMessage::setId);
    }

    public void markAsRead(UUID uuid) {
        this.update(table -> {
            table.bool("is_read", true);
            table.where("unique_id", uuid);
        });
    }

    public void clear(UUID uuid) {
        this.delete(table -> table.where("unique_id", uuid));
    }
}
