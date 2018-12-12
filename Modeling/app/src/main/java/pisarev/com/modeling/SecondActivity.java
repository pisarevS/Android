package pisarev.com.modeling;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.saber.chentianslideback.SlideBackActivity;

public class SecondActivity extends SlideBackActivity {

    private MyView myView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN );
        //getWindow().addFlags( WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED );
        //setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE );
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_second );
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

    @Override
    protected void slideBackSuccess(){
        Intent intent=new Intent( SecondActivity.this,FirstActivity.class );
        startActivity( intent );
        finish();
    }
}
