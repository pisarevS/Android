package pisarev.com.modeling.mvp.view.customview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import pisarev.com.modeling.interfaces.DrawMvp;
import pisarev.com.modeling.interfaces.IDraw;
import pisarev.com.modeling.mvp.model.Draw;
import pisarev.com.modeling.mvp.model.MyData;
import pisarev.com.modeling.mvp.model.Point;

import static android.view.MotionEvent.*;

public class DrawView extends View implements IDraw, DrawMvp.PresenterDrawViewMvp {
    private Paint paintCoordinateDottedLine;
    private DrawMvp.DrawViewMvp drawActivity;
    private Point pointCoordinateZero;
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
    private ArrayList<String> errorList;
    private boolean isSingleBlockDown = false;
    private boolean isResetDown = false;
    private boolean isStartDown = false;
    private final String TEG = getClass().getName();
    private MyData data;

    public DrawView(Context context) {
        super(context);
        init();
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    private void init() {
        paintCoordinateDottedLine = new Paint();
        paintCoordinateDottedLine.setColor(Color.LTGRAY);
        paintCoordinateDottedLine.setStyle(Paint.Style.STROKE);
        paintCoordinateDottedLine.setAntiAlias(true);
        paintCoordinateDottedLine.setPathEffect(new DashPathEffect(new float[]{20f, 10f}, 0f));
        pointCoordinateZero = new Point();
        errorList = new ArrayList<>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (button) {
            case START:
            case STOP:
                manager(canvas);
                invalidate();
                break;
            case RESET:
                initSystemCoordinate(canvas, true);
                button = 0;
                invalidate();
                isTouch = false;
                errorList.clear();
                break;
        }
        drawSystemCoordinate(canvas, isTouch);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case ACTION_DOWN:
                float downX = event.getX();
                float downZ = event.getY();
                moveX = pointCoordinateZero.getX() - downX;
                moveZ = pointCoordinateZero.getZ() - downZ;
                isTouch = true;
                invalidate();
                break;
            case ACTION_MOVE:
                pointCoordinateZero.setX(event.getX() + moveX);
                pointCoordinateZero.setZ(event.getY() + moveZ);
                invalidate();
                break;
            case ACTION_UP:
                invalidate();
                break;
        }
        return true;
    }

    @Override
    public void showError(String error) {
        button = STOP;
        if (!errorList.contains(error)) {
            errorList.add(error);
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Error")
                    .setMessage(error)
                    .setCancelable(false)
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            Log.d(TEG, "error " + error);
        }
    }

    private void manager(Canvas canvas) {
        Draw draw = new Draw(this, data);
        draw.drawContour(canvas, pointCoordinateZero, zoom, index);
    }

    private void drawSystemCoordinate(Canvas canvas, boolean isTouch) {
        if (!isTouch || button == RESET) {
            initSystemCoordinate(canvas, true);
            invalidate();
        }
        if (isTouch || button == START) {
            initSystemCoordinate(canvas, false);
            manager(canvas);
            invalidate();
        }

    }

    private void initSystemCoordinate(Canvas canvas, boolean isInit) {
        Path path;
        if (isInit) {
            path = new Path();
            pointCoordinateZero.setX(getWidth() >> 1);
            pointCoordinateZero.setZ(getHeight() >> 1);
            path.moveTo(0, pointCoordinateZero.getZ());
            path.lineTo(getWidth(), pointCoordinateZero.getZ());
            path.moveTo(pointCoordinateZero.getX(), 0);
            path.lineTo(pointCoordinateZero.getX(), getHeight());
            canvas.drawPath(path, paintCoordinateDottedLine);
        } else {
            path = new Path();
            path.moveTo(0, pointCoordinateZero.getZ());
            path.lineTo(getWidth(), pointCoordinateZero.getZ());
            path.moveTo(pointCoordinateZero.getX(), 0);
            path.lineTo(pointCoordinateZero.getX(), getHeight());
            canvas.drawPath(path, paintCoordinateDottedLine);
        }
    }

    @Override
    public void onButtonStart() {
        isStartDown = true;
        index = data.getFrameList().size();
        if (data.getFrameList().size() > 0) {
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
            drawActivity.showFrame((data.getProgramList().get(data.getFrameList().get(index - 1).getId())).toString());
            if (data.getFrameList().get(index - 1).isAxisContains()) {
                drawActivity.showAxis("X=" + data.getFrameList().get(index - 1).getX(), "Z=" + data.getFrameList().get(index - 1).getZ());
            }
        }
        if (!isSingleBlockDown && index < data.getFrameList().size() && !isStartDown) {
            isResetDown = false;
            isStartDown = true;
            button = START;
            final Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (index < data.getFrameList().size() && !isSingleBlockDown && !isResetDown && button == START) {
                        index++;
                        drawActivity.showFrame((data.getProgramList().get(data.getFrameList().get(index - 1).getId())).toString());
                        if (data.getFrameList().get(index - 1).isAxisContains()) {
                            drawActivity.showAxis("X=" + data.getFrameList().get(index - 1).getX(), "Z=" + data.getFrameList().get(index - 1).getZ());
                        }
                    } else {
                        isResetDown = false;
                        timer.cancel();
                        if (button == STOP) {
                            drawActivity.showFrame((data.getProgramList().get(data.getFrameList().get(index - 1).getId())).toString());
                            if (data.getFrameList().get(index - 1).isAxisContains()) {
                                drawActivity.showAxis("X=" + data.getFrameList().get(index - 1).getX(), "Z=" + data.getFrameList().get(index - 1).getZ());
                            }
                        }
                    }
                }
            }, 0, 200);
        }
    }

    @Override
    public void onButtonSingleBlock(boolean isSingleBlockDown) {
        this.isSingleBlockDown = isSingleBlockDown;
        isStartDown = false;
    }

    @Override
    public void onButtonReset() {
        isResetDown = true;
        button = RESET;
        index = 0;
        isStartDown = false;
        drawActivity.showFrame("");
        drawActivity.showAxis("", "");
    }

    @Override
    public void getActivity(DrawMvp.DrawViewMvp secondView) {
        this.drawActivity = secondView;
    }

    @Override
    public void getData(MyData data) {
        this.data = data;
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
