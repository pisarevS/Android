package pisarev.com.modeling.mvp.model;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import pisarev.com.modeling.application.App;

public class ProgramParameters implements Runnable {

    private String program;
    private String parameter;
    private ArrayList<String> listIgnore =new ArrayList<>();
    private ArrayList<StringBuffer>programList=new ArrayList<>(  );
    private ArrayList<StringBuffer>parameterList=new ArrayList<>(  );
    private Map<String,String> variablesList=new HashMap<>(  );
    @Inject
    MyData data;

    public ProgramParameters(String program, String parameter){
        this.program=program;
        this.parameter=parameter;
        App.getComponent().inject( this );
        data.getProgramList().clear();
    }

    @Override
    public void run() {
        getProgramList(program);
        getParameterList(parameter);
        readParameterVariables(parameterList);
        replaceProgramVariables( programList );

    }

    private void getProgramList(String program){
        try {
            BufferedReader br = new BufferedReader( new StringReader(program) );
            String line;
            while ((line = br.readLine()) != null) {
                programList.add(new StringBuffer( line )  );
                data.getProgramListTextView().add( line );
            }
            br.close();
        }catch (IOException e){

        }
    }

    private void getParameterList(String parameter){
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
        variablesList.put("N_GANTRYPOS_X","650");
        variablesList.put("N_GANTRYPOS_Z","250");
        variablesList.put("N_GANTRYPOS_U","650");
        variablesList.put("N_GANTRYPOS_W","250");
        variablesList.put("$P_TOOLR","16");
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
        listIgnore.add("G58 X=0 Z=N_CHUCK_HEIGHT_Z_S1[N_CHUCK_JAWS]");
        listIgnore.add("G59 X=N_WP_ZP_X_S1 Z=N_WP_ZP_Z_S1");
        listIgnore.add("G59 X=N_WP_ZP_X_S1");
        listIgnore.add("G59 X=N_WP_ZP_X_S1 Z=N_WP_ZP_Z_S1");
        listIgnore.add("G58 X=0 Z=N_CHUCK_HEIGHT_Z_S2[N_CHUCK_JAWS]");
        listIgnore.add("G59 X=N_WP_ZP_X_S2 Z=N_WP_ZP_Z_S2");
        listIgnore.add("G58 U=0 W=N_CHUCK_HEIGHT_W_S1[N_CHUCK_JAWS]");
        listIgnore.add("G59 U=N_WP_ZP_U_S1 W=N_WP_ZP_W_S1");
        listIgnore.add("G58 U=0 W=N_CHUCK_HEIGHT_W_S2[N_CHUCK_JAWS]");
        listIgnore.add("G59 U=N_WP_ZP_U_S2 W=N_WP_ZP_W_S2");

     for(int i=0;i<programList.size();i++){
         for(int j = 0; j< listIgnore.size(); j++){
             if(programList.get(i).toString().contains( listIgnore.get(j))){
                 programList.get(i).delete(0,programList.get(i).length());
             }
         }
     }
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
        data.setProgramList( programList );
    }
}
