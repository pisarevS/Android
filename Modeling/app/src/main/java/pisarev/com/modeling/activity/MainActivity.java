package pisarev.com.modeling.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.obsez.android.lib.filechooser.ChooserDialog;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import pisarev.com.modeling.interfaces.MainMvp;
import pisarev.com.modeling.mvp.model.StyleText;
import pisarev.com.modeling.mvp.model.SQLiteData;
import pisarev.com.modeling.mvp.presenter.PresenterMainImpl;
import pisarev.com.modeling.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, MainMvp.ViewMvp {

    private MainMvp.PresenterMainMvp presenter;
    private EditText editText;
    private SQLiteData sqLiteData;

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        presenter = new PresenterMainImpl( this );
        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        FloatingActionButton fab = findViewById( R.id.fab );
        fab.setOnClickListener( this );
        editText = findViewById( R.id.editText );
        editText.setBackgroundColor(Color.rgb(64,64,64));
        editText.setRawInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        editText.requestFocus();

        sqLiteData = new SQLiteData( this, SQLiteData.DATABASE_PROGRAM );
        editText.setText( sqLiteData.getProgramText().get( SQLiteData.KEY_PROGRAM ) );

        StyleText.setStyle(editText);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_main, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        java.io.File sdcard = Environment.getExternalStorageDirectory();
        switch (id) {
            case R.id.action_openProgram:
                new ChooserDialog( MainActivity.this )
                        .withStartFile( sdcard.getPath() )
                        .withChosenListener((path, pathFile) -> {
                            Toast.makeText( MainActivity.this, "FILE: " + path, Toast.LENGTH_SHORT ).show();
                            presenter.openProgram( path );
                            Map<String, String> stringMap = new HashMap<>();
                            stringMap.put( SQLiteData.KEY_PROGRAM, path );
                            new SQLiteData( getApplication(), SQLiteData.DATABASE_PATH ).setProgramText( stringMap );
                        })
                        // to handle the back key pressed or clicked outside the dialog:
                        .withOnCancelListener(dialog -> {
                            Log.d( "CANCEL", "CANCEL" );
                            dialog.cancel(); // MUST have
                        })
                        .build()
                        .show();
                return true;
            case R.id.setting:
                Intent intent = new Intent( MainActivity.this, SettingsActivity.class );
                startActivity( intent );
                return true;
            case R.id.action_exit:
                new SQLiteData( this, SQLiteData.DATABASE_PROGRAM ).deleteProgramText();
                new SQLiteData( this, SQLiteData.DATABASE_PARAMETER ).deleteProgramText();
                System.exit( 0 );
                return true;
        }
        return super.onOptionsItemSelected( item );
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void showProgram(String program) {
        editText.setText( program );
        StyleText.setStyle(editText);
    }

    @Override
    public void onClick(View v) {
        if(!editText.getText().toString().equals( "" )) {
            Intent intent = new Intent( MainActivity.this, DrawActivity.class );
            startActivity( intent );
        }else {
            Toast.makeText( this,"the field is empty",Toast.LENGTH_LONG ).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put( SQLiteData.KEY_PROGRAM, editText.getText().toString() );
        sqLiteData.deleteProgramText();
        sqLiteData.setProgramText( stringMap );
    }

    @Override
    public void onPause() {
        super.onPause();
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put( SQLiteData.KEY_PROGRAM, editText.getText().toString() );
        sqLiteData.deleteProgramText();
        sqLiteData.setProgramText( stringMap );
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        StyleText.setStyle(editText);
        editText.requestFocus();
        //editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
    }
}
