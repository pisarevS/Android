package pisarev.com.modeling.mvp.model;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;


import javax.inject.Inject;

import pisarev.com.modeling.application.App;

public class ProgramParameters implements Runnable {

    private String program;
    private String parameter;
    private ArrayList<String> listIgnore = new ArrayList<>();
    private ArrayList<StringBuffer> programList = new ArrayList<>();
    private ArrayList<StringBuffer> parameterList = new ArrayList<>();
    private Map<String, String> variablesList = new HashMap<>();
    public ArrayList<Frame> frameList=new ArrayList<>();

    private int x;
    private int u;
    boolean clockwise;
    final float FIBO = 1123581220;

    String horizontalAxis;
    String verticalAxis;
    @Inject
    MyData data;

    public ProgramParameters(String program, String parameter) {
        this.program = program;
        this.parameter = parameter;
        App.getComponent().inject( this );
        data.getFrameList().clear();
    }

    @Override
    public void run() {
        initList();
        getProgramList(program);
        getParameterList(parameter);
        readParameterVariables(parameterList);
        replaceProgramVariables(programList);
        addFrame();
        for (int i=0;i < frameList.size();i++) {
            System.out.print("id " + frameList.get(i).getId());
            System.out.print(" gCode " + frameList.get(i).getGCode());
            System.out.print(" X=" + frameList.get(i).getX());
            System.out.print(" Z=" + frameList.get(i).getZ());
            if (frameList.get(i).getIsCR()) {
                System.out.print(" CR=" + frameList.get(i).getCr());
            }
            System.out.println();
        }

    }

    private void initList(){
        //ЛПО
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
        //ЛПО2
        listIgnore.add("N_ZERO_O(54,X1,0,\"TR\")");
        listIgnore.add("N_ZERO_O(54,Z1,CHUCK_HEIGHT_Z1_S1[0],\"TR\")");
        listIgnore.add("N_ZERO_O(54,X1,WP_ZP_X1_S1,\"FI\")");
        listIgnore.add("N_ZERO_O(54,Z1,WP_ZP_Z1_S1,\"FI\")");

        listIgnore.add("N_ZERO_O(54,X1,0,\"TR\")");
        listIgnore.add("N_ZERO_O(54,Z1,CHUCK_HEIGHT_Z1_S2[0],\"TR\")");
        listIgnore.add("N_ZERO_O(54,X1,WP_ZP_X1_S2,\"FI\")");
        listIgnore.add("N_ZERO_O(54,Z1,WP_ZP_Z1_S2,\"FI\")");

        listIgnore.add("N_ZERO_O(54,X2,0,\"TR\")");
        listIgnore.add("N_ZERO_O(54,Z2,CHUCK_HEIGHT_Z2_S1[0],\"TR\")");
        listIgnore.add("N_ZERO_O(54,X2,WP_ZP_X2_S1,\"FI\")");
        listIgnore.add("N_ZERO_O(54,Z2,WP_ZP_Z2_S1,\"FI\")");

        listIgnore.add("N_ZERO_O(54,X2,0,\"TR\")");
        listIgnore.add("N_ZERO_O(54,Z2,CHUCK_HEIGHT_Z2_S2[0],\"TR\")");
        listIgnore.add("N_ZERO_O(54,X2,WP_ZP_X2_S2,\"FI\")");
        listIgnore.add("N_ZERO_O(54,Z2,WP_ZP_Z2_S2,\"FI\")");

        variablesList.put("N_GANTRYPOS_X", "650");
        variablesList.put("N_GANTRYPOS_Z", "250");
        variablesList.put("N_GANTRYPOS_U", "650");
        variablesList.put("N_GANTRYPOS_W", "250");
        variablesList.put("$P_TOOLR", "16");
    }

    private void getProgramList(String program) {
        try {
            BufferedReader br = new BufferedReader(new StringReader(program));
            String line;
            while ((line = br.readLine()) != null) {
                programList.add(new StringBuffer(line));
                data.getProgramListTextView().add( line );
            }
            br.close();
        } catch (IOException ignored) {

        }
    }

