package pisarev.com.modeling.mvp.model;

import java.util.ArrayList;

public class MyData  {


    public void setProgramList(ArrayList<StringBuffer> programList) {
        this.programList = programList;
    }

    private ArrayList<StringBuffer>programList=new ArrayList<>(  );

    public MyData(){
    }

    public ArrayList<StringBuffer> getProgramList() {
        return programList;
    }


}
