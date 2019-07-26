package pisarev.com.modeling.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.design.widget.TabLayout;
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

import pisarev.com.modeling.interfaces.MainMvp;
import pisarev.com.modeling.mvp.model.Program;
import pisarev.com.modeling.mvp.presenter.PresenterMainImpl;
import pisarev.com.modeling.R;
import pisarev.com.modeling.adapter.SectionsPageAdapter;
import pisarev.com.modeling.mvp.view.fragments.ParameterFragment;
import pisarev.com.modeling.mvp.view.fragments.ProgramFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MainMvp.ViewMvp {

    private SectionsPageAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private MainMvp.PresenterMainMvp presenter;
    private ParameterFragment parameterFragment;
    private ProgramFragment programFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        mSectionsPagerAdapter = new SectionsPageAdapter( getSupportFragmentManager() );
        mViewPager = findViewById( R.id.container );
        mViewPager.setAdapter( mSectionsPagerAdapter );
        TabLayout tabLayout = findViewById( R.id.tabs );
        mViewPager.addOnPageChangeListener( new TabLayout.TabLayoutOnPageChangeListener( tabLayout ) );
        tabLayout.addOnTabSelectedListener( new TabLayout.ViewPagerOnTabSelectedListener( mViewPager ) );
        FloatingActionButton fab = findViewById( R.id.fab );
        fab.setOnClickListener( this );

        programFragment = (ProgramFragment) mSectionsPagerAdapter.getItem( 0 );
        parameterFragment = (ParameterFragment) mSectionsPagerAdapter.getItem( 1 );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_main, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        presenter = new PresenterMainImpl( this );
        java.io.File sdcard = Environment.getExternalStorageDirectory();
        if (id == R.id.action_openProgram) {
            new ChooserDialog( MainActivity.this )
                    .withStartFile( sdcard.getPath() )
                    .withChosenListener( new ChooserDialog.Result() {
                        @Override
                        public void onChoosePath(String path, File pathFile) {
                            Toast.makeText( MainActivity.this, "FILE: " + path, Toast.LENGTH_SHORT ).show();
                            presenter.openProgram( path );
                        }
                    } )
                    // to handle the back key pressed or clicked outside the dialog:
                    .withOnCancelListener( new DialogInterface.OnCancelListener() {
                        public void onCancel(DialogInterface dialog) {
                            Log.d( "CANCEL", "CANCEL" );
                            dialog.cancel(); // MUST have
                        }
                    } )
                    .build()
                    .show();
            return true;
        }
        if (id == R.id.action_openParameter) {
            new ChooserDialog( MainActivity.this )
                    .withStartFile( sdcard.getPath() )
                    .withChosenListener( new ChooserDialog.Result() {
                        @Override
                        public void onChoosePath(String path, File pathFile) {
                            Toast.makeText( MainActivity.this, "FILE: " + path, Toast.LENGTH_SHORT ).show();
                            presenter.openParameter( path );
                        }
                    } )
                    // to handle the back key pressed or clicked outside the dialog:
                    .withOnCancelListener( new DialogInterface.OnCancelListener() {
                        public void onCancel(DialogInterface dialog) {
                            Log.d( "CANCEL", "CANCEL" );
                            dialog.cancel(); // MUST have
                        }
                    } )
                    .build()
                    .show();
            return true;
        }
        if (id == R.id.action_exit) {
            System.exit( 0 );
            return true;
        }
        return super.onOptionsItemSelected( item );
    }

    @Override
    public void showProgram(String program) {
        programFragment.setText( program );
    }

    @Override
    public void showParameter(String parameter) {
        parameterFragment.setText( parameter );
    }

    @Override
    public void onClick(View v) {
        Thread thread = new Thread( new Program( programFragment.getText(), parameterFragment.getText() ) );
        thread.start();
        Intent intent = new Intent( MainActivity.this, DrawActivity.class );
        startActivity( intent );
    }

}
