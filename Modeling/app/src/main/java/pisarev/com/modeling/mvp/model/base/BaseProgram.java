package pisarev.com.modeling.mvp.model.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pisarev.com.modeling.mvp.model.Expression;
import pisarev.com.modeling.mvp.model.Frame;

public abstract class BaseProgram {

    private int x;
    private int u;
    protected String horizontalAxis;
    protected String verticalAxis;
    protected String program;
    protected String parameter;
    protected ArrayList<String> listIgnore;
    protected Map<String, String> variablesList;
    protected ArrayList<Frame> frameList;
    protected Map<Integer, String> errorListMap;
    protected final float FIBO = 1123581220;

    protected BaseProgram(String program, String parameter){
        this.program = program;
        this.parameter = parameter;
        initLists();
    }

    protected abstract void readParameterVariables(ArrayList<StringBuffer> parameterList);

    protected abstract ArrayList<StringBuffer> getList(String program);

    protected abstract void replaceProgramVariables(ArrayList<StringBuffer> programList);

    protected abstract void replaceParameterVariables(Map<String, String> variablesList);

    protected abstract void addFrameList();

    private void initLists() {
        listIgnore = new ArrayList<>();
        variablesList = new HashMap<>();
        frameList = new ArrayList<>();
        errorListMap = new HashMap<>();
        //ЛПО
        listIgnore.add( "G58 X=0 Z=N_CHUCK_HEIGHT_Z_S1[N_CHUCK_JAWS]" );
        listIgnore.add( "G59 X=N_WP_ZP_X_S1 Z=N_WP_ZP_Z_S1" );
        listIgnore.add( "G59 X=N_WP_ZP_X_S1" );
        listIgnore.add( "G59 X=N_WP_ZP_X_S1 Z=N_WP_ZP_Z_S1" );
        listIgnore.add( "G58 X=0 Z=N_CHUCK_HEIGHT_Z_S2[N_CHUCK_JAWS]" );
        listIgnore.add( "G59 X=N_WP_ZP_X_S2 Z=N_WP_ZP_Z_S2" );
        listIgnore.add( "G58 U=0 W=N_CHUCK_HEIGHT_W_S1[N_CHUCK_JAWS]" );
        listIgnore.add( "G59 U=N_WP_ZP_U_S1 W=N_WP_ZP_W_S1" );
        listIgnore.add( "G58 U=0 W=N_CHUCK_HEIGHT_W_S2[N_CHUCK_JAWS]" );
        listIgnore.add( "G59 U=N_WP_ZP_U_S2 W=N_WP_ZP_W_S2" );
        //ЛПО2
        listIgnore.add( "N_ZERO_O(54,X1,0,\"TR\")" );
        listIgnore.add( "N_ZERO_O(54,Z1,CHUCK_HEIGHT_Z1_S1[0],\"TR\")" );
        listIgnore.add( "N_ZERO_O(54,X1,WP_ZP_X1_S1,\"FI\")" );
        listIgnore.add( "N_ZERO_O(54,Z1,WP_ZP_Z1_S1,\"FI\")" );

        listIgnore.add( "N_ZERO_O(54,X1,0,\"TR\")" );
        listIgnore.add( "N_ZERO_O(54,Z1,CHUCK_HEIGHT_Z1_S2[0],\"TR\")" );
        listIgnore.add( "N_ZERO_O(54,X1,WP_ZP_X1_S2,\"FI\")" );
        listIgnore.add( "N_ZERO_O(54,Z1,WP_ZP_Z1_S2,\"FI\")" );

        listIgnore.add( "N_ZERO_O(54,X2,0,\"TR\")" );
        listIgnore.add( "N_ZERO_O(54,Z2,CHUCK_HEIGHT_Z2_S1[0],\"TR\")" );
        listIgnore.add( "N_ZERO_O(54,X2,WP_ZP_X2_S1,\"FI\")" );
        listIgnore.add( "N_ZERO_O(54,Z2,WP_ZP_Z2_S1,\"FI\")" );

        listIgnore.add( "N_ZERO_O(54,X2,0,\"TR\")" );
        listIgnore.add( "N_ZERO_O(54,Z2,CHUCK_HEIGHT_Z2_S2[0],\"TR\")" );
        listIgnore.add( "N_ZERO_O(54,X2,WP_ZP_X2_S2,\"FI\")" );
        listIgnore.add( "N_ZERO_O(54,Z2,WP_ZP_Z2_S2,\"FI\")" );

        variablesList.put( "N_GANTRYPOS_X", "650" );
        variablesList.put( "N_GANTRYPOS_Z", "250" );
        variablesList.put( "N_GANTRYPOS_U", "650" );
        variablesList.put( "N_GANTRYPOS_W", "250" );
        variablesList.put( "$P_TOOLR", "16" );
    }

