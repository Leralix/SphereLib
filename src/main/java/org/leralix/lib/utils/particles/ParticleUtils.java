package org.leralix.lib.utils.particles;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.leralix.lib.position.Vector3D;

/**
 * This class is used to manage particles in the plugin.
 */
public class ParticleUtils {

    private ParticleUtils() {
        throw new IllegalStateException("Utility class");
    }

        /**
         * Draw a rectangular box between 2 points for a certain amount of time.
         * @param player    The player to show the box to
         * @param point1    The first point of the box
         * @param point2    The second point of the box
         * @param seconds   The amount of time to show the box
         * @param particle  The type of particle shown
         */
    public static void drawBox(Plugin plugin, Player player, Vector3D point1, Vector3D point2, int seconds, Particle particle){
        ParticleTask particleTask = new ParticleTask(player, seconds, () -> drawBox(player, point1, point2, particle));
        particleTask.runTaskTimer(plugin, 0, 20);
    }

    /**
     * Draw a line between 2 points for a certain amount of time.
     * @param player    The player to show the box to
     * @param point1    The first point of the box
     * @param point2    The second point of the box
     * @param seconds   The amount of time to show the box
     * @param particle  The type of particle shown
     */
    public static void drawLine(Plugin plugin, Player player, Vector3D point1, Vector3D point2, int seconds, Particle particle){
        ParticleTask particleTask = new ParticleTask(player, seconds, () -> drawLine(player, point1, point2, particle));
        particleTask.runTaskTimer(plugin, 0, 20);
    }


    /**
     * Draw a line between 2 points for a certain amount of time.
     * If the two points shares a common coordinate (min = max), only 4 lines will be created instead of 12.
     * @param player    The player to show the box to
     * @param point1    The first point of the box
     * @param point2    The second point of the box
     * @param seconds   The amount of time to show the box
     * @param particle  The type of particle shown
     */
    public static void drawPane(Plugin plugin, Player player, Vector3D point1, Vector3D point2, int seconds, Particle particle){
        ParticleTask particleTask = new ParticleTask(player, seconds, () -> drawPane(player, point1, point2, particle));
        particleTask.runTaskTimer(plugin, 0, 20);
    }

    private static void drawPane(Player player, Vector3D point1, Vector3D point2,Particle particle){

        double minX = Math.min(point1.getX(), point2.getX());
        double minY = Math.min(point1.getY(), point2.getY());
        double minZ = Math.min(point1.getZ(), point2.getZ());
        double maxX = Math.max(point1.getX(), point2.getX());
        double maxY = Math.max(point1.getY(), point2.getY());
        double maxZ = Math.max(point1.getZ(), point2.getZ());

        if(minX == maxX){
            // Draw bottom edges
            drawLine(player, minX, minY, maxZ, minX, minY, minZ, particle); // Bottom left

            // Draw top edges
            drawLine(player, minX, maxY, maxZ, minX, maxY, minZ, particle); // Top left

            // Draw vertical edges
            drawLine(player, minX, minY, minZ, minX, maxY, minZ, particle); // Left
            drawLine(player, minX, minY, maxZ, minX, maxY, maxZ, particle); // Back left
            return;
        }

        if(minZ == maxZ){
            // Draw bottom edges
            drawLine(player, minX, minY, minZ, maxX, minY, minZ, particle); // Bottom front

            // Draw top edges
            drawLine(player, minX, maxY, minZ, maxX, maxY, minZ, particle); // Top front

            // Draw vertical edges
            drawLine(player, minX, minY, minZ, minX, maxY, minZ, particle); // Left
            drawLine(player, maxX, minY, minZ, maxX, maxY, minZ, particle); // Right
            return;
        }
        drawBox(player, point1, point2, particle);
    }


    /**
     * Optimized drawing of a box between two points.
     * @param player    The player to show the box to
     * @param point1    The first point of the box
     * @param point2    The second point of the box
     */
    private static void drawBox(Player player, Vector3D point1, Vector3D point2, Particle particle) {
        double minX = Math.min(point1.getX(), point2.getX());
        double minY = Math.min(point1.getY(), point2.getY());
        double minZ = Math.min(point1.getZ(), point2.getZ());
        double maxX = Math.max(point1.getX(), point2.getX()) + 1.;
        double maxY = Math.max(point1.getY(), point2.getY()) + 1.;
        double maxZ = Math.max(point1.getZ(), point2.getZ()) + 1.;

        // Draw bottom edges
        drawLine(player, minX, minY, minZ, maxX, minY, minZ, particle); // Bottom front
        drawLine(player, maxX, minY, minZ, maxX, minY, maxZ, particle); // Bottom right
        drawLine(player, maxX, minY, maxZ, minX, minY, maxZ, particle); // Bottom back
        drawLine(player, minX, minY, maxZ, minX, minY, minZ, particle); // Bottom left

        // Draw top edges
        drawLine(player, minX, maxY, minZ, maxX, maxY, minZ, particle); // Top front
        drawLine(player, maxX, maxY, minZ, maxX, maxY, maxZ, particle); // Top right
        drawLine(player, maxX, maxY, maxZ, minX, maxY, maxZ, particle); // Top back
        drawLine(player, minX, maxY, maxZ, minX, maxY, minZ, particle); // Top left

        // Draw vertical edges
        drawLine(player, minX, minY, minZ, minX, maxY, minZ, particle); // Left
        drawLine(player, maxX, minY, minZ, maxX, maxY, minZ, particle); // Right
        drawLine(player, maxX, minY, maxZ, maxX, maxY, maxZ, particle); // Back right
        drawLine(player, minX, minY, maxZ, minX, maxY, maxZ, particle); // Back left
    }

    /**
     * Optimized drawing of a line between two points.
     * @param player    The player to show the line to
     * @param point1    The first point of the line
     * @param point2    The second point of the line
     * @param particle  The type of particle shown
     */
    private static void drawLine(Player player, Vector3D point1, Vector3D point2, Particle particle) {
        drawLine(player, point1.getX(), point1.getY(), point1.getZ(), point2.getX(), point2.getY(), point2.getZ(), particle);
    }

    /**
     * Optimized method to draw a line between two points.
     * @param player    The player to show the line to
     * @param x1        The x coordinate of the first point
     * @param y1        The y coordinate of the first point
     * @param z1        The z coordinate of the first point
     * @param x2        The x coordinate of the second point
     * @param y2        The y coordinate of the second point
     * @param z2        The z coordinate of the second point
     * @param particle  The type of particle shown
     */
    private static void drawLine(Player player, double x1, double y1, double z1, double x2, double y2, double z2, Particle particle) {
        double length = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) + Math.pow(z2 - z1, 2));
        int amount = (int) (length * 2);  // Increased density of particles

        double dx = (x2 - x1) / amount;
        double dy = (y2 - y1) / amount;
        double dz = (z2 - z1) / amount;

        for (int i = 0; i <= amount; i++) {
            Location loc = new Location(player.getWorld(), x1 + dx * i, y1 + dy * i, z1 + dz * i);
            player.spawnParticle(particle, loc, 0, 0, 0, 0, 1);
        }
    }

    private enum Type {
        BOX,
        LINE
    }
}
