package pisarev.com.modeling.mvp.model;

import android.graphics.Canvas;

import pisarev.com.modeling.interfaces.IDraw;
import pisarev.com.modeling.mvp.model.base.BaseDraw;

public class DrawHorizontalTurning extends BaseDraw {

    public DrawHorizontalTurning(IDraw draw, MyData data) {
        super(draw, data);
    }

    @Override
    public void drawContour(Canvas canvas, Point pointCoordinateZero, float zoom, int index) {
        boolean isLine = false;
        boolean isRadius = false;
        Point pStart = new Point();
        Point pEnd = new Point();
        pStart.setX(650f);
        pStart.setZ(250f);
        pEnd.setX(650f);
        pEnd.setZ(250f);

        float radius = 0;
        for (int i = 0; i < index; i++) {
            isG17 = isG17(frameList.get(i).getGCode());
            checkGCode(frameList.get(i).getGCode());
            if (data.getErrorListMap().containsKey(frameList.get(i).getId())) {
                draw.showError(data.getErrorListMap().get(frameList.get(i).getId()));
                break;
            } else {
                if (frameList.get(i).getIsCR()) {
                    pEnd.setX(frameList.get(i).getZ());
                    pEnd.setZ(frameList.get(i).getX());
                    radius = frameList.get(i).getCr();
                    isRadius = true;
                } else {
                    pEnd.setX(frameList.get(i).getZ());
                    pEnd.setZ(frameList.get(i).getX());
                    isLine = true;
                }
                if (isRadius && frameList.get(i).isAxisContains()) {
                    drawArc(canvas, line, pointCoordinateZero, pStart, pEnd, radius, zoom, !clockwise);
                    pStart.setX(pEnd.getX());
                    pStart.setZ(pEnd.getZ());
                    isLine = false;
                    isRadius = false;
                }
                if (isLine && frameList.get(i).isAxisContains()) {
                    drawLine(canvas, line, pointCoordinateZero, pStart, pEnd, zoom);
                    pStart.setX(pEnd.getX());
                    pStart.setZ(pEnd.getZ());
                }
            }
        }
        drawPoint(canvas, pointCoordinateZero, pEnd, zoom);
    }
}
