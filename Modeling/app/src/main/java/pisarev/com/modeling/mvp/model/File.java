package pisarev.com.modeling.mvp.model;

import android.content.Context;
import android.widget.Toast;

import pisarev.com.modeling.application.App;

import javax.inject.Inject;

import java.io.*;

import static android.content.Context.MODE_PRIVATE;

public class File {

    @Inject
    Context context;

    public File() {
        App.getComponent().inject( this );
    }

    public void writeFile(String text, String fileName) {
        try {
            // отрываем поток для записи
            BufferedWriter bw = new BufferedWriter( new OutputStreamWriter( context.getApplicationContext().openFileOutput( fileName, MODE_PRIVATE ) ) );
            // пишем данные
            bw.write( text );
            // закрываем поток
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
