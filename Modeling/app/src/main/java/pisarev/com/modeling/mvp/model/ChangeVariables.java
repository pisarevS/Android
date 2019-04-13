package pisarev.com.modeling.mvp.model;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import pisarev.com.modeling.Const;
import pisarev.com.modeling.application.App;

public class ChangeVariables implements Runnable {

    private String program;
    private String parameter;
    private ArrayList<StringBuffer>programList;
    private ArrayList<StringBuffer>parameterList;
    private Map<String,String> variablesList;
    @Inject
    MyData data;

    public ChangeVariables(String program,String parameter){
        this.program=program;
        this.parameter=parameter;
        App.getComponent().inject( this );
    }

    @Override
    public void run() {
        getProgramList(program);
        getParameterList(parameter);
        readParameterVariables(parameterList);
        replaceProgramVariables( programList );
    }

    private void getProgramList(String program){
        programList=new ArrayList<>(  );
        try {
            BufferedReader br = new BufferedReader( new StringReader(program) );
            String line;
            while ((line = br.readLine()) != null) {
                programList.add(new StringBuffer( line )  );
            }
            br.close();
        }catch (IOException e){

        }
    }

    private void getParameterList(String parameter){
        parameterList=new ArrayList<>(  );
        try {
            BufferedReader br = new BufferedReader( new StringReader(parameter) );
            String line;
            while ((line = br.readLine()) != null) {
                parameterList.add(new StringBuffer( line )  );
            }
            br.close();
        }catch (IOException e){

        }
    }

    private void readParameterVariables(ArrayList<StringBuffer>parameterList){
        variablesList=new HashMap(  );
        for(int i=0;i<parameterList.size();i++){
            if(parameterList.get(i).toString().contains(";")){
                parameterList.get(i).delete(parameterList.get(i).indexOf(";"),parameterList.get(i).length());
            }
            if(parameterList.get(i).toString().contains("=")){
                variablesList.put(
                        parameterList.get(i).substring(0,parameterList.get(i).indexOf("=")).replace(" ","")
                        ,parameterList.get(i).substring(parameterList.get(i).indexOf("=")+1,parameterList.get(i).length()).replace(" ",""));
            }
        }
        for(Map.Entry entry:variablesList.entrySet()){
            String key=entry.getKey().toString();
            String value=entry.getValue().toString();

            for(String keys:variablesList.keySet()){
                if(value.contains(keys)){
                    value=value.replace(keys,variablesList.get(keys));
                    variablesList.put(key,value);
                }
            }
        }
    }

    private void replaceProgramVariables(ArrayList<StringBuffer>programList){
        for(Map.Entry entry:variablesList.entrySet()){
            for(int i=0;i<programList.size();i++){
                if(programList.get(i).toString().contains(";")){
                    programList.get(i).delete(programList.get(i).indexOf(";"),programList.get(i).length());

                }
                if(programList.get(i).toString().contains(entry.getKey().toString())){
                    String str = programList.get( i ).toString().replace( entry.getKey().toString(),entry.getValue().toString() );
                    programList.get( i ).replace( 0,programList.get( i ).length(),str );
                }
            }
        }
        data.getProgramList().addAll( programList );
    }
}
