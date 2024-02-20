package truesmp.data;
import java.util.UUID;
import java.time.LocalDateTime;

public class PlayerData {

    private UUID playerUUID;
    private LocalDateTime timestamp;
    private double x, y, z;
    private float pitch, yaw;
    private int blockID;

    public PlayerData(UUID playerUUID, LocalDateTime timestamp,
                      double x, double y, double z,
                      float pitch, float yaw, int blockID) {
        this.playerUUID = playerUUID;
        this.timestamp = timestamp;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
        this.blockID = blockID;
    }

    // Getters and Setters

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public int getBlockID() {
        return blockID;
    }

    public void setBlockID(int blockID) {
        this.blockID = blockID;
    }

    @Override
    public String toString() {
        return "PlayerData{" +
                "playerUUID=" + playerUUID +
                ", timestamp=" + timestamp +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", pitch=" + pitch +
                ", yaw=" + yaw +
                ", blockID=" + blockID +
                '}';
    }
}

