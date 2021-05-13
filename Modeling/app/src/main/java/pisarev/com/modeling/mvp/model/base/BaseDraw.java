package pisarev.com.modeling.mvp.model.base;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pisarev.com.modeling.interfaces.IDraw;
import pisarev.com.modeling.mvp.model.Frame;
import pisarev.com.modeling.mvp.model.Point;
import pisarev.com.modeling.mvp.model.Point2D;

public abstract class BaseDraw {

    private Paint paintFullLine;
    private Paint paintDottedLine;
    protected int clockwise=0;
    protected int isToolRadiusCompensation = 0;
    protected Paint line;
    protected IDraw draw;
    protected boolean isG17 = false;
    protected boolean isNumberLine;
    protected int numberLine;
    protected int colorPoint=Color.RED;
    protected int colorTouchPoint=Color.rgb(0,128,255);
    protected Point pStart = new Point();
    protected Point pEnd = new Point();
    protected Map<String, Double> toolsMap=new HashMap<>();

    protected BaseDraw(IDraw draw) {
        this.draw = draw;
        init();
    }

    public BaseDraw() {

    }

    private void init() {
        line = new Paint();
        paintFullLine = new Paint();
        paintFullLine.setColor(Color.GREEN);
        paintFullLine.setStyle(Paint.Style.STROKE);
        paintFullLine.setAntiAlias(true);
        paintDottedLine = new Paint();
        paintDottedLine.setColor(Color.GRAY);
        paintDottedLine.setStyle(Paint.Style.STROKE);
        paintDottedLine.setAntiAlias(true);
        paintDottedLine.setPathEffect(new DashPathEffect(new float[]{12f, 7f}, 0f));
    }

    protected void drawLine(Canvas canvas, Paint paint, Point pointCoordinateZero, Point pointStart, Point pointEnd, float zoom) {
        Path path = new Path();
        Point pStart = new Point(pointStart.getX(), pointStart.getZ());
        Point pEnd = new Point(pointEnd.getX(), pointEnd.getZ());
        pStart.setX(pStart.getX() * zoom);
        pStart.setZ(pStart.getZ() * zoom);
        pEnd.setX(pEnd.getX() * zoom);
        pEnd.setZ(pEnd.getZ() * zoom);
        if (pStart.getZ() > 0) pStart.setZ(pointCoordinateZero.getZ() - pStart.getZ());
        else pStart.setZ(pointCoordinateZero.getZ() + Math.abs(pStart.getZ()));
        if (pEnd.getZ() > 0) pEnd.setZ(pointCoordinateZero.getZ() - pEnd.getZ());
        else pEnd.setZ(pointCoordinateZero.getZ() + Math.abs(pEnd.getZ()));
        path.moveTo(pointCoordinateZero.getX() + pStart.getX(), pStart.getZ());
        path.lineTo(pointCoordinateZero.getX() + pEnd.getX(), pEnd.getZ());
        canvas.drawPath(path, paint);
    }

