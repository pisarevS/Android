package pisarev.com.modeling.mvp.model;

import android.graphics.Canvas;

import android.graphics.Color;
import pisarev.com.modeling.interfaces.Drawing;
import pisarev.com.modeling.interfaces.IDraw;
import pisarev.com.modeling.mvp.model.base.BaseDraw;

import java.util.List;

public class DrawVerticalTurning extends BaseDraw implements Drawing {

    public DrawVerticalTurning(IDraw draw) {
        super(draw);
    }

    @Override
    public void drawContour(Canvas canvas,MyData data, Point pointCoordinateZero, float zoom, int index) {
        List<Frame> frameList=data.getFrameList();
        boolean isDrawPoint=false;
        Point pStart = new Point();
        Point pEnd = new Point();
        Point point =new Point();
        pStart.setX(650f);
        pStart.setZ(250f);
        pEnd.setX(650f);
        pEnd.setZ(250f);
        float radius;
        float radiusRND;

        for (int i = 0; i < index; i++) {
            if (frameList.get(i).getGCode().contains("G17") || frameList.get(i).getGCode().contains("G18"))
                isG17 = isG17(frameList.get(i).getGCode());
            checkGCode(frameList.get(i).getGCode());
            if (data.getErrorListMap().containsKey(frameList.get(i).getId())) {
                draw.showError(data.getErrorListMap().get(frameList.get(i).getId()));
                break;
            } else {
                if (frameList.get(i).getIsCR() && frameList.get(i).isAxisContains()) {                                  //draw Arc
                    pEnd.setX(frameList.get(i).getX());
                    pEnd.setZ(frameList.get(i).getZ());
                    radius = frameList.get(i).getCr();
                    drawArc(canvas, line, pointCoordinateZero, pStart, pEnd, radius, zoom, clockwise);
                    pStart.setX(pEnd.getX());
                    pStart.setZ(pEnd.getZ());
                }
                if (!frameList.get(i).getIsCR() && !frameList.get(i).isRND() && frameList.get(i).isAxisContains()) {    //draw line
                    pEnd.setX(frameList.get(i).getX());
                    pEnd.setZ(frameList.get(i).getZ());
                    drawLine(canvas, line, pointCoordinateZero, pStart, pEnd, zoom);
                    pStart.setX(pEnd.getX());
                    pStart.setZ(pEnd.getZ());
                }
                if (frameList.get(i).isRND() && frameList.get(i).isAxisContains()) {                                    //draw RND
                    pEnd.setX(frameList.get(i).getX());
                    pEnd.setZ(frameList.get(i).getZ());
                    radiusRND = frameList.get(i).getRnd();
                    Point pointF = new Point();
                    pointF.setX(frameList.get(i + 1).getX());
                    pointF.setZ(frameList.get(i + 1).getZ());
                    drawRND(canvas,line, pointCoordinateZero, pStart, pEnd, pointF, radiusRND, zoom);
                    pStart.setX(pEnd.getX());
                    pStart.setZ(pEnd.getZ());
                    pEnd.setX(frameList.get(i).getX());
                    pEnd.setZ(frameList.get(i).getZ());
                }
                if (isNumberLine && frameList.get(i).getId() == numberLine) {
                    point.setX(frameList.get(i).getX());
                    point.setZ(frameList.get(i).getZ());
                    isDrawPoint=true;
                }
            }
        }
        drawPoint(canvas, pointCoordinateZero, pEnd, colorPoint, zoom);
        if(isDrawPoint) {
            drawPoint(canvas, pointCoordinateZero, point, colorTouchPoint, zoom);
        }
    }

    @Override
    public void setNumberLine(int numberLine) {
        isNumberLine = true;
        this.numberLine=numberLine;
    }
}

