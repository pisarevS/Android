package pisarev.com.modeling.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import pisarev.com.modeling.application.App;

import pisarev.com.modeling.mvp.model.Const;
import pisarev.com.modeling.mvp.model.MyData;
import pisarev.com.modeling.mvp.view.customview.DrawView;
import pisarev.com.modeling.R;

import javax.inject.Inject;


public class SecondActivity extends AppCompatActivity implements View.OnTouchListener {

    private DrawView drawView;
    private ImageView buttonCycleStart;
    private ImageView buttonSingleBlock;
    private ImageView buttonReset;
    private TextView textViewFrame;
    private boolean isSingleBlockDown = false;
    private boolean isResetDown = false;
    private boolean isStartDown = false;
    private int count = 0;
    Vibrator vibrator;
    Timer timer;
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
        textViewFrame = findViewById( R.id.textViewCadr );
        buttonCycleStart = findViewById( R.id.start );
        buttonSingleBlock = findViewById( R.id.single_block );
        buttonReset = findViewById( R.id.reset );
        buttonCycleStart.setOnTouchListener( this );
        buttonSingleBlock.setOnTouchListener( this );
        buttonReset.setOnTouchListener( this );
        DrawView.button = DrawView.RESET;
        drawView.invalidate();
        vibrator = (Vibrator) getSystemService( getApplicationContext().VIBRATOR_SERVICE );
        if(savedInstanceState!=null)
        DrawView.button=savedInstanceState.getInt("key");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.start:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        buttonCycleStart.setImageResource( R.drawable.cycle_start_down );

                        if (isSingleBlockDown && DrawView.index < data.getProgramList().size()) {
                            isResetDown = false;
                            DrawView.button = DrawView.START;
                            DrawView.index++;
                            textViewFrame.setText( data.getProgramListTextView().get( DrawView.index - 1 ) );
                            if (vibrator.hasVibrator()) {
                                vibrator.vibrate( 20 );
                            }
                        }
                        if (!isSingleBlockDown && DrawView.index < data.getProgramList().size() && !isStartDown) {
                            if (vibrator.hasVibrator()) {
                                vibrator.vibrate( 20 );
                            }
                            isResetDown = false;
                            isStartDown = true;
                            DrawView.button = DrawView.START;
                            timer = new Timer();
                            timer.schedule( new TimerTask() {
                                @Override
                                public void run() {
                                    if (DrawView.index < data.getProgramList().size() && !isSingleBlockDown && !isResetDown&&DrawView.button==DrawView.START) {
                                        DrawView.index++;
                                        SecondActivity.this.runOnUiThread( new Runnable() {
                                            @Override
                                            public void run() {
                                                if(DrawView.button == DrawView.START)
                                                textViewFrame.setText( data.getProgramListTextView().get( DrawView.index - 1 ) );
                                            }
                                        } );
                                    } else {
                                        isResetDown = false;
                                        timer.cancel();
                                        SecondActivity.this.runOnUiThread( new Runnable() {
                                            @Override
                                            public void run() {
                                                if(DrawView.button == DrawView.PAUSE){
                                                    textViewFrame.setText( data.getProgramListTextView().get( DrawView.index-1 ) );
                                                }
                                            }
                                        } );

                                    }
                                }
                            }, 1000, 200 );
                        }
                        break;
                    case MotionEvent.ACTION_UP:
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
                        isSingleBlockDown = true;
                        buttonSingleBlock.setImageResource( R.drawable.single_block_down );
                    } else {
                        if (vibrator.hasVibrator()) {
                            vibrator.vibrate( 20 );
                        }
                        isStartDown = false;
                        isSingleBlockDown = false;
                        buttonSingleBlock.setImageResource( R.drawable.single_block );
                    }
                }
                break;
            case R.id.reset:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!isResetDown) {
                            if (vibrator.hasVibrator()) {
                                vibrator.vibrate( 20 );
                            }
                            data.getErrorList().clear();
                            DrawView.button = DrawView.RESET;
                            timer.cancel();
                            isStartDown = false;
                            isResetDown = true;
                            textViewFrame.setText( "" );
                            DrawView.index = 0;
                        }
                        buttonReset.setImageResource( R.drawable.reset_down );
                        break;
                    case MotionEvent.ACTION_UP:
                        buttonReset.setImageResource( R.drawable.reset );
                        break;
                }
                break;
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.d(Const.TEG," "+DrawView.button);
        outState.putInt("key",DrawView.button);
    }
}
