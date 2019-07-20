package pisarev.com.modeling.mvp.model;

import java.util.*;
public class Frame
{
    private int id;
    private float x;
    private float z;
    private float cr;
    private boolean isCR=false;
    private boolean isAxisContains=false;
    private ArrayList<String> gCode=new ArrayList<>();

    public boolean isAxisContains() {
        return isAxisContains;
    }

    public void setAxisContains(boolean axisContains) {
        isAxisContains = axisContains;
    }

    public void setGCode(ArrayList<String> gCode)
    {
        this.gCode = gCode;
    }

    public ArrayList<String> getGCode()
    {
        return gCode;
    }

    public void setIsCR(boolean isCR)
    {
        this.isCR = isCR;
    }

    public boolean getIsCR()
    {
        return isCR;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public void setX(float x)
    {
        this.x = x;
    }

    public float getX()
    {
        return x;
    }

    public void setZ(float z)
    {
        this.z = z;
    }

    public float getZ()
    {
        return z;
    }

    public void setCr(float cr)
    {
        this.cr = cr;
    }

    public float getCr()
    {
        return cr;
    }
}
