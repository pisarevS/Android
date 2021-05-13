package pisarev.com.modeling.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import android.widget.TextView;

import pisarev.com.modeling.interfaces.Callback;
import pisarev.com.modeling.interfaces.DrawMvp;
import pisarev.com.modeling.mvp.model.*;
import pisarev.com.modeling.R;

import java.io.File;
import java.util.*;

public class DrawActivity extends AppCompatActivity implements View.OnTouchListener, DrawMvp.DrawViewMvp, Callback {

    private DrawMvp.PresenterDrawViewMvp drawView;
    private ImageView buttonStart;
    private ImageView buttonCycleStart;
    private ImageView buttonSingleBlock;
    private ImageView buttonReset;
    private TextView textViewFrame, textViewX, textViewZ;
    private Map<String, String> variablesList;
    private int count = 0;
    private Vibrator vibrator;
    private int index;
    private boolean isSingleBlock;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN );
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_draw );
        drawView = findViewById( R.id.myView );
        drawView.setActivity( this );
        drawView.setIndex( index );
        textViewFrame = findViewById( R.id.textViewFrame );
        textViewX = findViewById( R.id.textViewX );
        textViewZ = findViewById( R.id.textViewZ );
        buttonStart = findViewById( R.id.start );
        buttonCycleStart = findViewById( R.id.cycle_start );
        buttonSingleBlock = findViewById( R.id.single_block );
        buttonReset = findViewById( R.id.reset );
        buttonStart.setOnTouchListener( this );
        buttonCycleStart.setOnTouchListener( this );
        buttonSingleBlock.setOnTouchListener( this );
        buttonReset.setOnTouchListener( this );
        vibrator = (Vibrator) getSystemService( VIBRATOR_SERVICE );

        String pathParameter=new SQLiteData( this, SQLiteData.DATABASE_PATH ).getProgramText().get( SQLiteData.KEY_PROGRAM );
        assert pathParameter != null;
        List<StringBuffer> parameterList = MyFile.getParameter(new File(pathParameter));
        readParameterVariables(parameterList);

        Thread thread = new Thread( new Program( new SQLiteData( this, SQLiteData.DATABASE_PROGRAM ).getProgramText().get( SQLiteData.KEY_PROGRAM ),variablesList, this) );
        thread.start();

       /* try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.start:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        drawView.onButtonStart();
                        buttonStart.setImageResource( R.drawable.start_down );
                        break;
                    case MotionEvent.ACTION_UP:
                        if (vibrator.hasVibrator()) {
                            vibrator.vibrate(20);
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
                        isSingleBlock=true;
                        drawView.onButtonSingleBlock(true);
                        buttonSingleBlock.setImageResource( R.drawable.single_block_down );
                    } else {
                        if (vibrator.hasVibrator()) {
                            vibrator.vibrate( 20 );
                        }
                        isSingleBlock=false;
                        drawView.onButtonSingleBlock(false);
                        buttonSingleBlock.setImageResource( R.drawable.single_block );
                    }
                }
                break;
            case R.id.reset:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        drawView.onButtonReset();
                        index = 0;
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
        return true;
    }

    @Override
    public void showFrame(final String frame) {
        DrawActivity.this.runOnUiThread(() -> textViewFrame.setText( frame ));
    }

    @Override
    public void showAxis(final String horizontalAxis, final String verticalAxis) {
        DrawActivity.this.runOnUiThread(() -> {
            textViewX.setText( horizontalAxis );
            textViewZ.setText( verticalAxis );
        });
    }

    @Override
    public void showIndex(int index) {
        this.index = index;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState( outState );
        outState.putInt( "index", index );
        outState.putBoolean( "singleBlock", isSingleBlock);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState( savedInstanceState );
        drawView.setIndex( savedInstanceState.getInt( "index" ) );
        drawView.onButtonSingleBlock(isSingleBlock);
        if(isSingleBlock){
            count++;
            buttonSingleBlock.setImageResource( R.drawable.single_block_down );
        }
        else  buttonSingleBlock.setImageResource( R.drawable.single_block );
    }


    private void readParameterVariables(List<StringBuffer> parameterList) {
        variablesList = new LinkedHashMap<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            parameterList.forEach(p -> {
                if (p.toString().contains(";")) p.delete(p.indexOf(";"), p.length());
                if (p.toString().contains("=")) {
                    int key = 0;
                    for (int j = p.indexOf("=") - 1; j >= 0; j--) {
                        char c = p.charAt(j);
                        if (c == ' ') {
                            key = j;
                            break;
                        }
                    }
                    variablesList.put(
                            p.substring(key, p.indexOf("=")).replace(" ", "")
                            , p.substring(p.indexOf("=") + 1, p.length()).replace(" ", ""));
                }
            });
        }
    }

    @Override
    public void callingBack(MyData data) {
        drawView.setData(data);
    }
}
