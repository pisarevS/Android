package pisarev.com.modeling.mvp.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import pisarev.com.modeling.interfaces.ISQLiteData;

public class SQLiteData extends SQLiteOpenHelper implements ISQLiteData {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contactsManager";
    private static final String TABLE_CONTACTS = "contacts";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO = "phone_number";

    public SQLiteData(@Nullable Context context) {
        super( context, KEY_NAME, null, DATABASE_VERSION );
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void setProgramText(String text) {

    }

    @Override
    public void setParameterText(String text) {

    }

    @Override
    public String getProgramText() {
        return null;
    }

    @Override
    public String getParameterText() {
        return null;
    }
}
