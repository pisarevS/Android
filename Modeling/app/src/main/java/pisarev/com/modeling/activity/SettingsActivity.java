package pisarev.com.modeling.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import pisarev.com.modeling.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_setting );
        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
    }

}
