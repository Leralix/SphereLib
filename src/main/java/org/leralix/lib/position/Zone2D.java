package org.leralix.lib.position;

public class Zone2D {

    private final Vector2D min;
    private final Vector2D max;

    public Zone2D(Vector2D min, Vector2D max) {
        this.min = min;
        this.max = max;
    }

    public Vector2D getMin() {
        return min;
    }

    public Vector2D getMax() {
        return max;
    }
}