    private void getParameterList(String parameter) {
        try {
            BufferedReader br = new BufferedReader(new StringReader(parameter));
            String line;
            while ((line = br.readLine()) != null) {
                parameterList.add(new StringBuffer(line));
            }
            br.close();
        } catch (IOException ignored) {

        }
    }

    private void readParameterVariables(ArrayList<StringBuffer> parameterList) {
        for (int i = 0; i < parameterList.size(); i++) {
            if (parameterList.get(i).toString().contains(";")) {
                parameterList.get(i).delete(parameterList.get(i).indexOf(";"), parameterList.get(i).length());
            }
            if (parameterList.get(i).toString().contains("=")) {
                variablesList.put(
                        parameterList.get(i).substring(0, parameterList.get(i).indexOf("=")).replace(" ", "")
                        , parameterList.get(i).substring(parameterList.get(i).indexOf("=") + 1, parameterList.get(i).length()).replace(" ", ""));
            }
        }
        for (Map.Entry entry : variablesList.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();

            for (String keys : variablesList.keySet()) {
                if (value.contains(keys)) {
                    value = value.replace(keys, variablesList.get(keys));
                    variablesList.put(key, value);
                }
            }
        }
    }

    private void replaceProgramVariables(ArrayList<StringBuffer> programList) {
        for (int i = 0; i < programList.size(); i++) {
            for (int j = 0; j < listIgnore.size(); j++) {
                if (programList.get(i).toString().contains(listIgnore.get(j))) {
                    programList.get(i).delete(0, programList.get(i).length());
                }
            }
        }
        for (Map.Entry entry : variablesList.entrySet()) {
            for (int i = 0; i < programList.size(); i++) {
                if (programList.get(i).toString().contains(";")) {
                    programList.get(i).delete(programList.get(i).indexOf(";"), programList.get(i).length());
                }
                if (programList.get(i).toString().contains(entry.getKey().toString())) {
                    String str = programList.get(i).toString().replace(entry.getKey().toString(), entry.getValue().toString());
                    programList.get(i).replace(0, programList.get(i).length(), str);
                }
            }
        }
    }

    private void addFrame() {
        selectCoordinateSystem(programList);
        StringBuffer strFrame;
        boolean isHorizontalAxis = false;
        boolean isVerticalAxis = false;
        float tempHorizontal=650;
        float tempVertical=250;
        float tempCR=0;
        ArrayList<String> tempGCode;
        boolean isCR = false;
        for (int i = 0; i < programList.size(); i++) {
            strFrame = programList.get(i);
            Frame frame=new Frame();

            try {
                if (contains(strFrame, "G")) {
                    tempGCode = searchGCog(strFrame.toString());
                    frame.setGCode(tempGCode);
                    frame.setId( i );
                    frame.setX(tempHorizontal);
                    frame.setZ(tempVertical);
                    frameList.add(frame);
                }
            } catch (Exception e) {

            }
            try {
                if (contains(strFrame, horizontalAxis + "=IC")) {
                    tempHorizontal = tempHorizontal + incrementSearch(strFrame, horizontalAxis + "=IC");
                    isHorizontalAxis = true;
                } else if (containsAxis(strFrame, horizontalAxis)) {
                    tempHorizontal = coordinateSearch(strFrame, horizontalAxis);
                    if (tempHorizontal != FIBO) {
                        isHorizontalAxis = true;
                    } else {

                    }
                }
            } catch (Exception e) {

            }
            try {
                if (contains(strFrame, verticalAxis + "=IC")) {
                    tempVertical = tempVertical + incrementSearch(strFrame, verticalAxis + "=IC");
                    isVerticalAxis = true;
                } else if (containsAxis(strFrame, verticalAxis)) {
                    tempVertical = coordinateSearch(strFrame, verticalAxis);
                    if (tempVertical != FIBO) {
                        isVerticalAxis = true;
                    } else {

                    }
                }
            } catch (Exception e) {

            }
            String radiusCR = "CR=";
            try {
                if (contains(strFrame, radiusCR)) {
                    tempCR = coordinateSearch(strFrame, radiusCR);
                    if (tempCR != FIBO) {
                        isCR = true;
                    } else {

                    }
                }
            } catch (Exception e) {

            }
            if (isHorizontalAxis && isVerticalAxis && isCR) {
                frame.setX(tempHorizontal);
                frame.setZ(tempVertical);
                frame.setCr(tempCR);
                frame.setIsCR(true);
                frame.setAxisContains( true );
                frame.setId(i);
                frameList.add(frame);
                isHorizontalAxis = false;
                isVerticalAxis = false;
                isCR = false;
            }

            if (isHorizontalAxis || isVerticalAxis) {
                frame.setX(tempHorizontal);
                frame.setZ(tempVertical);
                frame.setAxisContains( true );
                frame.setId(i);
                frameList.add(frame);
                isHorizontalAxis = false;
                isVerticalAxis = false;
            }
        }
        Set<Frame> s = new LinkedHashSet<>(frameList);
        frameList.clear();
        frameList.addAll( s );
        data.setFrameList( frameList );
    }

