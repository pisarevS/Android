package pisarev.com.modeling.mvp.model;

import java.util.*;

public class Frame {
    private int id;
    private float x;
    private float z;
    private float cr;
    private float rnd;
    private boolean isCR = false;
    private boolean isRND = false;
    private boolean isAxisContains = false;
    private List<String> gCode = new ArrayList<>();

    public boolean isAxisContains() {
        return isAxisContains;
    }

    public float getRnd() {
        return rnd;
    }

    public void setRnd(float rnd) {
        this.rnd = rnd;
    }

    public boolean isRND() {
        return isRND;
    }

    public void setRND(boolean RND) {
        isRND = RND;
    }

    public void setAxisContains(boolean axisContains) {
        isAxisContains = axisContains;
    }

    public void setGCode(List<String> gCode) {
        this.gCode = gCode;
    }

    public List<String> getGCode() {
        return gCode;
    }

    public void setIsCR(boolean isCR) {
        this.isCR = isCR;
    }

    public boolean getIsCR() {
        return isCR;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getX() {
        return x;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getZ() {
        return z;
    }

    public void setCr(float cr) {
        this.cr = cr;
    }

    public float getCr() {
        return cr;
    }

    @Override
    public String toString() {
        String frame = id + " " + gCode + " " + x + " " + z + " " + cr;
        return frame;
    }
}