    protected void drawArc(Canvas canvas, Paint paint, Point pointCoordinateZero, Point pointStart, Point pointEnd, float radius, float zoom, int clockwise) {
        Path path = new Path();
        Point pStart = new Point(pointStart.getX(), pointStart.getZ());
        Point pEnd = new Point(pointEnd.getX(), pointEnd.getZ());
        RectF rectF = new RectF();
        pStart.setX(pStart.getX() * zoom);
        pStart.setZ(pStart.getZ() * zoom);
        pEnd.setX(pEnd.getX() * zoom);
        pEnd.setZ(pEnd.getZ() * zoom);
        radius *= zoom;
        float startAngle = 0, sweetAngle, cathetus;
        float chord = (float) Math.sqrt(Math.pow(pStart.getX() - pEnd.getX(), 2) + Math.pow(pStart.getZ() - pEnd.getZ(), 2));
        float h = (float) Math.sqrt(radius * radius - (chord / 2) * (chord / 2));
        if (clockwise==2) {
            float x01 = (pStart.getX() + (pEnd.getX() - pStart.getX()) / 2 + h * (pEnd.getZ() - pStart.getZ()) / chord);
            float z01 = (pStart.getZ() + (pEnd.getZ() - pStart.getZ()) / 2 - h * (pEnd.getX() - pStart.getX()) / chord);
            if (pStart.getX() > x01 && pStart.getZ() >= z01) {
                cathetus = pStart.getX() - x01;
                if (pStart.getZ() == z01)
                    startAngle = 0;
                else startAngle = (float) (360 - Math.acos(cathetus / radius) * (180 / Math.PI));
            }
            if (pStart.getX() >= x01 && pStart.getZ() < z01) {
                cathetus = pStart.getX() - x01;
                if (pStart.getX() == x01)
                    startAngle = 90;
                else startAngle = (float) (Math.acos(cathetus / radius) * (180 / Math.PI));
            }
            if (pStart.getX() < x01 && pStart.getZ() <= z01) {
                cathetus = x01 - pStart.getX();
                if (pStart.getZ() == z01)
                    startAngle = 180;
                else startAngle = (float) (180 - Math.acos(cathetus / radius) * (180 / Math.PI));
            }
            if (pStart.getX() <= x01 && pStart.getZ() > z01) {
                cathetus = x01 - pStart.getX();
                if (pStart.getX() == x01)
                    startAngle = 270;
                else startAngle = (float) (180 + Math.acos(cathetus / radius) * (180 / Math.PI));
            }
            rectF.set(pointCoordinateZero.getX() + x01 - radius, pointCoordinateZero.getZ() - z01 - radius, pointCoordinateZero.getX() + x01 + radius, pointCoordinateZero.getZ() - z01 + radius);
        } if(clockwise==3) {
            float x02 = pStart.getX() + (pEnd.getX() - pStart.getX()) / 2 - h * (pEnd.getZ() - pStart.getZ()) / chord;
            float z02 = pStart.getZ() + (pEnd.getZ() - pStart.getZ()) / 2 + h * (pEnd.getX() - pStart.getX()) / chord;
            if (pEnd.getX() > x02 && pEnd.getZ() >= z02) {
                cathetus = pEnd.getX() - x02;
                if (pEnd.getZ() == z02)
                    startAngle = 0;
                else startAngle = (float) (360 - Math.acos(cathetus / radius) * (180 / Math.PI));
            }
            if (pEnd.getX() >= x02 && pEnd.getZ() < z02) {
                cathetus = pEnd.getX() - x02;
                if (pEnd.getX() == x02)
                    startAngle = 90;
                else startAngle = (float) (Math.acos(cathetus / radius) * (180 / Math.PI));
            }
            if (pEnd.getX() < x02 && pEnd.getZ() <= z02) {
                cathetus = x02 - pEnd.getX();
                if (pEnd.getZ() == z02)
                    startAngle = 180;
                else startAngle = (float) (180 - Math.acos(cathetus / radius) * (180 / Math.PI));
            }

            if (pEnd.getX() <= x02 && pEnd.getZ() > z02) {
                cathetus = x02 - pEnd.getX();
                if (pEnd.getX() == x02)
                    startAngle = 270;
                else startAngle = (float) (180 + Math.acos(cathetus / radius) * (180 / Math.PI));
            }
            rectF.set(pointCoordinateZero.getX() + x02 - radius, pointCoordinateZero.getZ() - z02 - radius, pointCoordinateZero.getX() + x02 + radius, pointCoordinateZero.getZ() - z02 + radius);
        }
        sweetAngle = (float) (2 * Math.asin(chord / (2 * radius)) * (180 / Math.PI));
        path.addArc(rectF, startAngle, sweetAngle);
        canvas.drawPath(path, paint);
    }

