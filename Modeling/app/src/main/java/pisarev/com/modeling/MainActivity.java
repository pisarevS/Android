package pisarev.com.modeling;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    private Canvas canvas;
    private Bitmap bitmap;
    private Paint paint;
    private MyView myView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN );
        getWindow().addFlags( WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED );
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE );
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        myView=(MyView)findViewById( R.id.myView );
    }

    public void onCliCkButtonStart(View v){
        MyView.button=MyView.START;
        myView.invalidate();
    }

    public void onClickButtonReset(View v){
        MyView.button=MyView.RESET;
        myView.invalidate();
    }
}
