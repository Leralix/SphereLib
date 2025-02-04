package org.leralix.lib.position;

public class Zone3D {

    private final Vector3D min;
    private final Vector3D max;

    public Zone3D(Vector3D min, Vector3D max) {
        this.min = min;
        this.max = max;
    }

    public Vector3D getMin() {
        return min;
    }

    public Vector3D getMax() {
        return max;
    }
}
