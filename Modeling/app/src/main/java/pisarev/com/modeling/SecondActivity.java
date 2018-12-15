package pisarev.com.modeling;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.saber.chentianslideback.SlideBackActivity;


public class SecondActivity extends SlideBackActivity implements View.OnTouchListener {

    private MyView myView;
    private ImageView start;
    private ImageView singleBlock;
    private ImageView reset;
    private int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN );
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_second );
        myView=(MyView)findViewById( R.id.myView );
        start=(ImageView)findViewById( R.id.start );
        singleBlock =(ImageView)findViewById( R.id.single_block );
        reset=(ImageView)findViewById( R.id.reset );
        start.setOnTouchListener(this);
        singleBlock.setOnTouchListener(this);
        reset.setOnTouchListener(this);
    }

    @Override
    protected void slideBackSuccess(){
        Intent intent=new Intent( SecondActivity.this,FirstActivity.class );
        startActivity( intent );
        finish();
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
