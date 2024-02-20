package truesmp.data;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.List;

public class Main extends JavaPlugin implements Listener {

    private PlayerDataCollector dataCollector;
    private DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        // Saves the default config.yml from your JAR to the plugin's data folder if it doesn't exist
        // Initialization
        this.dataCollector = new PlayerDataCollector();

        // Config values should ideally come from a config.yml or similar configuration
        // For the purpose of this example, they are hardcoded
        String dbURL = getConfig().getString("database.url");
        String dbUsername = getConfig().getString("database.username");
        String dbPassword = getConfig().getString("database.password");
        this.databaseManager = new DatabaseManager(dbURL, dbUsername, dbPassword);

        // Schedule the task to run every second (20 ticks in Minecraft time)
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    List<PlayerData> dataList = dataCollector.collectData();
                    databaseManager.batchInsert(dataList);
                } catch (Exception e) {
                    e.printStackTrace(); // In a real setup, consider more advanced error handling and logging
                }
            }
        }.runTaskTimer(this, 0L, 20L); // Starts immediately and repeats every 20 ticks
        getServer().getPluginManager().registerEvents(this, this);
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        int blockID = event.getBlock().getType().ordinal(); // Simplistic block ID based on type enumeration order
        dataCollector.onBlockMined(player.getUniqueId(), blockID);
    }

    @Override
    public void onDisable() {
        // Any cleanup operations if necessary
        // E.g., if you were to add a connection pool in DatabaseManager, close it here.
        databaseManager.closePool();
    }

    // Other possible plugin methods and event listeners can be added below
}

