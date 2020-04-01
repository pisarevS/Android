package pisarev.com.modeling.mvp.model.base;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

import pisarev.com.modeling.interfaces.IDraw;
import pisarev.com.modeling.mvp.model.Point;
import pisarev.com.modeling.mvp.model.Point2D;

public abstract class BaseDraw {

    private Paint paintFullLine;
    private Paint paintDottedLine;
    protected boolean clockwise;
    protected Paint line;
    protected IDraw draw;
    protected boolean isG17;
    protected boolean isNumberLine;
    protected int numberLine;
    protected int colorPoint=Color.RED;
    protected int colorTouchPoint=Color.rgb(0,128,255);

    protected BaseDraw(IDraw draw) {
        this.draw = draw;
        init();
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

    protected void drawArc(Canvas canvas, Paint paint, Point pointCoordinateZero, Point pointStart, Point pointEnd, float radius, float zoom, boolean clockwise) {
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
        if (clockwise) {
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
        } else {
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
        boolean clockwiseRND = false;
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
            clockwiseRND = true;
        }
        if(pointStart.getX()>pointF.getX()&&(pointStart.getZ()+pointF.getZ())/2<pointEnd.getZ()){
            clockwiseRND=false;
        }
        if(pointStart.getX()<pointF.getX()&&(pointStart.getZ()+pointF.getZ())/2<pointEnd.getZ()){
            clockwiseRND=true;
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

    protected void checkGCode(List<String> gCodeList) {
        for (String gCode : gCodeList) {
            switch (gCode) {
                case "G0":
                case "G00":
                    line = paintDottedLine;
                    break;
                case "G1":
                case "G01":
                    line = paintFullLine;
                    break;
                case "G2":
                case "G02":
                    clockwise = isG17;
                    break;
                case "G3":
                case "G03":
                    clockwise = !isG17;
                    break;
            }
        }
    }
}
