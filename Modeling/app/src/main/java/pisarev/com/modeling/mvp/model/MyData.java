package pisarev.com.modeling.mvp.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class MyData  {

    private String program="";
    private String parameter="";

    private ArrayList<String>programList=new ArrayList<>(  );

    private ArrayList<String>parameterList=new ArrayList<>(  );
    public MyData(){
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public ArrayList<String> getProgramList() {
        return programList;
    }

    public ArrayList<String> getParameterList() {
        return parameterList;
    }

}
