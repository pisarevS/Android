package pisarev.com.modeling.mvp.model;

import android.content.Context;
import android.graphics.Canvas;

import javax.inject.Inject;

import pisarev.com.modeling.interfaces.IDraw;
import pisarev.com.modeling.interfaces.ViewMvp;

public class Draw extends BaseDraw implements IDraw {

    private boolean isHorizontalAxis;
    private boolean isVerticalAxis;
    private boolean isCR;
    @Inject
    Context context;

    public Draw(ViewMvp.MyViewMvp myViewMvp) {
        super(myViewMvp);
    }

    @Override
    public void drawContour(Canvas canvas, Point pointCoordinateZero, float zoom, int index) {
        StringBuffer frame;
        Point pStart = new Point();
        Point pEnd = new Point();
        pStart.x = 650f;
        pStart.z = 250f;
        pEnd.x = 650f;
        pEnd.z = 250f;
        float radius = 0;
        selectCoordinateSystem(programList);

        for (int i = 0; i < index; i++) {
            frame = programList.get(i);
            containsGCode(frame.toString());
            if (contains(frame, horizontalAxis + "=IC")) {
                pEnd.x = pEnd.x + incrementSearch(frame, horizontalAxis + "=IC");
                isHorizontalAxis = true;
            } else if (contains(frame, horizontalAxis)) {
                float tempHorizontal = coordinateSearch(frame, horizontalAxis);
                if (tempHorizontal != FIBO) {
                    pEnd.x = tempHorizontal;
                    isHorizontalAxis = true;
                } else {
                    myViewMvp.showError("Error " + horizontalAxis + "\n" + frame.toString());
                }
            }
            if (contains(frame, verticalAxis + "=IC")) {
                pEnd.z = pEnd.z + incrementSearch(frame, verticalAxis + "=IC");
                isVerticalAxis = true;
            } else if (contains(frame, verticalAxis)) {
                float tempVertical = coordinateSearch(frame, verticalAxis);
                if (tempVertical != FIBO) {
                    pEnd.z = tempVertical;
                    isVerticalAxis = true;
                } else {
                    myViewMvp.showError("Error " + verticalAxis + "\n" + frame.toString());
                }
            }
            String radiusCR = "CR";
            if (contains(frame, "CR")) {
                float tempCR = coordinateSearch(frame, radiusCR);
                if (tempCR != FIBO) {
                    radius = tempCR;
                    isCR = true;
                } else {
                    myViewMvp.showError("Error " + radiusCR + "\n" + frame.toString());
                }
            }
            if (isHorizontalAxis && isVerticalAxis && isCR) {
                drawArc(canvas, line, pointCoordinateZero, pStart, pEnd, radius, zoom, clockwise);
                pStart.x = pEnd.x;
                pStart.z = pEnd.z;
                isHorizontalAxis = false;
                isVerticalAxis = false;
                isCR = false;
            }
            if (isHorizontalAxis || isVerticalAxis) {
                drawLine(canvas, line, pointCoordinateZero, pStart, pEnd, zoom);
                pStart.x = pEnd.x;
                pStart.z = pEnd.z;
                isHorizontalAxis = false;
                isVerticalAxis = false;
            }
        }
        drawPoint(canvas, pointCoordinateZero, pEnd, zoom);
    }
}

