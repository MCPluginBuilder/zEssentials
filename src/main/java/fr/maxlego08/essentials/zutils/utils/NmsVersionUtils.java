package fr.maxlego08.essentials.zutils.utils;

import fr.maxlego08.menu.api.utils.version.MinecraftVersion;

/**
 * Helper resolving the version-specific NMS sub-package name from the running server version.
 * <p>
 * Since zMenu dropped the legacy {@code NmsVersion} enum (which could not represent the year-based
 * Minecraft versioning introduced in 26.x) in favor of {@link MinecraftVersion}, the NMS package is
 * now derived from the parsed major/minor/patch components.
 */
public final class NmsVersionUtils {

    private NmsVersionUtils() {
    }

    /**
     * Derives the version-specific NMS sub-package name (e.g. {@code v1_21_11} or {@code v26_2}) for
     * the running server, matching the module package layout under {@code fr.maxlego08.essentials.nms}.
     * The patch component is omitted when it is {@code 0} (e.g. {@code 1.21} -&gt; {@code v1_21},
     * {@code 26.2} -&gt; {@code v26_2}).
     *
     * @return the NMS package suffix for the current Minecraft version
     */
    public static String getNmsPackage() {
        MinecraftVersion version = MinecraftVersion.getCurrentVersion();
        StringBuilder builder = new StringBuilder("v").append(version.getMajor()).append("_").append(version.getMinor());
        if (version.getPatch() > 0) {
            builder.append("_").append(version.getPatch());
        }
        return builder.toString();
    }
}