    protected void drawRND(Canvas canvas,Paint paint, Point pointSystemCoordinate, Point pointStart, Point pointEnd, Point pointF, float radiusRND, float zoom) {
        Point pointStartCR = new Point();
        Point pointEndCR = new Point();
        float differenceX;
        float differenceZ;
        float cathet;
        int clockwiseRND = 3;
        float angle = new Point2D(pointEnd.getX() - pointStart.getX(), pointEnd.getZ() - pointStart.getZ()).angle(pointEnd.getX() - pointF.getX(), pointEnd.getZ() - pointF.getZ());
        float firstDistance = new Point2D(pointStart.getX(), pointStart.getZ()).distance(pointEnd.getX(), pointEnd.getZ());
        float secondDistance = new Point2D(pointEnd.getX(), pointEnd.getZ()).distance(pointF.getX(), pointF.getZ());
        if (angle == 90) {
            cathet = radiusRND;
        } else {
            cathet = (float) ((180 - angle) / 2 * (Math.PI / 180) * radiusRND);
        }
        differenceX = pointStart.getX() - pointEnd.getX();
        differenceZ = pointStart.getZ() - pointEnd.getZ();

        pointStartCR.setX(differenceX * cathet / firstDistance);
        pointStartCR.setZ(differenceZ * cathet / firstDistance);
        pointStartCR.setX(pointEnd.getX() + pointStartCR.getX());
        pointStartCR.setZ(pointEnd.getZ() + pointStartCR.getZ());

        differenceX = pointF.getX() - pointEnd.getX();
        differenceZ = pointF.getZ() - pointEnd.getZ();

        pointEndCR.setX(differenceX * cathet / secondDistance);
        pointEndCR.setZ(differenceZ * cathet / secondDistance);
        pointEndCR.setX(pointEnd.getX() + pointEndCR.getX());
        pointEndCR.setZ(pointEnd.getZ() + pointEndCR.getZ());

        if (pointStart.getX()>pointF.getX()&&(pointStart.getZ()+pointF.getZ())/2>pointEnd.getZ()) {
            clockwiseRND = 2;
        }
        if(pointStart.getX()>pointF.getX()&&(pointStart.getZ()+pointF.getZ())/2<pointEnd.getZ()){
            clockwiseRND=3;
        }
        if(pointStart.getX()<pointF.getX()&&(pointStart.getZ()+pointF.getZ())/2<pointEnd.getZ()){
            clockwiseRND=2;
        }
        drawLine(canvas,paint, pointSystemCoordinate, pointStart, pointStartCR, zoom);
        drawArc(canvas, paint, pointSystemCoordinate, pointStartCR, pointEndCR, radiusRND, zoom, clockwiseRND);
        pointEnd.setX(pointEndCR.getX());
        pointEnd.setZ(pointEndCR.getZ());
    }

    protected void drawPoint(Canvas canvas, Point pointCoordinateZero, Point pointEnd,int color, float zoom) {
        float radiusPoint = 9F;
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        Path path = new Path();
        Point pEnd = new Point(pointEnd.getX(), pointEnd.getZ());
        pEnd.setX(pEnd.getX() * zoom);
        pEnd.setZ(pEnd.getZ() * zoom);
        if (pEnd.getZ() > 0) pEnd.setZ(pointCoordinateZero.getZ() - pEnd.getZ());
        else pEnd.setZ(pointCoordinateZero.getZ() + Math.abs(pEnd.getZ()));
        path.addCircle(pointCoordinateZero.getX() + pEnd.getX(), pEnd.getZ(), radiusPoint, Path.Direction.CW);
        canvas.drawPath(path, paint);
    }

    protected boolean isG17(List<String> gCodes) {
        for (String gCode:gCodes) {
            return gCode.contains("G17");
        }
        return false;
    }

