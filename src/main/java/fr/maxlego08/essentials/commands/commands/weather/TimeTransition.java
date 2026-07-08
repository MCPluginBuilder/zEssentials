package fr.maxlego08.essentials.commands.commands.weather;

import com.tcoded.folialib.wrapper.task.WrappedTask;
import fr.maxlego08.essentials.api.EssentialsPlugin;
import org.bukkit.World;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Utility class for smooth time transitions in worlds and for players
 */
class TimeTransition {

    static final Set<UUID> TIME_CHANGING_WORLDS = ConcurrentHashMap.newKeySet();
    private static final long WORLD_TIME_STEP = 150;

    /**
     * Smoothly transitions world time to target time
     */
    static void transitionWorldTime(EssentialsPlugin plugin, World world, long targetTime) {
        UUID worldId = world.getUID();
        TIME_CHANGING_WORLDS.add(worldId);

        plugin.getScheduler().runTimer(new Consumer<>() {
            long progressed = 0;
            long diff = -1;

            @Override
            public void accept(WrappedTask wrappedTask) {
                if (diff == -1) {
                    long startTime = world.getFullTime();
                    diff = (targetTime - startTime + 24000) % 24000;
                }

                if (progressed >= diff) {
                    world.setFullTime(targetTime);
                    TIME_CHANGING_WORLDS.remove(worldId);
                    wrappedTask.cancel();
                    return;
                }

                world.setFullTime(world.getFullTime() + WORLD_TIME_STEP);
                progressed += WORLD_TIME_STEP;
            }
        }, 1L, 1L);
    }

    /**
     * Checks if world time is currently being changed
     */
    static boolean isWorldTimeChanging(UUID worldId) {
        return TIME_CHANGING_WORLDS.contains(worldId);
    }
}
