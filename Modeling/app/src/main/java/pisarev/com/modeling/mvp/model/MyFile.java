package pisarev.com.modeling.mvp.model;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;



import pisarev.com.modeling.application.App;

import javax.inject.Inject;

import java.io.*;

public class MyFile {

    @Inject
    Context context;

    public MyFile() {
        App.getComponent().inject( this );
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }


    public void writeFile(String text, String path) {
        File sdFile = new File(path);
        try {
            // открываем поток для записи
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            // пишем данные
            bw.write(text);
            // закрываем поток
            bw.close();
        } catch (IOException e) {
            Toast.makeText( context.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG ).show();
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
                text.append( "\n" );
            }
            br.close();
        } catch (IOException e) {
            Toast.makeText( context.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG ).show();
        }
        return text.toString();
    }
}
