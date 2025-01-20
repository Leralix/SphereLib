package org.leralix.lib.position;

import org.bukkit.Location;

import java.util.Objects;

public class Vector3D extends Vector2D {
    private int y;

    public Vector3D(int x, int y, int z, String worldID) {
        super(x, z, worldID);
        this.y = y;
    }

    public Vector3D(Location location) {
        super(location.getBlockX(), location.getBlockZ(), location.getWorld().getUID().toString());
        this.y = location.getBlockY();
    }


    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }


    public Location getLocation(){
        return new Location(getWorld(), getX(), getY(), getZ());
    }
    @Override
    public String toString(){
        return "[" + this.x + ", " + this.y + ", " + this.z + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector3D vector3D = (Vector3D) o;
        return x == vector3D.x && y == vector3D.y && z == vector3D.z && Objects.equals(worldID, vector3D.worldID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, worldID);
    }
}
