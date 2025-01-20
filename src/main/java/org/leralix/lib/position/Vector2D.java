package org.leralix.lib.position;

import org.bukkit.Bukkit;
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

    public UUID getWorldID(){
        return UUID.fromString(worldID);
    }
    public World getWorld(){
        return Bukkit.getWorld(getWorldID());
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

}
