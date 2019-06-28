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
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import android.widget.Toast;

import pisarev.com.modeling.interfaces.ViewMvp;
import pisarev.com.modeling.mvp.model.Point;
import pisarev.com.modeling.mvp.model.Draw;

public class DrawView extends View implements ViewMvp.MyViewMvp {
    private Paint paintCoordinateDottedLine;
    private Point pointCoordinateZero = new Point();
    private boolean isTouch;
    private float moveX;
    private float moveZ;
    private float zoom=3;
    public static int button;
    public final static int START = 1;
    public final static int RESET = 2;
    public static int index;
    private ScaleGestureDetector scaleGestureDetector;

    public DrawView(Context context) {
        super( context );
        init();
        scaleGestureDetector=new ScaleGestureDetector(context,new ScaleListener());
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super( context, attrs );
        init();
        scaleGestureDetector=new ScaleGestureDetector(context,new ScaleListener());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw( canvas );
        switch (button) {
            case START:
                manager( canvas );
                break;
            case RESET:
                initSystemCoordinate( canvas, true );
                button = 0;
                break;
        }
        drawSystemCoordinate( canvas, isTouch, button );
    }

    private void manager(Canvas canvas) {
        Draw draw = new Draw(this);
        draw.drawContour( canvas, pointCoordinateZero, zoom,index );
    }

    private void drawSystemCoordinate(Canvas canvas, boolean isTouch, int button) {
        if (!isTouch || button == RESET) {
            initSystemCoordinate( canvas, true );
        } else if (isTouch || button == START) {
            initSystemCoordinate( canvas, false );
            if (button == START && isTouch)
                manager( canvas );
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float downX = event.getX();
                float downZ = event.getY();
                moveX = pointCoordinateZero.x - downX;
                moveZ = pointCoordinateZero.z - downZ;
                isTouch = true;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                pointCoordinateZero.x = event.getX() + moveX;
                pointCoordinateZero.z = event.getY() + moveZ;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                invalidate();
                break;
        }
        return true;
    }

    private void initSystemCoordinate(Canvas canvas, boolean isInit) {
        Path path;
        if (isInit) {
            path = new Path();
            pointCoordinateZero.x = getWidth() / 2;
            pointCoordinateZero.z = getHeight() / 2;
            path.moveTo( 0, pointCoordinateZero.z );
            path.lineTo( getWidth(), pointCoordinateZero.z );
            path.moveTo( pointCoordinateZero.x, 0 );
            path.lineTo( pointCoordinateZero.x, getHeight() );
            canvas.drawPath(path, paintCoordinateDottedLine );
        } else {
            path = new Path();
            path.moveTo( 0, pointCoordinateZero.z );
            path.lineTo( getWidth(), pointCoordinateZero.z );
            path.moveTo( pointCoordinateZero.x, 0 );
            path.lineTo( pointCoordinateZero.x, getHeight() );
            canvas.drawPath(path, paintCoordinateDottedLine );
        }
    }

    private void init() {
        paintCoordinateDottedLine = new Paint();
        paintCoordinateDottedLine.setColor( Color.GRAY );
        paintCoordinateDottedLine.setStyle( Paint.Style.STROKE );
        paintCoordinateDottedLine.setAntiAlias( true );
        paintCoordinateDottedLine.setPathEffect( new DashPathEffect( new float[]{20f, 10f}, 0f ) );
    }

    @Override
    public void showError(String error) {
        Toast.makeText(getContext(),error,Toast.LENGTH_LONG).show();
    }

    public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            zoom*=detector.getScaleFactor();
            invalidate();
            return true;
        }
    }
}
