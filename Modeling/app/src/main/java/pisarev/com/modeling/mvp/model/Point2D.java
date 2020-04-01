package pisarev.com.modeling.mvp.model;

public class Point2D {

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    private float x;
    private float z;

    public Point2D(float x, float z) {
        this.x=x;
        this.z=z;
    }

    public float distance(float x, float z) {
        float a = getX() - x;
        float b = getZ() - z;
        return (float) Math.sqrt(a * a + b * b);
    }

    public float angle(float x, float y) {
        final float ax = getX();
        final float ay = getZ();

        final float delta = (float) ((ax * x + ay * y) / Math.sqrt(
                        (ax * ax + ay * ay) * (x * x + y * y)));

        if (delta > 1.0) {
            return 0.0f;
        }
        if (delta < -1.0) {
            return 180.0f;
        }

        return (float) Math.toDegrees(Math.acos(delta));
    }
}
