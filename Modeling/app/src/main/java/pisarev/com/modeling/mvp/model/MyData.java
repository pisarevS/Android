package pisarev.com.modeling.mvp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyData {


    private ArrayList<String> programList = new ArrayList<>();

    private Map<Integer, String> errorListMap = new HashMap<>();

    private ArrayList<Frame> frameList = new ArrayList<>();

    public MyData() {
    }

    public Map<Integer, String> getErrorListMap() {
        return errorListMap;
    }

    public void setErrorListMap(Map<Integer, String> errorListMap) {
        this.errorListMap = errorListMap;
    }

    public ArrayList<Frame> getFrameList() {
        return frameList;
    }

    public void setFrameList(ArrayList<Frame> frameList) {
        this.frameList = frameList;
    }

    public ArrayList<String> getProgramList() {
        return programList;
    }

    public void setProgramList(ArrayList<String> programList) {
        this.programList = programList;
    }
}

