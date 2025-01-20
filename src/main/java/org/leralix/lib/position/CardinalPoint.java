package org.leralix.lib.position;

public enum CardinalPoint {

    NORTH,
    EAST,
    SOUTH,
    WEST;

    public static CardinalPoint getCardinalPoint(float yaw) {
        if (yaw >= 45 && yaw < 135) {
            return EAST;
        } else if (yaw >= 135 && yaw < 225) {
            return SOUTH;
        } else if (yaw >= 225 && yaw < 315) {
            return WEST;
        } else {
            return NORTH;
        }
    }

}
