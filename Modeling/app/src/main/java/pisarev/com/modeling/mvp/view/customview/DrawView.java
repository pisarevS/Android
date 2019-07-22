package pisarev.com.modeling.mvp.view.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import pisarev.com.modeling.R;
import pisarev.com.modeling.activity.SecondActivity;
import pisarev.com.modeling.application.App;
import pisarev.com.modeling.interfaces.IDrawView;
import pisarev.com.modeling.interfaces.ISecondView;
import pisarev.com.modeling.interfaces.ViewMvp;
import pisarev.com.modeling.mvp.model.Const;
import pisarev.com.modeling.mvp.model.MyData;
import pisarev.com.modeling.mvp.model.Point;
import pisarev.com.modeling.mvp.model.Draw;

public class DrawView extends View implements ViewMvp.MyViewMvp, IDrawView {
    private Paint paintCoordinateDottedLine;
    private ISecondView secondView;
    private Point pointCoordinateZero = new Point();
    private boolean isTouch;
    private float moveX;
    private float moveZ;
    private float zoom = 3;
    public int button;
    public final int START = 1;
    public final int RESET = 2;
    public final int STOP = 3;
    public int index;
    private ScaleGestureDetector scaleGestureDetector;
    private ArrayList<String>errorList=new ArrayList<>(  );
    private boolean isSingleBlockDown = false;
    private boolean isResetDown = false;
    private boolean isStartDown = false;
    @Inject
    MyData data;

    public DrawView(Context context) {
        super( context );
        init();
        scaleGestureDetector = new ScaleGestureDetector( context, new ScaleListener() );
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super( context, attrs );
        init();
        scaleGestureDetector = new ScaleGestureDetector( context, new ScaleListener() );
    }

    private void init() {
        paintCoordinateDottedLine = new Paint();
        paintCoordinateDottedLine.setColor( Color.GRAY );
        paintCoordinateDottedLine.setStyle( Paint.Style.STROKE );
        paintCoordinateDottedLine.setAntiAlias( true );
        paintCoordinateDottedLine.setPathEffect( new DashPathEffect( new float[]{20f, 10f}, 0f ) );
        App.getComponent().inject( this );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw( canvas );
        switch (button) {
            case START:
                manager( canvas );
                invalidate();
                break;
            case RESET:
                initSystemCoordinate( canvas, true );
                button = 0;
                invalidate();
                isTouch=false;
                errorList.clear();
                break;
            case STOP:
                manager( canvas );
                invalidate();
                break;
        }
        drawSystemCoordinate( canvas, isTouch);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent( event );
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float downX = event.getX();
                float downZ = event.getY();
                moveX = pointCoordinateZero.getX() - downX;
                moveZ = pointCoordinateZero.getZ() - downZ;
                isTouch = true;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                pointCoordinateZero.setX( event.getX() + moveX );
                pointCoordinateZero.setZ( event.getY() + moveZ );
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                invalidate();
                break;
        }
        return true;
    }

    @Override
    public void showError(String error) {
        button=STOP;
        if(!errorList.contains( error )){
            errorList.add( error );
            Toast toast= Toast.makeText( getContext(), error, Toast.LENGTH_LONG );
            toast.setGravity( Gravity.CENTER,0,0 );
            toast.show();
            Log.d(Const.TEG, "error " +error );
        }
    }

    private void manager(Canvas canvas) {
        Draw draw = new Draw( this );
        draw.drawContour( canvas, pointCoordinateZero, zoom, index );
    }

    private void drawSystemCoordinate(Canvas canvas, boolean isTouch) {
        if (!isTouch|| button == RESET) {
            initSystemCoordinate( canvas, true );
            invalidate();
        }
        if (isTouch|| button == START){
            initSystemCoordinate( canvas, false );
            manager( canvas );
            invalidate();
        }

    }

    private void initSystemCoordinate(Canvas canvas, boolean isInit) {
        Path path;
        if (isInit) {
            path = new Path();
            pointCoordinateZero.setX( getWidth() >> 1 );
            pointCoordinateZero.setZ( getHeight() >> 1 );
            path.moveTo( 0, pointCoordinateZero.getZ() );
            path.lineTo( getWidth(), pointCoordinateZero.getZ() );
            path.moveTo( pointCoordinateZero.getX(), 0 );
            path.lineTo( pointCoordinateZero.getX(), getHeight() );
            canvas.drawPath( path, paintCoordinateDottedLine );
        } else {
            path = new Path();
            path.moveTo( 0, pointCoordinateZero.getZ() );
            path.lineTo( getWidth(), pointCoordinateZero.getZ() );
            path.moveTo( pointCoordinateZero.getX(), 0 );
            path.lineTo( pointCoordinateZero.getX(), getHeight() );
            canvas.drawPath( path, paintCoordinateDottedLine );
        }
    }

    @Override
    public void onButtonStart(boolean isStartDown) {
        this.isStartDown=isStartDown;
        index=data.getFrameList().size();
        if(data.getFrameList().size()>0){
            button = START;
            isResetDown = false;
        }
    }

    @Override
    public void onButtonCycleStart() {
        if (isSingleBlockDown && index < data.getFrameList().size()) {
            isResetDown = false;
            button = START;
            index++;
            secondView.showFrame((data.getProgramList().get(data.getFrameList().get( index-1 ).getId() )).toString() );
            if(data.getFrameList().get( index-1  ).isAxisContains()){
                secondView.showAxis("X=" +data.getFrameList().get( index-1 ).getX(),"Z=" +data.getFrameList().get( index-1 ).getZ());
            }
        }

        if (!isSingleBlockDown && index < data.getFrameList().size() && !isStartDown) {
            isResetDown = false;
            isStartDown = true;
            button = START;
            final Timer timer = new Timer();
            timer.schedule( new TimerTask() {
                @Override
                public void run() {
                    if (index < data.getFrameList().size() && !isSingleBlockDown && !isResetDown&&button==START) {
                        index++;
                        if(button==START) {
                            secondView.showFrame((data.getProgramList().get(data.getFrameList().get( index-1 ).getId() )).toString() );
                            if (data.getFrameList().get( index - 1 ).isAxisContains()) {
                                secondView.showAxis("X=" +data.getFrameList().get( index-1 ).getX(),"Z=" +data.getFrameList().get( index-1 ).getZ());
                            }
                        }
                    } else {
                        isResetDown = false;
                        timer.cancel();

                        if(button == STOP){
                            secondView.showFrame((data.getProgramList().get(data.getFrameList().get( index-1 ).getId() )).toString() );
                            if(data.getFrameList().get( index-1  ).isAxisContains()){
                                secondView.showAxis("X=" +data.getFrameList().get( index-1 ).getX(),"Z=" +data.getFrameList().get( index-1 ).getZ());
                            }
                        }
                    }
                }
            }, 1000, 200 );
        }
    }

    @Override
    public void onButtonSingleBlock(boolean isSingleBlockDown) {
        this.isSingleBlockDown=isSingleBlockDown;
        isStartDown = false;
    }

    @Override
    public void onButtonReset( boolean isResetDown) {
        this.isResetDown=isResetDown;
        if (isResetDown) {
            button = RESET;
            index = 0;
            isStartDown = false;
            secondView.showFrame("");
            secondView.showAxis("","");
        }
    }

    @Override
    public void getActivity(ISecondView secondView) {
        this.secondView = secondView;
    }

    public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            zoom *= detector.getScaleFactor();
            invalidate();
            return true;
        }
    }
}
