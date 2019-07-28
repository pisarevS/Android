package pisarev.com.modeling.mvp.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import pisarev.com.modeling.application.App;
import pisarev.com.modeling.mvp.model.base.BaseProgram;


public class Program extends BaseProgram implements Runnable {

    private ArrayList<StringBuffer> programList;
    private ArrayList<StringBuffer> parameterList;
    @Inject
    MyData data;

    public Program(String program, String parameter) {
        super( program, parameter );
        programList = new ArrayList<>();
        parameterList = new ArrayList<>();
        App.getComponent().inject( this );
        data.getFrameList().clear();
    }

    @Override
    public void run() {
        data.setProgramList( getList( program ) );
        programList.addAll( getList( program ) );
        parameterList.addAll( getList( parameter ) );
        removeIgnore( programList );
        removeLockedFrame( programList );
        gotoF( programList );
        readParameterVariables( parameterList );
        replaceParameterVariables( variablesList );
        replaceProgramVariables( programList );
        addFrameList();
    }

    @Override
    protected ArrayList<StringBuffer> getList(String program) {
        ArrayList<StringBuffer> arrayList = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader( new StringReader( program ) );
            String line;
            while ((line = br.readLine()) != null) {
                arrayList.add( new StringBuffer( line ) );
            }
            br.close();
        } catch (IOException ignored) {

        }
        return arrayList;
    }

    @Override
    protected void readParameterVariables(ArrayList<StringBuffer> parameterList) {
        for (int i = 0; i < parameterList.size(); i++) {
            if (parameterList.get( i ).toString().contains( ";" )) {
                parameterList.get( i ).delete( parameterList.get( i ).indexOf( ";" ), parameterList.get( i ).length() );
            }
            if (parameterList.get( i ).toString().contains( "=" )) {
                variablesList.put(
                        parameterList.get( i ).substring( 0, parameterList.get( i ).indexOf( "=" ) ).replace( " ", "" )
                        , parameterList.get( i ).substring( parameterList.get( i ).indexOf( "=" ) + 1, parameterList.get( i ).length() ).replace( " ", "" ) );
            }
        }
    }

    @Override
    protected void replaceParameterVariables(Map<String, String> variablesList) {
        for (Map.Entry entry : variablesList.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();

            for (String keys : variablesList.keySet()) {
                if (value.contains( keys )) {
                    value = value.replace( keys, variablesList.get( keys ) );
                    variablesList.put( key, value );
                }
            }
        }
    }

    @Override
    protected void replaceProgramVariables(ArrayList<StringBuffer> programList) {
        for (Map.Entry entry : variablesList.entrySet()) {
            for (int i = 0; i < programList.size(); i++) {
                if (programList.get( i ).toString().contains( entry.getKey().toString() )) {
                    String str = programList.get( i ).toString().replace( entry.getKey().toString(), entry.getValue().toString() );
                    programList.get( i ).replace( 0, programList.get( i ).length(), str );
                }
            }
        }
    }

    @Override
    protected void addFrameList() {
        selectCoordinateSystem( programList );
        StringBuffer strFrame;
        boolean isHorizontalAxis = false;
        boolean isVerticalAxis = false;
        float tempHorizontal = 650;
        float tempVertical = 250;
        float tempCR = 0;
        ArrayList<String> tempGCode;
        boolean isCR = false;
        for (int i = 0; i < programList.size(); i++) {
            strFrame = programList.get( i );
            Frame frame = new Frame();

            try {
                if (contains( strFrame, "G" )) {
                    tempGCode = searchGCog( strFrame.toString() );
                    frame.setGCode( tempGCode );
                    frame.setId( i );
                    frame.setX( tempHorizontal );
                    frame.setZ( tempVertical );
                    frameList.add( frame );
                }
            } catch (Exception e) {
                errorListMap.put( i, strFrame.toString() );
            }
            try {
                if (contains( strFrame, horizontalAxis + "=IC" )) {
                    tempHorizontal = tempHorizontal + incrementSearch( strFrame, horizontalAxis + "=IC" );
                    isHorizontalAxis = true;
                } else if (containsAxis( strFrame, horizontalAxis )) {
                    tempHorizontal = coordinateSearch( strFrame, horizontalAxis );
                    if (tempHorizontal != FIBO) {
                        isHorizontalAxis = true;
                    } else {
                        errorListMap.put( i, strFrame.toString() );
                    }
                }
            } catch (Exception e) {
                errorListMap.put( i, strFrame.toString() );
            }
            try {
                if (contains( strFrame, verticalAxis + "=IC" )) {
                    tempVertical = tempVertical + incrementSearch( strFrame, verticalAxis + "=IC" );
                    isVerticalAxis = true;
                } else if (containsAxis( strFrame, verticalAxis )) {
                    tempVertical = coordinateSearch( strFrame, verticalAxis );
                    if (tempVertical != FIBO) {
                        isVerticalAxis = true;
                    } else {
                        errorListMap.put( i, strFrame.toString() );
                    }
                }
            } catch (Exception e) {
                errorListMap.put( i, strFrame.toString() );
            }
            String radiusCR = "CR=";
            try {
                if (contains( strFrame, radiusCR )) {
                    tempCR = coordinateSearch( strFrame, radiusCR );
                    if (tempCR != FIBO) {
                        isCR = true;
                    } else {
                        errorListMap.put( i, strFrame.toString() );
                    }
                }
            } catch (Exception e) {
                errorListMap.put( i, strFrame.toString() );
            }
            if (isHorizontalAxis && isVerticalAxis && isCR) {
                frame.setX( tempHorizontal );
                frame.setZ( tempVertical );
                frame.setCr( tempCR );
                frame.setIsCR( true );
                frame.setAxisContains( true );
                frame.setId( i );
                frameList.add( frame );
                isHorizontalAxis = false;
                isVerticalAxis = false;
                isCR = false;
            }

            if (isHorizontalAxis || isVerticalAxis) {
                frame.setX( tempHorizontal );
                frame.setZ( tempVertical );
                frame.setAxisContains( true );
                frame.setId( i );
                frameList.add( frame );
                isHorizontalAxis = false;
                isVerticalAxis = false;
            }
        }
        data.setErrorListMap( errorListMap );
        Set<Frame> s = new LinkedHashSet<>( frameList );
        frameList.clear();
        frameList.addAll( s );
        data.setFrameList( frameList );
    }

    private void gotoF(ArrayList<StringBuffer> programList) {
        String label;
        String gotoF = "GOTOF";
        for (int i = 0; i < programList.size(); i++) {
            if (programList.get( i ).toString().contains( gotoF )) {
                label = programList.get( i ).substring( programList.get( i ).indexOf( gotoF ) + gotoF.length(), programList.get( i ).length() ).replace( " ","" );
                for(int j=i+1;j<programList.size();j++){
                    if (!programList.get( j ).toString().contains( label+":" )) {
                        programList.get( j ).delete( 0, programList.get( j ).length() );
                    }else {
                        break;
                    }
                }
            }

        }
    }

    private void removeIgnore(ArrayList<StringBuffer> programList) {
        for (int i = 0; i < programList.size(); i++) {
            for (int j = 0; j < listIgnore.size(); j++) {
                if (programList.get( i ).toString().contains( listIgnore.get( j ) )) {
                    programList.get( i ).delete( 0, programList.get( i ).length() );
                }
            }
        }
    }

    private void removeLockedFrame(ArrayList<StringBuffer> programList) {
        for (int i = 0; i < programList.size(); i++) {
            if (programList.get( i ).toString().contains( ";" )) {
                programList.get( i ).delete( programList.get( i ).indexOf( ";" ), programList.get( i ).length() );
            }
        }

    }

}
