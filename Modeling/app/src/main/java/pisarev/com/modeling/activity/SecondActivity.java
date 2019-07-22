package pisarev.com.modeling.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import android.widget.TextView;

import pisarev.com.modeling.application.App;

import pisarev.com.modeling.interfaces.IDrawView;
import pisarev.com.modeling.interfaces.ISecondView;
import pisarev.com.modeling.mvp.model.MyData;
import pisarev.com.modeling.R;

import javax.inject.Inject;

public class SecondActivity extends AppCompatActivity implements View.OnTouchListener, ISecondView {

    private IDrawView drawView;
    private ImageView buttonStart;
    private ImageView buttonCycleStart;
    private ImageView buttonSingleBlock;
    private ImageView buttonReset;
    private TextView textViewFrame,textViewX,textViewZ;
    private int count = 0;
    private Vibrator vibrator;
    @Inject
    MyData data;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN );
        super.onCreate( savedInstanceState );
        App.getComponent().inject( this );
        setContentView( R.layout.activity_second );
        drawView = findViewById( R.id.myView );
        drawView.getActivity(this);
        textViewFrame = findViewById( R.id.textViewFrame );
        textViewX=findViewById( R.id.textViewX );
        textViewZ=findViewById( R.id.textViewZ );
        buttonStart = findViewById( R.id.start );
        buttonCycleStart = findViewById( R.id.cycle_start );
        buttonSingleBlock = findViewById( R.id.single_block );
        buttonReset = findViewById( R.id.reset );
        buttonStart.setOnTouchListener( this );
        buttonCycleStart.setOnTouchListener( this );
        buttonSingleBlock.setOnTouchListener( this );
        buttonReset.setOnTouchListener( this );
        vibrator = (Vibrator) getSystemService( getApplicationContext().VIBRATOR_SERVICE );
    }

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        onClick( v,event );
        return true;
    }

    private void onClick(View v, MotionEvent event){
        switch (v.getId()) {
            case R.id.start:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        drawView.onButtonStart(true);
                        buttonStart.setImageResource( R.drawable.start_down );
                        break;
                    case MotionEvent.ACTION_UP:
                        if (vibrator.hasVibrator()) {
                            vibrator.vibrate( 20 );
                        }
                        buttonStart.setImageResource( R.drawable.start );
                        break;
                }
                break;
            case R.id.cycle_start:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        drawView.onButtonCycleStart();
                        buttonCycleStart.setImageResource( R.drawable.cycle_start_down );
                        break;
                    case MotionEvent.ACTION_UP:
                        if (vibrator.hasVibrator()) {
                            vibrator.vibrate( 20 );
                        }
                        buttonCycleStart.setImageResource( R.drawable.cycle_start );
                        break;
                }
                break;
            case R.id.single_block:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    count++;
                    if (count % 2 != 0) {
                        if (vibrator.hasVibrator()) {
                            vibrator.vibrate( 20 );
                        }
                        drawView.onButtonSingleBlock(true);
                        buttonSingleBlock.setImageResource( R.drawable.single_block_down );
                    } else {
                        if (vibrator.hasVibrator()) {
                            vibrator.vibrate( 20 );
                        }
                        drawView.onButtonSingleBlock(false);
                        buttonSingleBlock.setImageResource( R.drawable.single_block );
                    }
                }
                break;
            case R.id.reset:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        drawView.onButtonReset(true);
                        buttonReset.setImageResource( R.drawable.reset_down );
                        break;
                    case MotionEvent.ACTION_UP:
                        if (vibrator.hasVibrator()) {
                            vibrator.vibrate( 20 );
                        }
                        buttonReset.setImageResource( R.drawable.reset );
                        break;
                }
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState( outState, outPersistentState );
        //outState.putInt( "keyButton",DrawView.button );
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState( savedInstanceState );
        //DrawView.button=savedInstanceState.getInt( "keyButton" );
    }

    @Override
    public void showFrame(final String frame) {
        SecondActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewFrame.setText(frame);
            }
        });
    }

    @Override
    public void showAxis(final String horizontalAxis, final String verticalAxis) {
        SecondActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewX.setText(horizontalAxis );
                textViewZ.setText( verticalAxis );
            }
        });
    }
}
