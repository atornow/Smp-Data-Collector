package truesmp.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseManager {

    private HikariDataSource dataSource;

    public DatabaseManager(String url, String username, String password) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            // Consider logging this error or even throwing a runtime exception, because if the driver can't be loaded, the rest of the plugin won't function correctly.
        }
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(config);
    }

    public Connection connect() throws SQLException {
        return dataSource.getConnection();
    }

    public void insert(PlayerData data) {
        String sql = "INSERT INTO player_data(playerUUID, timestamp, x, y, z, pitch, yaw, blockID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, data.getPlayerUUID());
            pstmt.setTimestamp(2, java.sql.Timestamp.valueOf(data.getTimestamp()));
            pstmt.setDouble(3, data.getX());
            pstmt.setDouble(4, data.getY());
            pstmt.setDouble(5, data.getZ());
            pstmt.setFloat(6, data.getPitch());
            pstmt.setFloat(7, data.getYaw());
            pstmt.setInt(8, data.getBlockID());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void batchInsert(List<PlayerData> dataList) {
        String sql = "INSERT INTO player_data(playerUUID, timestamp, x, y, z, pitch, yaw, blockID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (PlayerData data : dataList) {
                pstmt.setObject(1, data.getPlayerUUID());
                pstmt.setTimestamp(2, java.sql.Timestamp.valueOf(data.getTimestamp()));
                pstmt.setDouble(3, data.getX());
                pstmt.setDouble(4, data.getY());
                pstmt.setDouble(5, data.getZ());
                pstmt.setFloat(6, data.getPitch());
                pstmt.setFloat(7, data.getYaw());
                pstmt.setInt(8, data.getBlockID());
                pstmt.addBatch();
            }

            pstmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
