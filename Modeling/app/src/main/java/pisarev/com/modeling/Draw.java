package pisarev.com.modeling;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

public class Draw {
    private Path path;
    private Point pStart;
    private Point pEnd;
    private RectF rectF;
    private Paint paintFullLine;
    private Paint paintDottedLine;

    public Draw() {
        init();
    }

    private void init() {
        paintFullLine = new Paint();
        paintFullLine.setColor( Color.GREEN );
        paintFullLine.setStyle( Paint.Style.STROKE );
        paintFullLine.setAntiAlias( true );
        paintDottedLine = new Paint();
        paintDottedLine.setColor( Color.GRAY );
        paintDottedLine.setStyle( Paint.Style.STROKE );
        paintDottedLine.setAntiAlias( true );
        paintDottedLine.setPathEffect( new DashPathEffect( new float[]{20f, 10f}, 0f ) );
    }

    private void drawLine(Canvas canvas, Paint paint, Point pointCoordinateZero, Point pointStart, Point pointEnd, float zoom) {
        path = new Path();
        pStart = new Point( pointStart.x, pointStart.z );
        pEnd = new Point( pointEnd.x, pointEnd.z );
        pStart.x *= zoom;
        pStart.z *= zoom;
        pEnd.x *= zoom;
        pEnd.z *= zoom;
        if (pStart.z > 0) pStart.z = pointCoordinateZero.z - pStart.z;
        else pStart.z = pointCoordinateZero.z + Math.abs( pStart.z );
        if (pEnd.z > 0) pEnd.z = pointCoordinateZero.z - pEnd.z;
        else pEnd.z = pointCoordinateZero.z + Math.abs( pEnd.z );
        path.moveTo( pointCoordinateZero.x + pStart.x, pStart.z );
        path.lineTo( pointCoordinateZero.x + pEnd.x, pEnd.z );
        canvas.drawPath( path, paint );
    }

    private void drawArc(Canvas canvas, Paint paint, Point pointCoordinateZero, Point pointStart, Point pointEnd, float radius, float zoom, boolean clockwise) {
        path = new Path();
        pStart = new Point( pointStart.x, pointStart.z );
        pEnd = new Point( pointEnd.x, pointEnd.z );
        rectF = new RectF();
        pStart.x *= zoom;
        pStart.z *= zoom;
        pEnd.x *= zoom;
        pEnd.z *= zoom;
        radius *= zoom;
        float startAngle = 0, sweetAngle, catet;
        float hord = (float) Math.sqrt( Math.pow( pStart.x - pEnd.x, 2 ) + Math.pow( pStart.z - pEnd.z, 2 ) );
        float h = (float) Math.sqrt( radius * radius - (hord / 2) * (hord / 2) );
        if (clockwise) {
            float x01 = (pStart.x + (pEnd.x - pStart.x) / 2 + h * (pEnd.z - pStart.z) / hord);
            float z01 = (pStart.z + (pEnd.z - pStart.z) / 2 - h * (pEnd.x - pStart.x) / hord);
            if (pStart.x > x01 && pStart.z >= z01) {
                catet = pStart.x - x01;
                if (pStart.z == z01)
                    startAngle = 0;
                else startAngle = (float) (360 - Math.acos( catet / radius ) * (180 / Math.PI));
            }
            if (pStart.x >= x01 && pStart.z < z01) {
                catet = pStart.x - x01;
                if (pStart.x == x01)
                    startAngle = 90;
                else startAngle = (float) (Math.acos( catet / radius ) * (180 / Math.PI));
            }
            if (pStart.x < x01 && pStart.z <= z01) {
                catet = x01 - pStart.x;
                if (pStart.z == z01)
                    startAngle = 180;
                else startAngle = (float) (180 - Math.acos( catet / radius ) * (180 / Math.PI));
            }
            if (pStart.x <= x01 && pStart.z > z01) {
                catet = x01 - pStart.x;
                if (pStart.x == x01)
                    startAngle = 270;
                else startAngle = (float) (180 + Math.acos( catet / radius ) * (180 / Math.PI));
            }
            rectF.set( pointCoordinateZero.x + x01 - radius, pointCoordinateZero.z - z01 - radius, pointCoordinateZero.x + x01 + radius, pointCoordinateZero.z - z01 + radius );
        } else {
            float x02 = pStart.x + (pEnd.x - pStart.x) / 2 - h * (pEnd.z - pStart.z) / hord;
            float z02 = pStart.z + (pEnd.z - pStart.z) / 2 + h * (pEnd.x - pStart.x) / hord;
            if (pEnd.x > x02 && pEnd.z >= z02) {
                catet = pEnd.x - x02;
                if (pEnd.z == z02)
                    startAngle = 0;
                else startAngle = (float) (360 - Math.acos( (catet / radius) * (180 / Math.PI) ));
            }
            if (pEnd.x >= x02 && pEnd.z < z02) {
                catet = pEnd.x - x02;
                if (pEnd.x == x02)
                    startAngle = 90;
                else startAngle = (float) (Math.acos( catet / radius ) * (180 * Math.PI));
            }
            if (pEnd.x < x02 && pEnd.z <= z02) {
                catet = x02 - pEnd.z;
                if (pEnd.z == z02)
                    startAngle = 180;
                else startAngle = (float) (180 - Math.acos( catet / radius ) * (180 / Math.PI));
            }
            if (pEnd.x <= x02 && pEnd.z > z02) {
                catet = x02 - pEnd.x;
                if (pEnd.x == x02)
                    startAngle = 270;
                else startAngle = (float) (180 + Math.acos( catet / radius ) * (180 / Math.PI));
            }
            rectF.set( pointCoordinateZero.x + x02 - radius, pointCoordinateZero.z - z02 - radius, pointCoordinateZero.x + x02 + radius, pointCoordinateZero.z - z02 + radius );
        }
        sweetAngle = (float) (2 * Math.asin( hord / (2 * radius) ) * (180 / Math.PI));
        path.addArc( rectF, startAngle, sweetAngle );
        canvas.drawPath( path, paint );
    }

    public void drawContour(Canvas canvas, Point pointCoordinateZero, float zoom) {
        drawLine( canvas, paintFullLine, pointCoordinateZero, new Point( 650, 250 ), new Point( 100, 100 ), 1 );
        drawArc( canvas, paintFullLine, pointCoordinateZero, new Point( 500, 500 ), new Point( 700, 300 ), 200, 1, true );
        drawArc( canvas, paintFullLine, pointCoordinateZero, new Point( 500, 500 ), new Point( 700, 300 ), 200, 1, false );
    }
}

