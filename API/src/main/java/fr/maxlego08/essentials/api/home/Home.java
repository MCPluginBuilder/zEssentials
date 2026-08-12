package fr.maxlego08.essentials.api.home;

import org.bukkit.Location;
import org.bukkit.Material;

/**
 * Represents a home location in the plugin system.
 */
public interface Home {

    /**
     * Gets the location of the home.
     *
     * @return The location of the home.
     */
    Location getLocation();

    /**
     * Gets the name of the home.
     *
     * @return The name of the home.
     */
    String getName();

    /**
     * Gets the material associated with the home.
     *
     * @return The material associated with the home.
     */
    Material getMaterial();

    /**
     * Sets the material associated with the home.
     *
     * @param material The material to set for the home.
     */
    void setMaterial(Material material);

    /**
     * Whether this home is public (visitable by any player).
     *
     * @return true if the home is public.
     */
    boolean isPublic();

    /**
     * Sets whether this home is public.
     *
     * @param isPublic true to make the home public.
     */
    void setPublic(boolean isPublic);

    /**
     * Gets the category of this home, or null if none.
     *
     * @return the category name, or null.
     */
    String getCategory();

    /**
     * Sets the category of this home (null to clear).
     *
     * @param category the category name, or null.
     */
    void setCategory(String category);

    /**
     * Whether this home is marked as a favorite.
     *
     * @return true if the home is a favorite.
     */
    boolean isFavorite();

    /**
     * Sets whether this home is a favorite.
     *
     * @param favorite true to mark the home as a favorite.
     */
    void setFavorite(boolean favorite);

}

