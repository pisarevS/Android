package pisarev.com.modeling.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import pisarev.com.modeling.Fragments.ParameterFragment;
import pisarev.com.modeling.Fragments.ProgramFragment;
import pisarev.com.modeling.MyCollection;
import pisarev.com.modeling.R;
import pisarev.com.modeling.SectionsPageAdapter;

public class MainActivity extends AppCompatActivity {

    private SectionsPageAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public static final String PROGRAM="PROGRAM";
    public static final String PARAMETER="PARAMETER";
    final String LOG_TAG = "myLogs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        mSectionsPagerAdapter = new SectionsPageAdapter( getSupportFragmentManager() );

        mViewPager = (ViewPager) findViewById( R.id.container );
        mViewPager.setAdapter( mSectionsPagerAdapter );

        TabLayout tabLayout = (TabLayout) findViewById( R.id.tabs );

        mViewPager.addOnPageChangeListener( new TabLayout.TabLayoutOnPageChangeListener( tabLayout ) );
        tabLayout.addOnTabSelectedListener( new TabLayout.ViewPagerOnTabSelectedListener( mViewPager ) );

        FloatingActionButton fab = (FloatingActionButton) findViewById( R.id.fab );
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent( MainActivity.this,SecondActivity.class );
                startActivity( intent );
                finish();
            }
        } );
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState( savedInstanceState );

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState( outState );


        Log.d(LOG_TAG, "onSaveInstanceState");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_main, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Bundle args=new Bundle(  );

        if (id == R.id.action_openProgram) {
            MyCollection.setProgramArrayList( "2222" );
            MyCollection.setProgramArrayList( "3333" );
            MyCollection.setProgramArrayList( "4444" );
            ProgramFragment programFragment=new ProgramFragment();
            args.putBoolean( PROGRAM,true );
            programFragment.setArguments( args );
            return true;
        }
        if (id == R.id.action_openParameter) {
            ParameterFragment parameterFragment=new ParameterFragment();
            args.putBoolean( PARAMETER,true );
            parameterFragment.setArguments( args );
            return true;
        }
        if (id == R.id.action_exit) {
            this.finish();
            return true;
        }
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected( item );
    }
}