    protected void correctionForOffn(List<Frame> frameList) {
        for (int i = 0; i < frameList.size(); i++) {
            if (frameList.get(i).getGCode().contains("G17") || frameList.get(i).getGCode().contains("G18"))
                isG17 = isG17(frameList.get(i).getGCode());
            checkGCode(frameList.get(i).getGCode());
            if (frameList.get(i).getIsCR() && frameList.get(i).isAxisContains() && frameList.get(i).getOffn() > 0) {   //drawArc                            //draw Arc
                toolRadiusCompensationArcOffn(frameList, i, isToolRadiusCompensation, clockwise);
            }
            if (!frameList.get(i).getIsCR() && !frameList.get(i).isRND() && frameList.get(i).isAxisContains() && frameList.get(i).getOffn() > 0) {    //draw line
                toolRadiusCompensationLineOffn(frameList, i, isToolRadiusCompensation);
            }
            if (frameList.get(i).isRND() && frameList.get(i).isAxisContains()) {                                    //draw RND
                pEnd.setX(frameList.get(i).getX());
                pEnd.setZ(frameList.get(i).getZ());

                pStart.setX(pEnd.getX());
                pStart.setZ(pEnd.getZ());
            }
        }
    }

    protected void toolRadiusCompensationArcOffn(List<Frame> frameList, int numberLIne, int isToolRadiusCompensation, int clockwise) {
        pEnd.setX(frameList.get(numberLIne).getX());
        pEnd.setZ(frameList.get(numberLIne).getZ());
        float radius = frameList.get(numberLIne).getCr();
        float offn = frameList.get(numberLIne).getOffn();
        float chord = (float) Math.sqrt(Math.pow(pStart.getX() - pEnd.getX(), 2) + Math.pow(pStart.getZ() - pEnd.getZ(), 2));
        float h = (float) Math.sqrt(radius * radius - (chord / 2) * (chord / 2));
        if (clockwise==2 && frameList.get(numberLIne).getOffn() > 0) {
            float x01 = (pStart.getX() + (pEnd.getX() - pStart.getX()) / 2 + h * (pEnd.getZ() - pStart.getZ()) / chord);
            float z01 = (pStart.getZ() + (pEnd.getZ() - pStart.getZ()) / 2 - h * (pEnd.getX() - pStart.getX()) / chord);
            if (isToolRadiusCompensation == 1) {
                float tempStartX = (x01 - pStart.getX()) * ((radius - offn) / radius);
                float tempStartZ = (z01 - pStart.getZ()) * ((radius - offn) / radius);
                float tempEndX = (x01 - pEnd.getX()) * ((radius - offn) / radius);
                float tempEndZ = (z01 - pEnd.getZ()) * ((radius - offn) / radius);
                frameList.get(numberLIne - 1).setX(pStart.getX() + (x01 - pStart.getX() - tempStartX));
                frameList.get(numberLIne - 1).setZ(pStart.getZ() + (z01 - pStart.getZ() - tempStartZ));
                frameList.get(numberLIne).setX(pEnd.getX() + (x01 - pEnd.getX() - tempEndX));
                frameList.get(numberLIne).setZ(pEnd.getZ() + (z01 - pEnd.getZ() - tempEndZ));
                frameList.get(numberLIne).setCr(radius - offn);
            }  //G41
            if (isToolRadiusCompensation == 2) {
                float tempStartX = (x01 - pStart.getX()) * ((radius + offn) / radius);
                float tempStartZ = (z01 - pStart.getZ()) * ((radius + offn) / radius);
                float tempEndX = (x01 - pEnd.getX()) * ((radius + offn) / radius);
                float tempEndZ = (z01 - pEnd.getZ()) * ((radius + offn) / radius);
                frameList.get(numberLIne - 1).setX(pStart.getX() + (x01 - pStart.getX() - tempStartX));
                frameList.get(numberLIne - 1).setZ(pStart.getZ() + (z01 - pStart.getZ() - tempStartZ));
                frameList.get(numberLIne).setX(pEnd.getX() + (x01 - pEnd.getX() - tempEndX));
                frameList.get(numberLIne).setZ(pEnd.getZ() + (z01 - pEnd.getZ() - tempEndZ));
                frameList.get(numberLIne).setCr(radius + offn);
            }  //G42
        }
        if (clockwise==3 && frameList.get(numberLIne).getOffn() > 0) {
            float x02 = pStart.getX() + (pEnd.getX() - pStart.getX()) / 2 - h * (pEnd.getZ() - pStart.getZ()) / chord;
            float z02 = pStart.getZ() + (pEnd.getZ() - pStart.getZ()) / 2 + h * (pEnd.getX() - pStart.getX()) / chord;
            if (isToolRadiusCompensation == 1) {
                float tempStartX = (x02 - pStart.getX()) * ((radius + offn) / radius);
                float tempStartZ = (z02 - pStart.getZ()) * ((radius + offn) / radius);
                float tempEndX = (x02 - pEnd.getX()) * ((radius + offn) / radius);
                float tempEndZ = (z02 - pEnd.getZ()) * ((radius + offn) / radius);
                frameList.get(numberLIne - 1).setX(pStart.getX() + (x02 - pStart.getX() - tempStartX));
                frameList.get(numberLIne - 1).setZ(pStart.getZ() + (z02 - pStart.getZ() - tempStartZ));
                frameList.get(numberLIne).setX(pEnd.getX() + (x02 - pEnd.getX() - tempEndX));
                frameList.get(numberLIne).setZ(pEnd.getZ() + (z02 - pEnd.getZ() - tempEndZ));
                frameList.get(numberLIne).setCr(radius + offn);
            }  //G41
            if (isToolRadiusCompensation == 2) {
                float tempStartX = (x02 - pStart.getX()) * ((radius - offn) / radius);
                float tempStartZ = (z02 - pStart.getZ()) * ((radius - offn) / radius);
                float tempEndX = (x02 - pEnd.getX()) * ((radius - offn) / radius);
                float tempEndZ = (z02 - pEnd.getZ()) * ((radius - offn) / radius);
                frameList.get(numberLIne - 1).setX(pStart.getX() + (x02 - pStart.getX() - tempStartX));
                frameList.get(numberLIne - 1).setZ(pStart.getZ() + (z02 - pStart.getZ() - tempStartZ));
                frameList.get(numberLIne).setX(pEnd.getX() + (x02 - pEnd.getX() - tempEndX));
                frameList.get(numberLIne).setZ(pEnd.getZ() + (z02 - pEnd.getZ() - tempEndZ));
                frameList.get(numberLIne).setCr(radius - offn);
            }  //G42
        }
        pStart.setX(pEnd.getX());
        pStart.setZ(pEnd.getZ());
    }

