package pisarev.com.modeling;

import java.util.ArrayList;

public class MyCollection {

    private static ArrayList<String> programArrayList =new ArrayList<>(  );
    private static ArrayList<String> parameterArrayList=new ArrayList<>(  );

    public static ArrayList<String> getParameterArrayList() {
        return parameterArrayList;
    }

    public static void setParameterArrayList(String parameter) {
        parameterArrayList.add( parameter );
    }

    public static ArrayList<String> getProgramArrayList() {
        return programArrayList;
    }

    public static void setProgramArrayList(String program) {
        programArrayList.add( program );
    }

}
