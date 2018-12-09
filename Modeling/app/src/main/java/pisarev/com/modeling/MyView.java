package pisarev.com.modeling;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MyView extends View {
    private Paint paintCoordinateDottedLine;

    private Bitmap bitmap;
    private Canvas canvas;
    private Draw draw;
    private Path path;
    private Point pointCoordinateZero = new Point();
    private Point pointContour=new Point( );
    private boolean isTouch;
    private float downX;
    private float downZ;
    private float moveX;
    private float moveZ;
    public static int button;
    public final static int START=1;
    public final static int RESET=2;



    public MyView(Context context) {
        super( context );
        init();
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super( context, attrs );
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw( canvas );
        switch (button){
            case START:
                manager( canvas );
                break;
            case RESET:
                initCoordinate( canvas,true );
                button=0;
                break;
        }
        drawSystemCoordinate( canvas,isTouch,button );

    }
    private void manager(Canvas canvas){
        draw=new Draw(  );
        draw.drawContour( canvas,pointCoordinateZero,1 );
    }

    private void drawSystemCoordinate(Canvas canvas,boolean isTouch,int button){
        if (!isTouch||button==RESET) {
            initCoordinate( canvas,true );
        } else if(isTouch||button==START) {
            initCoordinate( canvas,false );
            if(button==START&&isTouch)
            manager( canvas );
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downZ = event.getY();
                moveX = pointCoordinateZero.x - downX;
                moveZ = pointCoordinateZero.z - downZ;
                isTouch = true;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                pointCoordinateZero.x =event.getX()+ moveX;
                pointCoordinateZero.z =event.getY()+ moveZ;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                invalidate();
                break;
        }
        return true;
    }

    private void initCoordinate(Canvas canvas,boolean isInit){
        if(isInit){
            path=new Path( );
            pointCoordinateZero.x= getWidth() / 2 ;
            pointCoordinateZero.z= getHeight() / 2;
            path.moveTo( 0, pointCoordinateZero.z );
            path.lineTo( getWidth(), pointCoordinateZero.z );
            path.moveTo(pointCoordinateZero.x, 0  );
            path.lineTo( pointCoordinateZero.x, getHeight() );
            canvas.drawPath( path, paintCoordinateDottedLine );
        }else {
            path=new Path( );
            path.moveTo( 0, pointCoordinateZero.z );
            path.lineTo( getWidth(), pointCoordinateZero.z );
            path.moveTo(pointCoordinateZero.x, 0  );
            path.lineTo( pointCoordinateZero.x, getHeight() );
            canvas.drawPath( path, paintCoordinateDottedLine );
        }
    }

    private void init() {
        paintCoordinateDottedLine = new Paint();
        paintCoordinateDottedLine.setColor( Color.GRAY );
        paintCoordinateDottedLine.setStyle( Paint.Style.STROKE );
        paintCoordinateDottedLine.setAntiAlias( true );
        paintCoordinateDottedLine.setPathEffect( new DashPathEffect( new float[]{20f, 10f}, 0f ) );

    }
}
