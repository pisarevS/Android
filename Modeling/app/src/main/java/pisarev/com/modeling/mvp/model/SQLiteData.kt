package pisarev.com.modeling.mvp.model

import android.database.sqlite.SQLiteOpenHelper
import pisarev.com.modeling.interfaces.ISQLiteData
import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import android.annotation.SuppressLint
import android.content.Context
import java.util.HashMap

class SQLiteData(context: Context?, base: String?) : SQLiteOpenHelper(context, base, null, DATABASE_VERSION),
    ISQLiteData {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABlE " + TABLE_PROGRAM + "("
                    + KEY_PROGRAM + " text)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PROGRAM")
        onCreate(db)
    }

    override var programText: Map<String?, String?>?
        get()  {
            val programs: MutableMap<String?, String?> = HashMap()
            val selectQuery = "SELECT  * FROM $TABLE_PROGRAM"
            val db = this.writableDatabase
            @SuppressLint("Recycle") val cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    programs[KEY_PROGRAM] = cursor.getString(0)
                } while (cursor.moveToNext())
            }
            return programs
        }
        set(programs) {
            val db = this.writableDatabase
            val values = ContentValues()
            values.put(KEY_PROGRAM, programs!![KEY_PROGRAM])
            db.insert(TABLE_PROGRAM, null, values)
            db.close()
        }

    override fun deleteProgramText() {
        val db = this.writableDatabase
        db.delete(TABLE_PROGRAM, null, null)
    }

    companion object {
        private const val DATABASE_VERSION = 1
        const val DATABASE_PROGRAM = "MyDataBaseProgram"
        const val DATABASE_PARAMETER = "MyDataBaseParameter"
        const val DATABASE_PATH = "MyDataBasePath"
        private const val TABLE_PROGRAM = "TableProgram"
        const val KEY_PROGRAM = "program"
    }
}