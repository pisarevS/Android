package pisarev.com.modeling.mvp.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import pisarev.com.modeling.application.App;

public class ChangeVariables implements Runnable {

    private String program;
    private String parameter;
    @Inject
    MyData data;

    public ChangeVariables(String program,String parameter){
        this.program=program;
        this.parameter=parameter;
        App.getComponent().inject( this );
    }

    @Override
    public void run() {
        getProgramList();
        getParameter();

    }

    private void getProgramList(){
        data.getProgramList().clear();
        try {
            BufferedReader br = new BufferedReader( new StringReader(program) );
            String line;
            while ((line = br.readLine()) != null) {
                data.getProgramList().add(line  );
            }
            br.close();
        }catch (IOException e){

        }
    }

    private void getParameter(){
        data.getParameterList().clear();
        try {
            BufferedReader br = new BufferedReader( new StringReader(parameter) );
            String line;
            while ((line = br.readLine()) != null) {
                data.getParameterList().add(line  );
            }
            br.close();
        }catch (IOException e){

        }
    }

    private void change(){
        Map<String,String> parameterMap=new HashMap(  );

    }
}
