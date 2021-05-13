package pisarev.com.modeling.mvp.model;

public class Point {

    protected float x = Constant.N_GANTRYPOS_X;

    protected float z = Constant.N_GANTRYPOS_Z;

    public Point() {

    }

    public Point(Point point){
        this.x= point.getX();
        this.z= point.getZ();
    }

    public Point(float x, float z) {
        this.x = x;
        this.z = z;
    }

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
}
