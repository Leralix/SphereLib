package org.leralix.lib.position;

import org.bukkit.Location;

import java.util.Objects;

public class DoubleVector3D extends DoubleVector2D {
    protected double y;

    public DoubleVector3D(double x, double y, double z, String worldID) {
        super(x, z, worldID);
        this.y = y;
    }

    public DoubleVector3D(Location location) {
        super(location.getBlockX(), location.getBlockZ(), location.getWorld().getUID().toString());
        this.y = location.getBlockY();
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getDistance(DoubleVector3D other) {
        return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(z - other.z, 2) + Math.pow(y - other.y, 2));
    }

    /**
     * Compute a 3 dimension area between two Vector3D
     * @param other The second Vector3D
     * @return A 3 dimension area between two Vector3D
     */
    public double getArea(DoubleVector3D other){
        double lineX = Math.abs(x - other.getX());
        double lineY = Math.abs(y - other.getY());
        double lineZ = Math.abs(z - other.getZ());
        return lineX * lineY * lineZ;
    }

    public Location getLocation() {
        return new Location(getWorld(), getX(), getY(), getZ());
    }

    public Vector3D get3DVector(){
        return new Vector3D(get2DVector(), (int) y);
    }

    @Override
    public String toString() {
        return "[" + this.x + ", " + this.y + ", " + this.z + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoubleVector3D vector3D = (DoubleVector3D) o;
        return x == vector3D.x && y == vector3D.y && z == vector3D.z && Objects.equals(worldID, vector3D.worldID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, worldID);
    }
}
