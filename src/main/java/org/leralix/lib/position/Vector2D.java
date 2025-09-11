package org.leralix.lib.position;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Objects;
import java.util.UUID;

public class Vector2D {
    protected int x;
    protected int z;
    protected String worldID;

    public Vector2D(int x, int z, String worldID) {
        this.x = x;
        this.z = z;
        this.worldID = worldID;
    }

    public Vector2D(Location location) {
        this.x = location.getBlockX();
        this.z = location.getBlockZ();
        this.worldID = location.getWorld().getUID().toString();
    }

    public Vector2D(Chunk chunk) {
        this.x = chunk.getX();
        this.z = chunk.getZ();
        this.worldID = chunk.getWorld().getUID().toString();
    }

    public Vector2D(Vector3D vector) {
        this.x = vector.getX();
        this.z = vector.getZ();
        this.worldID = vector.getWorldID().toString();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public UUID getWorldID() {
        return UUID.fromString(worldID);
    }

    public World getWorld() {
        return Bukkit.getWorld(getWorldID());
    }

    public double getDistance(Vector2D other) {
        return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(z - other.z, 2));
    }

    /**
     * Compute a 2 dimension area between two Vector3D
     * @param other The second Vector3D
     * @return A 2 dimension area between two Vector3D
     */
    public int getArea(Vector2D other){
        int lineX = Math.abs(x - other.getX());
        int lineZ = Math.abs(z - other.getZ());
        return lineX * lineZ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2D vector3D = (Vector2D) o;
        return x == vector3D.x && z == vector3D.z && Objects.equals(worldID, vector3D.worldID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z, worldID);
    }


    @Override
    public String toString() {
        return "Vector2D{" +
                "x=" + x +
                ", z=" + z +
                ", worldID='" + worldID + "'}";
    }
}
