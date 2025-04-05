package org.leralix.lib.position;

public enum CardinalPoint {

    NORTH,
    EAST,
    SOUTH,
    WEST;

    public static CardinalPoint getCardinalPoint(float yaw) {

        if(yaw <= 135 && yaw > 45){
            return WEST;
        }
        if(yaw <=45 && yaw > -45){
            return SOUTH;
        }
        if(yaw <= -45 && yaw > -135){
            return EAST;
        }
        return NORTH;
    }


}