    protected ArrayList<String> searchGCog(String frame) {
        ArrayList<String> gCodeList = new ArrayList<>();
        StringBuilder G = new StringBuilder( "G" );
        if (frame.contains( "G" )) {

            for (int i = 0; i < frame.length(); i++) {
                char c = frame.charAt( i );
                if (c == 'G') {
                    for (int j = i + 1; j < frame.length(); j++) {
                        char t = frame.charAt( j );
                        if (isDigit( t )) {
                            G.append( t );
                        } else {
                            gCodeList.add( G.toString() );
                            break;
                        }
                    }
                    G = new StringBuilder( "G" );
                }
            }
        }
        return gCodeList;
    }

    protected float coordinateSearch(StringBuffer frame, String axis) {
        Expression expression = new Expression();
        StringBuffer temp = new StringBuffer();
        int n = frame.indexOf( axis );

        if (isDigit( frame.charAt( n + axis.length() ) ) || frame.charAt( n + axis.length() ) == '-' || frame.charAt( n + axis.length() ) == '+') {
            for (int i = n + axis.length(); i < frame.length(); i++) {
                if (readUp( frame.charAt( i ) )) {
                    temp.append( frame.charAt( i ) );
                } else {
                    break;
                }
            }
            return Float.parseFloat( temp.toString() );
        } else if (frame.charAt( n + axis.length() ) == '=') {
            for (int i = n + axis.length() + 1; i < frame.length(); i++) {
                if (readUp( frame.charAt( i ) )) {
                    temp.append( frame.charAt( i ) );
                } else {
                    break;
                }
            }
            return expression.calculate( temp.toString() );
        }
        return FIBO;
    }

    protected float incrementSearch(StringBuffer frame, String axis) {
        Expression expression = new Expression();
        StringBuilder temp = new StringBuilder();
        int n = frame.indexOf( axis );

        if (frame.charAt( n + axis.length() ) == '(') {
            for (int i = n + axis.length(); i < frame.length(); i++) {
                if (readUp( frame.charAt( i ) )) {
                    temp.append( frame.charAt( i ) );
                } else {
                    break;
                }
            }
            return expression.calculate( temp.toString() );
        }
        return Float.parseFloat( temp.toString() );
    }

    protected boolean containsAxis(StringBuffer frame, String axis) {
        if (contains( frame, axis )) {
            int n = frame.indexOf( axis ) + 1;
            char c = frame.charAt( n );
            switch (c) {
                case '-':
                case '=':
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    return true;
            }
        }
        return false;
    }

    protected void selectCoordinateSystem(ArrayList<StringBuffer> programList) {
        for (int i = 0; i < programList.size(); i++) {
            if (programList.get( i ).toString().contains( "X" ))
                x++;
            if (programList.get( i ).toString().contains( "U" ))
                u++;
            if (x > u) {
                horizontalAxis = "X";
                verticalAxis = "Z";
            } else {
                horizontalAxis = "U";
                verticalAxis = "W";
            }
        }
    }

    private boolean readUp(char input) {
        switch (input) {
            case 'C':
            case 'X':
            case 'G':
            case 'M':
            case 'F':
            case 'W':
            case 'Z':
            case 'D':
            case 'S':
            case 'A':
            case 'U':
            case 'L':
            case 'O':
            case 'H':
                return false;
        }
        return true;
    }

    protected boolean contains(StringBuffer sb, String findString) {
        return sb.indexOf( findString ) > -1;
    }

    private boolean isDigit(char input) {
        switch (input) {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return true;
        }
        return false;
    }

}
