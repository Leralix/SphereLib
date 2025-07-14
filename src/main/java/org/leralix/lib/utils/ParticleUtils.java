package org.leralix.lib.utils;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
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
     * @param seconds   The amount of time to show the box for
     */
    public static void showBox(Plugin plugin, Player player, Vector3D point1, Vector3D point2, int seconds){
        ParticleTask particleTask = new ParticleTask(player, point1, point2, seconds, Type.BOX);
        particleTask.runTaskTimer(plugin, 0, 20);
    }

    public static void drawLine(Plugin plugin, Player player, Vector3D point1, Vector3D point2, int seconds){
        ParticleTask particleTask = new ParticleTask(player, point1, point2, seconds, Type.LINE);
        particleTask.runTaskTimer(plugin, 0, 20);
    }

    /**
     * This class is used to draw a box from particles between 2 points for a certain amount of time.
     */
    private static class ParticleTask extends BukkitRunnable {
        private final Player player;
        private final Vector3D p1;
        private final Vector3D p2;
        private int secondsLeft;
        private final Type type;

        protected ParticleTask(Player player, Vector3D p1, Vector3D p2, int duration, Type type) {
            this.player = player;
            this.p1 = p1;
            this.p2 = p2;
            this.secondsLeft = duration;
            this.type = type;
        }
        @Override
        public void run() {
            if (player != null && secondsLeft > 0) {
                switch (type){
                    case BOX -> ParticleUtils.drawBox(player, this.p1, this.p2);
                    case LINE -> ParticleUtils.drawLine(player, this.p1, this.p2);
                }

                secondsLeft--;
                if (secondsLeft == 0) {
                    cancel();
                }
            } else {
                cancel();
            }
        }
    }

    /**
     * Draw a box between 2 points.
     * @param player    The player to show the box to
     * @param point1    The first point of the box
     * @param point2    The second point of the box
     */
    private static void drawBox(Player player, Vector3D point1, Vector3D point2) {
        double minX = Math.min(point1.getX(), point2.getX());
        double minY = Math.min(point1.getY(), point2.getY());
        double minZ = Math.min(point1.getZ(), point2.getZ());
        double maxX = Math.max(point1.getX(), point2.getX()) + 1;
        double maxY = Math.max(point1.getY(), point2.getY()) + 1;
        double maxZ = Math.max(point1.getZ(), point2.getZ()) + 1;

        // Draw bottom edges
        drawLine(player, minX, minY, minZ, maxX, minY, minZ);
        drawLine(player, maxX, minY, minZ, maxX, minY, maxZ);
        drawLine(player, maxX, minY, maxZ, minX, minY, maxZ);
        drawLine(player, minX, minY, maxZ, minX, minY, minZ);

        // Draw top edges
        drawLine(player, minX, maxY, minZ, maxX, maxY, minZ);
        drawLine(player, maxX, maxY, minZ, maxX, maxY, maxZ);
        drawLine(player, maxX, maxY, maxZ, minX, maxY, maxZ);
        drawLine(player, minX, maxY, maxZ, minX, maxY, minZ);

        // Draw vertical edges
        drawLine(player, minX, minY, minZ, minX, maxY, minZ);
        drawLine(player, maxX, minY, minZ, maxX, maxY, minZ);
        drawLine(player, maxX, minY, maxZ, maxX, maxY, maxZ);
        drawLine(player, minX, minY, maxZ, minX, maxY, maxZ);
    }

    private static void drawLine(Player player, Vector3D point1, Vector3D point2){
        drawLine(player, point1.getX(), point1.getY(), point1.getZ(), point2.getX(), point2.getY(), point2.getZ());
    }

    /**
     * Draw a line between 2 points.
     * @param player    The player to show the line to
     * @param x1        The x coordinate of the first point
     * @param y1        The y coordinate of the first point
     * @param z1        The z coordinate of the first point
     * @param x2        The x coordinate of the second point
     * @param y2        The y coordinate of the second point
     * @param z2        The z coordinate of the second point
     */
    private static void drawLine(Player player, double x1, double y1, double z1, double x2, double y2, double z2) {
        double length = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) + Math.pow(z2 - z1, 2));
        double amount = length * 2;

        double dx = (x2 - x1) / amount;
        double dy = (y2 - y1) / amount;
        double dz = (z2 - z1) / amount;

        for (int i = 0; i < amount; i++) {
            Location loc = new Location(player.getWorld(), x1 + dx * i, y1 + dy * i, z1 + dz * i);
            player.spawnParticle(Particle.DRAGON_BREATH, loc, 0, 0, 0, 0, 1, null);
        }
    }

    private enum Type{
        BOX,
        LINE
    }
}
