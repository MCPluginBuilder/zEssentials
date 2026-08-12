package fr.maxlego08.essentials.user;

import fr.maxlego08.essentials.api.home.Home;
import fr.maxlego08.essentials.api.utils.SafeLocation;
import org.bukkit.Location;
import org.bukkit.Material;

public class ZHome implements Home {

    private final SafeLocation location;
    private final String name;
    private Material material;
    private boolean isPublic;
    private String category;
    private boolean favorite;

    public ZHome(SafeLocation location, String name, Material material) {
        this(location, name, material, false, null, false);
    }

    public ZHome(SafeLocation location, String name, Material material, boolean isPublic, String category, boolean favorite) {
        this.location = location;
        this.name = name;
        this.material = material;
        this.isPublic = isPublic;
        this.category = category;
        this.favorite = favorite;
    }

    @Override
    public Location getLocation() {
        return location.getLocation();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Material getMaterial() {
        return this.material;
    }

    @Override
    public void setMaterial(Material material) {
        this.material = material;
    }

    @Override
    public boolean isPublic() {
        return this.isPublic;
    }

    @Override
    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    @Override
    public String getCategory() {
        return this.category;
    }

    @Override
    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public boolean isFavorite() {
        return this.favorite;
    }

    @Override
    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
