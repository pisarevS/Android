package pisarev.com.modeling.mvp.model;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
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
    private String[] defs = {"DEF REAL", "DEF INT"};
    private String offn = "OFFN=";
    private final String TEG = getClass().getName();
    @Inject
    MyData data;
    @Inject
    Context context;

    public Program(String program) {
        super( program );
        programList = new ArrayList<>();
        parameterList = new ArrayList<>();
        App.getComponent().inject( this );
        data.getFrameList().clear();
    }

    @Override
    public void run() {
        data.setProgramList( getList( program ) );
        programList.addAll( getList( program ) );
        removeIgnore( programList );
        removeLockedFrame( programList );
        gotoF( programList );
        if (containsDef( programList ))
            searchDef( programList );
        parameter = getParameter( new SQLiteData( context, SQLiteData.DATABASE_PATH ).getProgramText().get( SQLiteData.KEY_PROGRAM ) );
        parameterList.addAll( getList( parameter ) );
        readParameterVariables( parameterList );
        replaceParameterVariables( variablesList );
        replaceProgramVariables( programList );
        addFrameList();
        for (int i = 0; i < frameList.size(); i++) {
            Log.d( TEG, frameList.get( i ).toString() );
        }
    }

    private String convert() {
        programList.addAll( getList( program ) );
        if (containsDef( programList ))
            searchDef( programList );
        parameter = getParameter( new SQLiteData( context, SQLiteData.DATABASE_PATH ).getProgramText().get( SQLiteData.KEY_PROGRAM ) );
        parameterList.addAll( getList( parameter ) );
        readParameterVariables( parameterList );
        replaceParameterVariables( variablesList );
        replaceProgramVariables( programList );
        deleteVariables();
        StringBuilder temp = new StringBuilder();
        for (StringBuffer text : programList) {
            temp.append( text ).append( "\n" );
        }
        return temp.toString();
    }

    private void deleteVariables() {
        selectCoordinateSystem( programList );
        StringBuffer strFrame;
        float tempHorizontal = 650;
        float tempVertical = 250;
        for (int i = 0; i < programList.size(); i++) {
            strFrame = programList.get( i );

            try {
                if (contains( strFrame, horizontalAxis + "=IC" )) {
                    tempHorizontal = tempHorizontal + incrementSearch( strFrame, horizontalAxis + "=IC" );
                    String strHorizontal = incrementSearchStr( strFrame, horizontalAxis + "=IC" );

                    int startIndex = programList.get( i ).indexOf( strHorizontal ) - 2;
                    int endIndex = programList.get( i ).indexOf( strHorizontal ) + strHorizontal.length();
                    programList.get( i ).replace( startIndex, endIndex, tempHorizontal + " " );


                } else if (containsAxis( strFrame, horizontalAxis )) {
                    tempHorizontal = coordinateSearch( strFrame, horizontalAxis );
                    String strHorizontal = coordinateSearchStr( strFrame, horizontalAxis );

                    if (tempHorizontal != FIBO) {
                        int startIndex = programList.get( i ).indexOf( strHorizontal );
                        int endIndex = programList.get( i ).indexOf( strHorizontal ) + strHorizontal.length();
                        programList.get( i ).replace( startIndex, endIndex, tempHorizontal + " " );
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
                    String strVertical = incrementSearchStr( strFrame, verticalAxis + "=IC" );
                    int startIndex = programList.get( i ).indexOf( strVertical ) - 2;
                    int endIndex = programList.get( i ).indexOf( strVertical ) + strVertical.length();
                    programList.get( i ).replace( startIndex, endIndex, tempVertical + " " );

                } else if (containsAxis( strFrame, verticalAxis )) {
                    tempVertical = coordinateSearch( strFrame, verticalAxis );
                    String strVertical = coordinateSearchStr( strFrame, verticalAxis );


                    if (tempVertical != FIBO) {
                        int startIndex = programList.get( i ).indexOf( strVertical );
                        int endIndex = programList.get( i ).indexOf( strVertical ) + strVertical.length();
                        programList.get( i ).replace( startIndex, endIndex, tempVertical + " " );

                    } else {
                        errorListMap.put( i, strFrame.toString() );
                    }
                }
            } catch (Exception e) {
                errorListMap.put( i, strFrame.toString() );
            }
        }
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
                int key = 0;
                for (int j = parameterList.get( i ).indexOf( "=" ) - 1; j >= 0; j--) {
                    char c = parameterList.get( i ).charAt( j );
                    if (c == ' ') {
                        key = j;
                        break;
                    }
                }
                variablesList.put(
                        parameterList.get( i ).substring( key, parameterList.get( i ).indexOf( "=" ) ).replace( " ", "" )
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
        boolean isCR = false;
        boolean isRadius = false;
        boolean isOffn = false;
        for (int i = 0; i < programList.size(); i++) {
            strFrame = programList.get( i );
            Frame frame = new Frame();

            try {
                if (contains( strFrame, offn )) {
                    frame.setOffn( searchOffn( strFrame ) );
                    frame.setId( i );
                    frame.setX( tempHorizontal );
                    frame.setZ( tempVertical );
                    frameList.add( frame );
                    isOffn = true;
                }
            } catch (Exception e) {
                errorListMap.put( i, strFrame.toString() );
            }

            try {
                if (contains( strFrame, "G" )) {
                    ArrayList<String> gCode = searchGCog( strFrame.toString() );
                    isRadius = activatedRadius( searchGCog( strFrame.toString() ) );
                    frame.setGCode( gCode );
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
                if (contains( strFrame, radiusCR ) && isRadius) {
                    tempCR = coordinateSearch( strFrame, radiusCR );
                    if (tempCR != FIBO) {
                        isCR = true;
                    }
                } else if (contains( strFrame, radiusCR ) && !isRadius && !isOffn) {
                    errorListMap.put( i, strFrame.toString() );
                } else if (!contains( strFrame, radiusCR ) && isRadius && !isOffn) {
                    errorListMap.put( i, strFrame.toString() );
                }
            } catch (Exception e) {
                errorListMap.put( i, strFrame.toString() );
            }

            if (isCR) {
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
                isOffn = false;
            }

            if (isHorizontalAxis || isVerticalAxis) {
                frame.setX( tempHorizontal );
                frame.setZ( tempVertical );
                frame.setAxisContains( true );
                frame.setId( i );
                frameList.add( frame );
                isHorizontalAxis = false;
                isVerticalAxis = false;
                isOffn = false;
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
                label = programList.get( i ).substring( programList.get( i ).indexOf( gotoF ) + gotoF.length(), programList.get( i ).length() ).replace( " ", "" );
                for (int j = i + 1; j < programList.size(); j++) {
                    if (!programList.get( j ).toString().contains( label + ":" )) {
                        programList.get( j ).delete( 0, programList.get( j ).length() );
                    } else {
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

    private boolean containsDef(ArrayList<StringBuffer> programList) {
        for (int i = 0; i < programList.size(); i++) {
            for (String def : defs) {
                if (programList.get( i ).toString().contains( def ) && programList.get( i ).toString().contains( "=" ))
                    return true;
            }
        }
        return false;
    }

    private void searchDef(ArrayList<StringBuffer> programList) {
        for (int i = 0; i < programList.size(); i++) {
            for (String def : defs) {
                if (programList.get( i ).toString().contains( def ) && programList.get( i ).toString().contains( "=" )) {
                    int n = programList.get( i ).indexOf( def ) + def.length();
                    String key = programList.get( i ).substring( n, programList.get( i ).indexOf( "=" ) ).replace( " ", "" );
                    String value = programList.get( i ).substring( programList.get( i ).indexOf( "=" ) + 1, programList.get( i ).length() ).replace( " ", "" );
                    variablesList.put( key, value );
                }
            }
        }
    }

    private String getParameter(String path) {
        MyFile myFile = new MyFile();
        File directory = new File( path );
        File folder = new File( directory.getParent() );
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                if (listOfFiles[i].getName().contains( "PAR" )) {
                    return myFile.readFile( listOfFiles[i].getPath() );
                }
            }
        }
        return "";
    }

    private float searchOffn(StringBuffer frame) {
        Expression expression = new Expression();
        int n = frame.indexOf( offn );
        String temp = frame.substring( n + offn.length(), frame.length() );
        return expression.calculate( temp );
    }
}