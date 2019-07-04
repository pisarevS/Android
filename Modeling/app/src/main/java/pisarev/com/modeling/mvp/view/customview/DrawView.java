package pisarev.com.modeling.mvp.view.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import pisarev.com.modeling.application.App;
import pisarev.com.modeling.interfaces.IDraw;
import pisarev.com.modeling.interfaces.ViewMvp;
import pisarev.com.modeling.mvp.model.Const;
import pisarev.com.modeling.mvp.model.MyData;
import pisarev.com.modeling.mvp.model.Point;
import pisarev.com.modeling.mvp.model.Draw;

public class DrawView extends SurfaceView implements SurfaceHolder.Callback, ViewMvp.MyViewMvp {
    private Paint paintCoordinateDottedLine;
    private Point pointCoordinateZero = new Point();
    private boolean isTouch;
    private float moveX;
    private float moveZ;
    private float zoom = 3;
    public static int button;
    public final static int START = 1;
    public final static int RESET = 2;
    public static int index;
    private ScaleGestureDetector scaleGestureDetector;
    private DrawThread drawThread;
    @Inject
    MyData data;


    public DrawView(Context context) {
        super( context );
        init();
        scaleGestureDetector = new ScaleGestureDetector( context, new ScaleListener() );
        getHolder().addCallback(this);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super( context, attrs );
        init();
        scaleGestureDetector = new ScaleGestureDetector( context, new ScaleListener() );
        getHolder().addCallback(this);
    }

    private void manager(Canvas canvas) {
        IDraw draw = new Draw( this );
        draw.drawContour( canvas, pointCoordinateZero, zoom, index );
    }

    private void drawSystemCoordinate(Canvas canvas, boolean isTouch, int button) {
        if (!isTouch || button == RESET) {
            initSystemCoordinate( canvas, true );
        } else {
            initSystemCoordinate( canvas, false );
            if (button == START)
                manager( canvas );
        }
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
                break;
            case MotionEvent.ACTION_MOVE:
                pointCoordinateZero.setX( event.getX() + moveX );
                pointCoordinateZero.setZ( event.getY() + moveZ );
                break;
        }
        return true;
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

    private void init() {
        paintCoordinateDottedLine = new Paint();
        paintCoordinateDottedLine.setColor( Color.GRAY );
        paintCoordinateDottedLine.setStyle( Paint.Style.STROKE );
        paintCoordinateDottedLine.setAntiAlias( true );
        paintCoordinateDottedLine.setPathEffect( new DashPathEffect( new float[]{20f, 10f}, 0f ) );
        App.getComponent().inject( this );
    }

    @Override
    public void showError(String error) {
        if (!data.getErrorList().contains( error )) {
            data.setErrorList( error );
            Toast.makeText( getContext(), error, Toast.LENGTH_LONG ).show();
        }
    }

    public void refresh() {
        if (getHolder().getSurface() == null) {
            // preview surface does not exist
            return;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new DrawThread(getHolder());
        drawThread.setRunning(true);
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        drawThread.setRunning(false);
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    class DrawThread extends Thread
    {

        private boolean running = false;
        private SurfaceHolder surfaceHolder;

        public DrawThread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
        }

        public void setRunning(boolean running) {
            this.running = running;
        }

        @Override
        public void run() {
            Canvas canvas;
            while (running) {
                refresh();
                canvas = null;
                try {
                    canvas = surfaceHolder.lockCanvas(null);
                    synchronized (surfaceHolder){
                        if (canvas == null)
                            continue;
                        canvas.drawColor( Color.BLACK);
                        switch (button) {
                            case START:

                                manager( canvas );
                                break;
                            case RESET:
                                initSystemCoordinate( canvas, true );
                                button = 0;
                                break;
                        }
                    }
                    drawSystemCoordinate( canvas, isTouch, button );


                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }
    }

    public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            zoom *= detector.getScaleFactor();
            return true;
        }
    }
}
