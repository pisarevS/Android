package pisarev.com.modeling.mvp.view.customview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.*;

import pisarev.com.modeling.interfaces.DrawMvp;
import pisarev.com.modeling.interfaces.Drawing;
import pisarev.com.modeling.interfaces.IDraw;
import pisarev.com.modeling.mvp.model.*;

import static android.view.MotionEvent.*;

public class DrawView extends View implements IDraw, DrawMvp.PresenterDrawViewMvp {
    private Paint paintCoordinateDottedLine;
    private DrawMvp.DrawViewMvp drawActivity;
    private Point pointSystemCoordinate;
    private boolean isTouch;
    private boolean isMove;
    private float moveX;
    private float moveZ;
    private float zooming = 3;
    public int button;
    public final int START = 1;
    public final int RESET = 2;
    public final int STOP = 3;
    public final int TAP = 4;
    private int index;
    private int numberLine;
    private ScaleGestureDetector scaleGestureDetector;
    private ArrayList<String> errorList;
    private boolean isSingleBlockDown = false;
    private boolean isResetDown = false;
    private boolean isStartDown = false;
    private final String TEG = getClass().getName();
    private MyData data;
    private Drawing drawing;
    private boolean isDrawPoint = false;

    public DrawView(Context context) {
        super(context);
        init(context);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        data = new MyData();
        paintCoordinateDottedLine = new Paint();
        paintCoordinateDottedLine.setColor(Color.LTGRAY);
        paintCoordinateDottedLine.setStyle(Paint.Style.STROKE);
        paintCoordinateDottedLine.setAntiAlias(true);
        paintCoordinateDottedLine.setPathEffect(new DashPathEffect(new float[]{20f, 10f}, 0f));
        pointSystemCoordinate = new Point();
        errorList = new ArrayList<>();
        if (myPreferences.getBoolean("RADIOBUTTON", false)) {
            drawing = new DrawVerticalTurning(this);
        } else {
            drawing = new DrawHorizontalTurning(this);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (button) {
            case START:
            case STOP:
                drawing.drawContour(canvas,data, pointSystemCoordinate, zooming, index);
                invalidate();
                break;
            case TAP:
                drawing.setNumberLine(numberLine);
                drawing.drawContour(canvas,data, pointSystemCoordinate, zooming, index);
                invalidate();
                break;
            case RESET:
                initSystemCoordinate(canvas, true);
                button = 0;
                invalidate();
                isTouch = false;
                drawing.setNumberLine(-1);
                errorList.clear();
                break;
        }
        drawSystemCoordinate(canvas, isTouch);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case ACTION_DOWN:
                float downX = event.getX();
                float downZ = event.getY();
                moveX = pointSystemCoordinate.getX() - downX;
                moveZ = pointSystemCoordinate.getZ() - downZ;
                isTouch = true;
                invalidate();
                break;
            case ACTION_MOVE:
                pointSystemCoordinate.setX(event.getX() + moveX);
                pointSystemCoordinate.setZ(event.getY() + moveZ);
                invalidate();
                isMove=true;
                break;
            case ACTION_UP:
                if(!isMove){
                    Point point = new Point();
                    point.setX((pointSystemCoordinate.getX() - event.getX()) * -1);
                    point.setZ(event.getY());
                    if (point.getZ() > 0) point.setZ(pointSystemCoordinate.getZ() - point.getZ());
                    else point.setZ(pointSystemCoordinate.getZ() + Math.abs(point.getZ()));
                    point.setX(point.getX() / zooming);
                    point.setZ(point.getZ() / zooming);
                    Optional<Frame> frame = getFrame(point);
                    if (frame.isPresent()) {
                        numberLine=frame.get().getId();
                        button=TAP;
                        invalidate();
                        drawActivity.showFrame(data.getProgramList().get(frame.get().getId()).toString());
                        drawActivity.showAxis("X=" + frame.get().getX(), "Z=" + frame.get().getZ());
                        System.out.println("X="+point.getX());
                        System.out.println("Z="+point.getZ());
                        System.out.println(data.getProgramList().get(frame.get().getId()));
                        isDrawPoint = true;
                    } else if (isDrawPoint) {
                        numberLine=-1;
                        button=TAP;
                        drawActivity.showFrame("");
                        drawActivity.showAxis("", "");
                        isDrawPoint = false;
                    }
                }
                isMove=false;
                invalidate();
                break;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Optional<Frame> getFrame(Point point) {
        int side = 20;
        Rect rect = new Rect();
        rect.setRect(point.getX() - (side >> 1), point.getZ() - (side >> 1), side, side);
        return data.getFrameList().stream()
                .filter(p -> rect.isInsideRect(p.getX(), p.getZ()))
                .min(Comparator.comparingDouble(p -> Math.abs(point.getX() - p.getX()) + Math.abs(point.getZ() - p.getZ())));
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
                    .setNegativeButton("OK", (dialog, which) -> dialog.cancel());
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            Log.d(TEG, "error " + error);
        }
    }


    private void drawSystemCoordinate(Canvas canvas, boolean isTouch) {
        if (!isTouch || button == RESET) {
            initSystemCoordinate(canvas, true);
            invalidate();
        }
        if (isTouch || button == START) {
            initSystemCoordinate(canvas, false);
            drawing.drawContour(canvas,data, pointSystemCoordinate, zooming, index);
            invalidate();
        }

    }

    private void initSystemCoordinate(Canvas canvas, boolean isInit) {
        Path path;
        if (isInit) {
            path = new Path();
            pointSystemCoordinate.setX(getWidth() >> 1);
            pointSystemCoordinate.setZ(getHeight() >> 1);
            path.moveTo(0, pointSystemCoordinate.getZ());
            path.lineTo(getWidth(), pointSystemCoordinate.getZ());
            path.moveTo(pointSystemCoordinate.getX(), 0);
            path.lineTo(pointSystemCoordinate.getX(), getHeight());
            canvas.drawPath(path, paintCoordinateDottedLine);
        } else {
            path = new Path();
            path.moveTo(0, pointSystemCoordinate.getZ());
            path.lineTo(getWidth(), pointSystemCoordinate.getZ());
            path.moveTo(pointSystemCoordinate.getX(), 0);
            path.lineTo(pointSystemCoordinate.getX(), getHeight());
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
                drawActivity.showIndex(index);
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
                            drawActivity.showIndex(index);
                        }
                    } else {
                        isResetDown = false;
                        timer.cancel();
                        if (button == STOP) {
                            drawActivity.showFrame((data.getProgramList().get(data.getFrameList().get(index - 1).getId())).toString());
                            if (data.getFrameList().get(index - 1).isAxisContains()) {
                                drawActivity.showAxis("X=" + data.getFrameList().get(index - 1).getX(), "Z=" + data.getFrameList().get(index - 1).getZ());
                                drawActivity.showIndex(index);
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
    public void setActivity(DrawMvp.DrawViewMvp secondView) {
        this.drawActivity = secondView;
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public void setData(MyData data) {
        this.data = data;
    }

    public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            zooming *= detector.getScaleFactor();
            invalidate();
            return true;
        }
    }
}
