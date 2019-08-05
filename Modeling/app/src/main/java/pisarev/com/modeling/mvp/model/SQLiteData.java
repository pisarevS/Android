package pisarev.com.modeling.mvp.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import pisarev.com.modeling.interfaces.ISQLiteData;

public class SQLiteData extends SQLiteOpenHelper implements ISQLiteData {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_PROGRAM = "MyDataBaseProgram";
    public static final String DATABASE_PARAMETER = "MyDataBaseParameter";
    private static final String TABLE_PROGRAM = "TableProgram";
    public static final String KEY_PROGRAM = "program";

    public SQLiteData(@Nullable Context context,String base) {
        super( context, base, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( "CREATE TABlE " + TABLE_PROGRAM + "("
                + KEY_PROGRAM + " text)" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_PROGRAM );
        onCreate( db );
    }

    @Override
    public void setProgramText(Map<String, String> programs) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PROGRAM, programs.get( KEY_PROGRAM ));
        db.insert( TABLE_PROGRAM, null, values );
        db.close();
    }

    @Override
    public Map<String, String> getProgramText() {
        Map<String,String>programs=new HashMap<>(  );
        String selectQuery = "SELECT  * FROM " + TABLE_PROGRAM;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery( selectQuery, null );
        if (cursor.moveToFirst()) {
            do {
                programs.put( KEY_PROGRAM,cursor.getString( 0 ) );
            } while (cursor.moveToNext());
        }
        return programs;
    }

    @Override
    public void deleteProgramText() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete( TABLE_PROGRAM,null,null );
    }


}
