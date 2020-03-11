package pisarev.com.modeling.mvp.model;

import android.os.Build;
import android.util.Log;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MyFile {

    private final String TEG = getClass().getName();

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
            Log.d(TEG,e.getMessage());
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
            Log.d(TEG,e.getMessage());
        }
        return text.toString();
    }

    public static List<StringBuffer> getParameter(File path) {
        File folder = new File(path.getParent());
        File[] listOfFiles = folder.listFiles();
        assert listOfFiles != null;
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                if (listOfFile.getName().contains("PAR")) {
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            return  Files.lines(Paths.get(listOfFile.getPath())).map(StringBuffer::new).collect(Collectors.toList());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return new ArrayList<>();
                    }
                }
            }
        }
        return new ArrayList<>();
    }
}
