package pisarev.com.modeling.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import pisarev.com.modeling.mvp.ViewMvp;

import pisarev.com.modeling.mvp.view.customview.MyView;
import pisarev.com.modeling.R;


public class SecondActivity extends AppCompatActivity implements ViewMvp.SecondViewMvp,View.OnTouchListener {

    private MyView myView;
    private ImageView start;
    private ImageView singleBlock;
    private ImageView reset;
    private int count=0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN );
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_second );
        myView=findViewById( R.id.myView );
        start=findViewById( R.id.start );
        singleBlock =findViewById( R.id.single_block );
        reset=findViewById( R.id.reset );
        start.setOnTouchListener(this);
        singleBlock.setOnTouchListener(this);
        reset.setOnTouchListener(this);
        MyView.button=MyView.RESET;
        myView.invalidate();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (v.getId()){
            case R.id.start:
                MyView.button=MyView.START;
                myView.invalidate();
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        start.setImageResource( R.drawable.sysle_start_down );
                        break;
                    case MotionEvent.ACTION_UP:
                        start.setImageResource( R.drawable.sysle_start );
                        break;
                }
                break;
            case R.id.single_block:
                    if(event.getAction()==MotionEvent.ACTION_DOWN){
                        count++;
                        if(count%2!=0){
                            singleBlock.setImageResource( R.drawable.single_block_down );
                        }else {
                            singleBlock.setImageResource( R.drawable.single_block );
                        }
                    }
                break;
            case R.id.reset:
                MyView.button=MyView.RESET;
                myView.invalidate();
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        reset.setImageResource( R.drawable.reset_down );
                        break;
                    case MotionEvent.ACTION_UP:
                        reset.setImageResource( R.drawable.reset );
                        break;
                }
                break;
        }
        return true;
    }
}