    protected void toolRadiusCompensationLineOffn(List<Frame> frameList, int numberLIne, int isToolRadiusCompensation) {
        float offn = frameList.get(numberLIne).getOffn();
        pEnd.setX(frameList.get(numberLIne).getX());
        pEnd.setZ(frameList.get(numberLIne).getZ());
        if (isToolRadiusCompensation == 1) {
            if (pStart.getZ() == pEnd.getZ() && pStart.getX() > pEnd.getX()) {
                if (!containsG41G42(frameList.get(numberLIne).getGCode())) {
                    if (frameList.get(numberLIne - 1).getZ() == frameList.get(numberLIne).getZ() && frameList.get(numberLIne - 1).getX() > frameList.get(numberLIne).getX()) {
                        frameList.get(numberLIne - 1).setX(pStart.getX() - offn);
                    } else frameList.get(numberLIne - 1).setX(pStart.getX());
                    frameList.get(numberLIne - 1).setZ(pStart.getZ() + offn);
                }
                frameList.get(numberLIne).setX(pEnd.getX());
                frameList.get(numberLIne).setZ(pEnd.getZ() + offn);
            }  //Z==Z -X
            if (pStart.getZ() == pEnd.getZ() && pStart.getX() < pEnd.getX()) {
                if (!containsG41G42(frameList.get(numberLIne).getGCode())) {
                    if (frameList.get(numberLIne - 1).getZ() == frameList.get(numberLIne).getZ() && frameList.get(numberLIne - 1).getX() < frameList.get(numberLIne).getX()) {
                        frameList.get(numberLIne - 1).setX(pStart.getX() + offn);
                    } else frameList.get(numberLIne - 1).setX(pStart.getX());
                    frameList.get(numberLIne - 1).setZ(pStart.getZ() - offn);
                }
                frameList.get(numberLIne).setX(pEnd.getX());
                frameList.get(numberLIne).setZ(pEnd.getZ() - offn);
            }  //Z==Z +X
            if (pStart.getX() == pEnd.getX() && pStart.getZ() > pEnd.getZ()) {
                if (!containsG41G42(frameList.get(numberLIne).getGCode())) {
                    frameList.get(numberLIne - 1).setX(pStart.getX() - offn);
                    frameList.get(numberLIne - 1).setZ(pStart.getZ());
                }
                frameList.get(numberLIne).setX(pEnd.getX() - offn);
                frameList.get(numberLIne).setZ(pEnd.getZ());
            }  //X==X -Z
            if (pStart.getX() == pEnd.getX() && pStart.getZ() < pEnd.getZ()) {
                if (!containsG41G42(frameList.get(numberLIne).getGCode())) {
                    frameList.get(numberLIne - 1).setX(pStart.getX() + offn);
                    frameList.get(numberLIne - 1).setZ(pStart.getZ());
                }
                frameList.get(numberLIne).setX(pEnd.getX() + offn);
                frameList.get(numberLIne).setZ(pEnd.getZ());
            }  //X==X +Z
            if (pStart.getX() < pEnd.getX() && pStart.getZ() > pEnd.getZ()) {
                float angle = new Point2D(pEnd.getX() - pStart.getX(), pStart.getZ() - pEnd.getZ()).angle(pStart.getX(), 0);
                angle = 180 - 90 - angle;
                float cathet1 = (float) (offn * Math.sin(Math.toRadians(angle)));
                float cathet2 = (float) Math.sqrt(offn * offn - cathet1 * cathet1);
                if (!containsG41G42(frameList.get(numberLIne).getGCode())) {
                    frameList.get(numberLIne - 1).setX(pStart.getX() - cathet2);
                    frameList.get(numberLIne - 1).setZ(pStart.getZ() - cathet1);
                }
                frameList.get(numberLIne).setX(pEnd.getX() - cathet2);
                frameList.get(numberLIne).setZ(pEnd.getZ() - cathet1);
            }
            if (pStart.getX() > pEnd.getX() && pStart.getZ() > pEnd.getZ()) {
                float angle = new Point2D(pStart.getX() - pEnd.getX(), pStart.getZ() - pEnd.getZ()).angle(pEnd.getX(), 0);
                angle = 180 - 90 - angle;
                float cathet1 = (float) (offn * Math.sin(Math.toRadians(angle)));
                float cathet2 = (float) Math.sqrt(offn * offn - cathet1 * cathet1);
                if (!containsG41G42(frameList.get(numberLIne).getGCode())) {
                    frameList.get(numberLIne - 1).setX(pStart.getX() - cathet2);
                    frameList.get(numberLIne - 1).setZ(pStart.getZ() + cathet1);
                }
                frameList.get(numberLIne).setX(pEnd.getX() - cathet2);
                frameList.get(numberLIne).setZ(pEnd.getZ() + cathet1);
            }
            if (pStart.getX() > pEnd.getX() && pStart.getZ() < pEnd.getZ()) {
                float angle = new Point2D(pStart.getX() - pEnd.getX(), pStart.getZ() - pEnd.getZ()).angle(pEnd.getX(), 0);
                angle = 180 - 90 - angle;
                System.out.println(angle);
                float cathet1 = (float) (offn * Math.sin(Math.toRadians(angle)));
                float cathet2 = (float) Math.sqrt(offn * offn - cathet1 * cathet1);
                if (!containsG41G42(frameList.get(numberLIne).getGCode())) {
                    frameList.get(numberLIne - 1).setX(pStart.getX() + cathet2);
                    frameList.get(numberLIne - 1).setZ(pStart.getZ() + cathet1);
                }
                frameList.get(numberLIne).setX(pEnd.getX() + cathet2);
                frameList.get(numberLIne).setZ(pEnd.getZ() + cathet1);
            }
            if (pStart.getX() < pEnd.getX() && pStart.getZ() < pEnd.getZ()) {
                float angle = new Point2D(pStart.getX() - pEnd.getX(), pStart.getZ() - pEnd.getZ()).angle(pEnd.getX(), 0);
                angle = 180 - angle;
                System.out.println(angle);
                float cathet1 = (float) (offn * Math.sin(Math.toRadians(angle)));
                float cathet2 = (float) Math.sqrt(offn * offn - cathet1 * cathet1);
                if (!containsG41G42(frameList.get(numberLIne).getGCode())) {
                    frameList.get(numberLIne - 1).setX(pStart.getX() + cathet2);
                    frameList.get(numberLIne - 1).setZ(pStart.getZ() - cathet1);
                }
                frameList.get(numberLIne).setX(pEnd.getX() + cathet2);
                frameList.get(numberLIne).setZ(pEnd.getZ() - cathet1);
            }
        }  //G41
        if (isToolRadiusCompensation == 2) {
            if (pStart.getZ() == pEnd.getZ() && pStart.getX() > pEnd.getX()) {
                if (!containsG41G42(frameList.get(numberLIne).getGCode())) {
                    frameList.get(numberLIne - 1).setX(pStart.getX());
                    frameList.get(numberLIne - 1).setZ(pStart.getZ() - offn);
                }
                frameList.get(numberLIne).setX(pEnd.getX());
                frameList.get(numberLIne).setZ(pEnd.getZ() - offn);
            }  //Z==Z -X
            if (pStart.getZ() == pEnd.getZ() && pStart.getX() < pEnd.getX()) {
                if (!containsG41G42(frameList.get(numberLIne).getGCode())) {
                    frameList.get(numberLIne - 1).setX(pStart.getX());
                    frameList.get(numberLIne - 1).setZ(pStart.getZ() + offn);
                }
                frameList.get(numberLIne).setX(pEnd.getX());
                frameList.get(numberLIne).setZ(pEnd.getZ() + offn);
            }  //Z==Z +X
            if (pStart.getX() == pEnd.getX() && pStart.getZ() > pEnd.getZ()) {
                if (!containsG41G42(frameList.get(numberLIne).getGCode())) {
                    frameList.get(numberLIne - 1).setX(pStart.getX() + offn);
                    frameList.get(numberLIne - 1).setZ(pStart.getZ());
                }
                frameList.get(numberLIne).setX(pEnd.getX() + offn);
                frameList.get(numberLIne).setZ(pEnd.getZ());
            }  //X==X -Z
            if (pStart.getX() == pEnd.getX() && pStart.getZ() < pEnd.getZ()) {
                if (!containsG41G42(frameList.get(numberLIne).getGCode())) {
                    frameList.get(numberLIne - 1).setX(pStart.getX() - offn);
                    frameList.get(numberLIne - 1).setZ(pStart.getZ());
                }
                frameList.get(numberLIne).setX(pEnd.getX() - offn);
                frameList.get(numberLIne).setZ(pEnd.getZ());
            }  //X==X +Z
            if (pStart.getX() < pEnd.getX() && pStart.getZ() > pEnd.getZ()) {
                float angle = new Point2D(pEnd.getX() - pStart.getX(), pStart.getZ() - pEnd.getZ()).angle(pStart.getX(), 0);
                angle = 180 - 90 - angle;
                float cathet1 = (float) (offn * Math.sin(Math.toRadians(angle)));
                float cathet2 = (float) Math.sqrt(offn * offn - cathet1 * cathet1);
                if (!containsG41G42(frameList.get(numberLIne).getGCode())) {
                    frameList.get(numberLIne - 1).setX(pStart.getX() + cathet2);
                    frameList.get(numberLIne - 1).setZ(pStart.getZ() + cathet1);
                }
                frameList.get(numberLIne).setX(pEnd.getX() + cathet2);
                frameList.get(numberLIne).setZ(pEnd.getZ() + cathet1);
            }
            if (pStart.getX() > pEnd.getX() && pStart.getZ() > pEnd.getZ()) {
                float angle = new Point2D(pStart.getX() - pEnd.getX(), pStart.getZ() - pEnd.getZ()).angle(pEnd.getX(), 0);
                angle = 180 - 90 - angle;
                float cathet1 = (float) (offn * Math.sin(Math.toRadians(angle)));
                float cathet2 = (float) Math.sqrt(offn * offn - cathet1 * cathet1);
                if (!containsG41G42(frameList.get(numberLIne).getGCode())) {
                    frameList.get(numberLIne - 1).setX(pStart.getX() + cathet2);
                    frameList.get(numberLIne - 1).setZ(pStart.getZ() - cathet1);
                }
                frameList.get(numberLIne).setX(pEnd.getX() + cathet2);
                frameList.get(numberLIne).setZ(pEnd.getZ() - cathet1);
            }
            if (pStart.getX() > pEnd.getX() && pStart.getZ() < pEnd.getZ()) {
                float angle = new Point2D(pStart.getX() - pEnd.getX(), pStart.getZ() - pEnd.getZ()).angle(pEnd.getX(), 0);
                angle = 180 - 90 - angle;
                System.out.println(angle);
                float cathet1 = (float) (offn * Math.sin(Math.toRadians(angle)));
                float cathet2 = (float) Math.sqrt(offn * offn - cathet1 * cathet1);
                if (!containsG41G42(frameList.get(numberLIne).getGCode())) {
                    frameList.get(numberLIne - 1).setX(pStart.getX() - cathet2);
                    frameList.get(numberLIne - 1).setZ(pStart.getZ() - cathet1);
                }
                frameList.get(numberLIne).setX(pEnd.getX() - cathet2);
                frameList.get(numberLIne).setZ(pEnd.getZ() - cathet1);
            }
            if (pStart.getX() < pEnd.getX() && pStart.getZ() < pEnd.getZ()) {
                float angle = new Point2D(pStart.getX() - pEnd.getX(), pStart.getZ() - pEnd.getZ()).angle(pEnd.getX(), 0);
                angle = 180 - angle;
                System.out.println(angle);
                float cathet1 = (float) (offn * Math.sin(Math.toRadians(angle)));
                float cathet2 = (float) Math.sqrt(offn * offn - cathet1 * cathet1);
                if (!containsG41G42(frameList.get(numberLIne).getGCode())) {
                    frameList.get(numberLIne - 1).setX(pStart.getX() - cathet2);
                    frameList.get(numberLIne - 1).setZ(pStart.getZ() + cathet1);
                }
                frameList.get(numberLIne).setX(pEnd.getX() - cathet2);
                frameList.get(numberLIne).setZ(pEnd.getZ() + cathet1);
            }
        }  //G42
        pStart.setX(pEnd.getX());
        pStart.setZ(pEnd.getZ());
    }

    protected boolean containsG41G42(List<String> gCodes) {
        final boolean[] b = {false};
        gCodes.stream()
                .filter(g -> g.equals("G41") || g.equals("G42"))
                .findAny()
                .ifPresent(g -> b[0] = true);
        return b[0];
    }

    protected void checkGCode(List<String> gCodeList) {
        for (String gCode : gCodeList) {
            switch (gCode) {
                case "G0":
                case "G00":
                    line = paintDottedLine;
                    isToolRadiusCompensation = 0;
                    clockwise=0;
                    break;
                case "G1":
                case "G01":
                    line = paintFullLine;
                    clockwise=0;
                    break;
                case "G2":
                case "G02":
                    if(isG17) clockwise =2;
                    else clockwise=3;
                    break;
                case "G3":
                case "G03":
                    if(isG17) clockwise = 3;
                    else clockwise=2;
                    break;
                case "G41":
                    if (!isG17) isToolRadiusCompensation = 1;
                    else isToolRadiusCompensation = 2;
                    break;
                case "G42":
                    if (!isG17) isToolRadiusCompensation = 2;
                    else isToolRadiusCompensation = 1;
                    break;
            }
        }
    }

}
