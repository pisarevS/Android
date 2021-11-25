package pisarev.com.modeling.mvp.model

import android.os.Build
import android.util.Log
import java.io.*
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.stream.Collectors
import kotlin.text.StringBuilder

class MyFile {
    private val TEG = javaClass.name
    fun writeFile(text: String?, path: String?) {
        val sdFile = File(path)
        try {
            // открываем поток для записи
            val bw = BufferedWriter(FileWriter(sdFile))
            // пишем данные
            bw.write(text)
            // закрываем поток
            bw.close()
        } catch (e: IOException) {
            Log.d(TEG, Objects.requireNonNull(e.message))
            e.printStackTrace()
        }
    }

    fun readFile(path: String?): String {
        val file = File(path)
        val text = StringBuilder()
        try {
            val br = BufferedReader(FileReader(file))
            var line: String?
            while (br.readLine().also { line = it } != null) {
                text.append(line)
                text.append("\n")
            }
            br.close()
        } catch (e: IOException) {
            Log.d(TEG, Objects.requireNonNull(e.message))
        }
        return text.toString()
    }

    companion object {
        @JvmStatic
        fun getParameter(path: File): List<StringBuilder> {
            val folder = File(Objects.requireNonNull(path.parent))
            val listOfFiles = folder.listFiles()
            //assert listOfFiles != null;
            if (listOfFiles != null) {
                for (listOfFile in listOfFiles) {
                    if (listOfFile.isFile) {
                        if (listOfFile.name.contains("PAR")) {
                            try {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    return Files.lines(Paths.get(listOfFile.path)).map { str: String? ->
                                        StringBuilder(
                                            str!!
                                        )
                                    }.collect(Collectors.toList())
                                }
                            } catch (e: IOException) {
                                e.printStackTrace()
                                return ArrayList()
                            }
                        }
                    }
                }
            }
            return ArrayList()
        }
    }
}