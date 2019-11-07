package pisarev.com.modeling.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;

import pisarev.com.modeling.R;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private  SharedPreferences.Editor myEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_setting );
        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences( this );
        myEdit= myPreferences.edit();
        RadioButton radioButtonHorizontal = findViewById( R.id.radioButtonHorizontal );
        RadioButton radioButtonVertical = findViewById( R.id.radioButtonVertical );
        radioButtonHorizontal.setOnClickListener( this );
        radioButtonVertical.setOnClickListener( this );
        if(myPreferences.getBoolean( "RADIOBUTTON",false )){
            radioButtonHorizontal.setChecked( true );
        }else {
            radioButtonVertical.setChecked( true );
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.radioButtonHorizontal:
                myEdit.putBoolean( "RADIOBUTTON",true );
                myEdit.commit();
                break;
            case R.id.radioButtonVertical:
                myEdit.putBoolean( "RADIOBUTTON",false );
                myEdit.commit();
                break;
        }
    }
}
