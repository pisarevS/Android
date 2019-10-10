package pisarev.com.modeling.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.obsez.android.lib.filechooser.ChooserDialog;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import pisarev.com.modeling.interfaces.MainMvp;
import pisarev.com.modeling.mvp.model.Program;
import pisarev.com.modeling.mvp.model.SQLiteData;
import pisarev.com.modeling.mvp.presenter.PresenterMainImpl;
import pisarev.com.modeling.R;
import pisarev.com.modeling.adapter.SectionsPageAdapter;
import pisarev.com.modeling.mvp.view.fragments.ProgramFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MainMvp.ViewMvp {

    private MainMvp.PresenterMainMvp presenter;
    private ProgramFragment programFragment;
    private String program;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new PresenterMainImpl(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SectionsPageAdapter mSectionsPagerAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
        programFragment = (ProgramFragment) mSectionsPagerAdapter.getItem(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        java.io.File sdcard = Environment.getExternalStorageDirectory();
        switch (id) {
            case R.id.action_openProgram:
                new ChooserDialog(MainActivity.this)
                        .withStartFile(sdcard.getPath())
                        .withChosenListener(new ChooserDialog.Result() {
                            @Override
                            public void onChoosePath(String path, File pathFile) {
                                Toast.makeText(MainActivity.this, "FILE: " + path, Toast.LENGTH_SHORT).show();
                                presenter.openProgram(path);
                                Map<String, String> stringMap = new HashMap<>();
                                stringMap.put(SQLiteData.KEY_PROGRAM, path);
                                new SQLiteData(getApplication(), SQLiteData.DATABASE_PATH).setProgramText(stringMap);
                            }
                        })
                        // to handle the back key pressed or clicked outside the dialog:
                        .withOnCancelListener(new DialogInterface.OnCancelListener() {
                            public void onCancel(DialogInterface dialog) {
                                Log.d("CANCEL", "CANCEL");
                                dialog.cancel(); // MUST have
                            }
                        })
                        .build()
                        .show();
                return true;
            case R.id.convert:
                Thread thread = new Thread(new Program(true,new SQLiteData(this, SQLiteData.DATABASE_PROGRAM).getProgramText().get(SQLiteData.KEY_PROGRAM),this));
                thread.start();
                programFragment.setText(program);
                return true;
            case R.id.action_exit:
                new SQLiteData(this, SQLiteData.DATABASE_PROGRAM).deleteProgramText();
                new SQLiteData(this, SQLiteData.DATABASE_PARAMETER).deleteProgramText();
                System.exit(0);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgram(String program) {
        programFragment.setText(program);
    }

    public void showConvert(String text) {
        program=text;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, DrawActivity.class);
        startActivity(intent);
    }


}
