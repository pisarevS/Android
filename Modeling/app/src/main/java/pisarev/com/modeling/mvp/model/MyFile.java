package pisarev.com.modeling.mvp.model;

import android.util.Log;
import java.io.*;

public class MyFile {

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
            Log.d(Const.TEG,e.getMessage());
            e.printStackTrace();
        }
    }


    public String readFile(String path) {
        java.io.File file = new java.io.File(path);
        StringBuffer text = new StringBuffer();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append("\n");
            }
            br.close();
        } catch (IOException e) {
            Log.d(Const.TEG,e.getMessage());
        }
        return text.toString();
    }
}
