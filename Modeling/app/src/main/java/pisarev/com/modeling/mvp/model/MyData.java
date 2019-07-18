package pisarev.com.modeling.mvp.model;

import java.util.ArrayList;

public class MyData  {




    private ArrayList<String>programListTextView=new ArrayList<>(  );

    private ArrayList<String>errorList=new ArrayList<>();

    public ArrayList<Frame> getFrameList() {
        return frameList;
    }

    public void setFrameList(ArrayList<Frame> frameList) {
        this.frameList = frameList;
    }

    private ArrayList<Frame> frameList=new ArrayList<>(  );

    public MyData(){
    }


    public ArrayList<String> getProgramListTextView() {
        return programListTextView;
    }

    public void setProgramListTextView(ArrayList<String> programListTextView) {
        this.programListTextView = programListTextView;
    }

    public ArrayList<String> getErrorList() {
        return errorList;
    }

    public void setErrorList(String error) {
        this.errorList.add(error);
    }
}
