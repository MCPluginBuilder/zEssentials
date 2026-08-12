package fr.maxlego08.essentials.api.mailbox;

import fr.maxlego08.essentials.api.dto.MailMessageDTO;

import java.util.Date;
import java.util.UUID;

/**
 * Represents a text message stored in a player's mailbox.
 * Unlike {@link MailBoxItem}, which stores an item, this is a message sent with /mail send that can
 * be read later, even if the receiver was offline when it was sent.
 */
public class MailMessage {

    private final UUID uniqueId;
    private final UUID senderId;
    private final String senderName;
    private final String content;
    private final Date createdAt;
    private int id;
    private boolean read;

    /**
     * Constructs a new MailMessage.
     *
     * @param uniqueId   the UUID of the player who receives the message
     * @param senderId   the UUID of the sender, null when the message comes from the console
     * @param senderName the name of the sender
     * @param content    the message
     * @param createdAt  the date the message was sent
     */
    public MailMessage(UUID uniqueId, UUID senderId, String senderName, String content, Date createdAt) {
        this.uniqueId = uniqueId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.content = content;
        this.createdAt = createdAt;
    }

    /**
     * Constructs a new MailMessage from a MailMessageDTO.
     *
     * @param mailMessageDTO the data transfer object containing the message data
     */
    public MailMessage(MailMessageDTO mailMessageDTO) {
        this.id = mailMessageDTO.id();
        this.uniqueId = mailMessageDTO.unique_id();
        this.senderId = mailMessageDTO.sender_id();
        this.senderName = mailMessageDTO.sender_name();
        this.content = mailMessageDTO.content();
        this.read = mailMessageDTO.is_read() != null && mailMessageDTO.is_read();
        this.createdAt = mailMessageDTO.created_at();
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getUniqueId() {
        return this.uniqueId;
    }

    public UUID getSenderId() {
        return this.senderId;
    }

    public String getSenderName() {
        return this.senderName;
    }

    public String getContent() {
        return this.content;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public boolean isRead() {
        return this.read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
