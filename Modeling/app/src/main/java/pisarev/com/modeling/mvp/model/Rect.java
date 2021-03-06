package pisarev.com.modeling.mvp.model;

public class Rect extends Point {
    double height;
    double width;

    public Rect(){}

    public Rect(Point point, float height, float width) {
        x = point.getX();
        z = point.getZ();
        this.height = height;
        this.width = width;
    }

    public void setRect(Point point, float height, float width){
        x = point.getX();
        z = point.getZ();
        this.height = height;
        this.width = width;
    }

    public void setRect(float x,float z, float height, float width){
        this.x = x;
        this.z = z;
        this.height = height;
        this.width = width;
    }

    public boolean isInsideRect(float x, float z) {
        return (x >= this.x && x <= this.x + width) && (z >= this.z && z <= this.z + height);
    }
}

