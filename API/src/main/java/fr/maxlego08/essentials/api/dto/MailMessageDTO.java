package fr.maxlego08.essentials.api.dto;

import java.util.Date;
import java.util.UUID;

/**
 * A text message sent to a player with /mail send, readable even if the player was offline.
 *
 * @param id          the id of the message
 * @param unique_id   the receiver of the message
 * @param sender_id   the sender of the message, null when the message comes from the console
 * @param sender_name the name of the sender, used to display the message without another request
 * @param content     the message itself
 * @param is_read     whether the receiver has already read the message
 * @param created_at  when the message was sent
 */
public record MailMessageDTO(int id, UUID unique_id, UUID sender_id, String sender_name, String content,
                             Boolean is_read, Date created_at) {
}
