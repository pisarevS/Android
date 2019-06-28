package pisarev.com.modeling.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import android.widget.TextView;
import pisarev.com.modeling.application.App;
import pisarev.com.modeling.interfaces.ViewMvp;

import pisarev.com.modeling.mvp.model.ChangeVariables;
import pisarev.com.modeling.mvp.model.MyData;
import pisarev.com.modeling.mvp.view.customview.DrawView;
import pisarev.com.modeling.R;

import javax.inject.Inject;


public class SecondActivity extends AppCompatActivity implements View.OnTouchListener, ViewMvp {

    private DrawView drawView;
    private ImageView start;
    private ImageView singleBlock;
    private ImageView reset;
    private TextView textViewCadr;
    private int count=0;
    private boolean isSingleBlockDown=false;
    @Inject
    MyData data;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN );
        super.onCreate( savedInstanceState );
        App.getComponent().inject( this );
        setContentView( R.layout.activity_second );
        drawView =findViewById( R.id.myView );
        textViewCadr=findViewById(R.id.textViewCadr);
        start=findViewById( R.id.start );
        singleBlock =findViewById( R.id.single_block );
        reset=findViewById( R.id.reset );
        start.setOnTouchListener(this);
        singleBlock.setOnTouchListener(this);
        reset.setOnTouchListener(this);
        DrawView.button= DrawView.RESET;
        drawView.invalidate();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            case R.id.start:
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        start.setImageResource( R.drawable.sysle_start_down );
                        DrawView.button= DrawView.START;
                        if(isSingleBlockDown&& DrawView.index<data.getProgramList().size()){
                            DrawView.index++;
                            textViewCadr.setText(DrawView.index +"  "+data.getProgramListTextView().get(DrawView.index-1));
                        }else DrawView.index=data.getProgramList().size();
                        drawView.invalidate();
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
                            isSingleBlockDown=true;
                            singleBlock.setImageResource( R.drawable.single_block_down );
                        }else {
                            isSingleBlockDown=false;
                            singleBlock.setImageResource( R.drawable.single_block );
                        }
                    }
                break;
            case R.id.reset:
                DrawView.button= DrawView.RESET;
                drawView.invalidate();
                DrawView.index=0;
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        textViewCadr.setText("");
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
