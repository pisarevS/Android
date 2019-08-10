package pisarev.com.modeling.mvp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MyData {


    private ArrayList<StringBuffer> programList = new ArrayList<>();

    private Map<Integer, String> errorListMap = new LinkedHashMap<>();

    private ArrayList<Frame> frameList = new ArrayList<>();

    private String pathProgram;

    public MyData() {
    }

    public String getPathProgram() {
        return pathProgram;
    }

    public void setPathProgram(String pathProgram) {
        this.pathProgram = pathProgram;
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

    public ArrayList<StringBuffer> getProgramList() {
        return programList;
    }

    public void setProgramList(ArrayList<StringBuffer> programList) {
        this.programList = programList;
    }
}

