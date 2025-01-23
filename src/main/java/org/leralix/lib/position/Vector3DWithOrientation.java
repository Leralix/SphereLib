package org.leralix.lib.position;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;

public class Vector3DWithOrientation extends Vector3D{

    private float pitch;
    private float yaw;


    public Vector3DWithOrientation(int x, int y, int z, String worldID, float pitch, float yaw) {
        super(x, y, z, worldID);
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public Vector3DWithOrientation(Location location){
        super(location);
        this.pitch = location.getPitch();
        this.yaw = location.getYaw();
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

    public Location getLocation(){
        return new Location(Bukkit.getWorld(UUID.fromString(super.worldID)), x, y, z, yaw, pitch);
    }

}
