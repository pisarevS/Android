package pisarev.com.modeling.mvp.model;

import java.util.ArrayList;

public class MyData  {


    private ArrayList<StringBuffer>programList=new ArrayList<>(  );

    private ArrayList<String>programListTextView=new ArrayList<>(  );

    private ArrayList<String>errorList=new ArrayList<>();

    public MyData(){
    }

    public ArrayList<StringBuffer> getProgramList() {
        return programList;
    }

    public void setProgramList(ArrayList<StringBuffer> programList) {
        this.programList = programList;
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
