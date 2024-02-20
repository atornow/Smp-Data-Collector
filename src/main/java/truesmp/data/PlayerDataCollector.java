package truesmp.data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import java.time.LocalDateTime;
import java.util.*;

public class PlayerDataCollector {

    private Map<UUID, List<TimedBlock>> recentBlocksMined = new HashMap<>();

    private static class TimedBlock {
        final int blockID;
        final long timestamp;

        TimedBlock(int blockID, long timestamp) {
            this.blockID = blockID;
            this.timestamp = timestamp;
        }
    }


    /**
     * Collects data for all online players.
     * @return List of PlayerData objects for online players.
     */
    public List<PlayerData> collectData() {
        List<PlayerData> dataList = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            Location location = player.getLocation();
            dataList.add(new PlayerData(
                    player.getUniqueId(),
                    LocalDateTime.now(),
                    location.getX(),
                    location.getY(),
                    location.getZ(),
                    location.getPitch(),
                    location.getYaw(),
                    getBlockModeMined(player.getUniqueId())
            ));
        }
        return dataList;
    }

    /**
     * (Simplified) Returns the mode of blocks mined by a player in the last second.
     * This method and its mechanism can be further optimized.
     */
    public int getBlockModeMined(UUID playerUUID) {
        List<TimedBlock> blocks = recentBlocksMined.get(playerUUID);
        if (blocks == null || blocks.isEmpty()) {
            return 0;
        }

        long oneSecondAgo = System.currentTimeMillis() - 1000;
        blocks.removeIf(block -> block.timestamp < oneSecondAgo);

        // If blocks is empty after removal
        if (blocks.isEmpty()) {
            return 0;
        }

        // Determine mode of the block IDs
        Map<Integer, Integer> blockCounts = new HashMap<>();
        for (TimedBlock block : blocks) {
            blockCounts.put(block.blockID, blockCounts.getOrDefault(block.blockID, 0) + 1);
        }

        return blockCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(0);
    }




    /**
     * (Placeholder) This would be an event listener for when a player mines a block.
     * You'd update your data structures accordingly.
     * Note: This is a simplified representation.
     */
    public void onBlockMined(UUID playerUUID, int blockID) {
        List<TimedBlock> blocks = recentBlocksMined.computeIfAbsent(playerUUID, k -> new ArrayList<>());
        blocks.add(new TimedBlock(blockID, System.currentTimeMillis()));
    }


}