    private ArrayList<String> searchGCog(String frame) {
        ArrayList<String> gCodeList= new ArrayList<>();
        StringBuilder G = new StringBuilder("G");
        if (frame.contains("G")) {

            for (int i = 0; i < frame.length(); i++) {
                char c = frame.charAt(i);
                if (c == 'G') {
                    for (int j = i + 1; j < frame.length(); j++) {
                        char t = frame.charAt(j);
                        if (isDigit(t)) {
                            G.append(t);
                        } else {
                            gCodeList.add(G.toString());
                            break;
                        }
                    }
                    G = new StringBuilder("G");
                }
            }
        }
        return gCodeList;
    }

    private float coordinateSearch(StringBuffer frame, String axis) {
        Expression expression = new Expression();
        StringBuffer temp = new StringBuffer();
        int n = frame.indexOf(axis);

        if (isDigit(frame.charAt(n + axis.length())) || frame.charAt(n + axis.length()) == '-' || frame.charAt(n + axis.length()) == '+') {
            for (int i = n + axis.length(); i < frame.length(); i++) {
                if (readUp(frame.charAt(i))) {
                    temp.append(frame.charAt(i));
                } else {
                    break;
                }
            }
            return Float.parseFloat(temp.toString());
        } else if (frame.charAt(n + axis.length()) == '=') {
            for (int i = n + axis.length() + 1; i < frame.length(); i++) {
                if (readUp(frame.charAt(i))) {
                    temp.append(frame.charAt(i));
                } else {
                    break;
                }
            }
            return expression.calculate(temp.toString());
        }
        return FIBO;
    }

    private float incrementSearch(StringBuffer frame, String axis) {
        Expression expression = new Expression();
        StringBuilder temp = new StringBuilder();
        int n = frame.indexOf(axis);

        if (frame.charAt(n + axis.length()) == '(') {
            for (int i = n + axis.length(); i < frame.length(); i++) {
                if (readUp(frame.charAt(i))) {
                    temp.append(frame.charAt(i));
                } else {
                    break;
                }
            }
            return expression.calculate(temp.toString());
        }
        return Float.parseFloat(temp.toString());
    }

    private boolean containsAxis(StringBuffer frame, String axis) {
        if (contains(frame, axis)) {
            int n=frame.indexOf(axis) + 1;
            char c=frame.charAt(n);
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

    private void selectCoordinateSystem(ArrayList<StringBuffer> programList) {
        for (int i = 0; i < programList.size(); i++) {
            if (programList.get(i).toString().contains("X"))
                x++;
            if (programList.get(i).toString().contains("U"))
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

    private static boolean readUp(char input) {
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

    private boolean contains(StringBuffer sb, String findString) {
        return sb.indexOf(findString) > -1;
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
