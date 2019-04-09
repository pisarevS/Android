package pisarev.com.modeling.mvp.presenter;

import android.content.ContextWrapper;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import pisarev.com.modeling.mvp.ViewMvp;
import pisarev.com.modeling.mvp.view.fragments.ParameterFragment;
import pisarev.com.modeling.mvp.view.fragments.ProgramFragment;

import static android.content.Context.MODE_PRIVATE;


public class PresenterMainImpl implements ViewMvp.PresenterMainMvp {

    private ViewMvp.MainViewMvp mainViewMvp;
    private ContextWrapper contextWrapper;

    public PresenterMainImpl(ViewMvp.MainViewMvp mainViewMvp, ContextWrapper contextWrapper){
        this.mainViewMvp=mainViewMvp;
        this.contextWrapper=contextWrapper;
    }

    @Override
    public void openProgram(String path) {
        mainViewMvp.showProgram(readFile( path )  );
    }

    @Override
    public void openParameter(String path) {
        mainViewMvp.showParameter(readFile(path ) );
    }

    public void writeFile(String text, String fileName) {
        try {
            // отрываем поток для записи
            BufferedWriter bw = new BufferedWriter( new OutputStreamWriter( contextWrapper.openFileOutput( fileName, MODE_PRIVATE ) ) );
            // пишем данные
            bw.write( text );
            // закрываем поток
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String readFile(String path) {
        java.io.File file = new java.io.File( path );
        StringBuffer text = new StringBuffer();

        try {
            BufferedReader br = new BufferedReader( new FileReader( file ) );
            String line;
            while ((line = br.readLine()) != null) {
                text.append( line );
                text.append("\n");
            }
            br.close();
        } catch (IOException e) {
            mainViewMvp.showError(e);
        }
        return text.toString();
    }





}
