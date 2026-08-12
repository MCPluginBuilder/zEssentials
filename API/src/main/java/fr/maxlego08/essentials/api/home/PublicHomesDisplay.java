package fr.maxlego08.essentials.api.home;

/**
 * Controls how the /publichomes command displays the list of public homes.
 */
public enum PublicHomesDisplay {

    /**
     * Displays the public homes as a clickable chat message.
     */
    CHAT,

    /**
     * Displays the public homes in a paginated inventory (GUI).
     */
    INVENTORY,
}
