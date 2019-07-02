package pisarev.com.modeling.mvp.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import java.util.ArrayList;

import javax.inject.Inject;

import pisarev.com.modeling.application.App;
import pisarev.com.modeling.interfaces.ViewMvp;


public abstract class BaseDraw {

    private Paint paintFullLine;
    private Paint paintDottedLine;
    private int x;
    private int u;
    boolean clockwise;
    final float FIBO=1123581220;
    Paint line;
    String horizontalAxis;
    String verticalAxis;
    ArrayList<StringBuffer> programList;
    ViewMvp.MyViewMvp myViewMvp;
    @Inject
    MyData data;

    BaseDraw(ViewMvp.MyViewMvp myViewMvp) {
        this.myViewMvp =myViewMvp;
        init();
    }

    private void init() {
        App.getComponent().inject( this );
        programList= data.getProgramList();
        line=new Paint();
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

    void drawLine(Canvas canvas, Paint paint, Point pointCoordinateZero, Point pointStart, Point pointEnd, float zoom) {
        Path path = new Path();
        Point pStart = new Point( pointStart.x, pointStart.z );
        Point pEnd = new Point( pointEnd.x, pointEnd.z );
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

    void drawArc(Canvas canvas, Paint paint, Point pointCoordinateZero, Point pointStart, Point pointEnd, float radius, float zoom, boolean clockwise) {
        Path path = new Path();
        Point pStart = new Point( pointStart.x, pointStart.z );
        Point pEnd = new Point( pointEnd.x, pointEnd.z );
        RectF rectF = new RectF();
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
                else startAngle = (float) (360- Math.acos( catet / radius ) * (180 / Math.PI));
            }
            if (pEnd.x >= x02 && pEnd.z < z02) {
                catet = pEnd.x - x02;
                if (pEnd.x == x02)
                    startAngle = 90;
                else startAngle = (float) (Math.acos( catet / radius ) * (180 / Math.PI));
            }
            if (pEnd.x < x02 && pEnd.z <= z02) {
                catet = x02 - pEnd.x;
                if (pEnd.z == z02)
                    startAngle = 180;
                else startAngle = (float) (180 - Math.acos(catet / radius) * (180 / Math.PI));
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
        path.addArc(rectF, startAngle, sweetAngle );
        canvas.drawPath( path, paint );
    }

    void drawPoint(Canvas canvas, Point pointCoordinateZero, Point pointEnd, float zoom){
        Paint paint=new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        Path path = new Path();
        Point pEnd = new Point( pointEnd.x, pointEnd.z );
        pEnd.x *= zoom;
        pEnd.z *= zoom;
        if (pEnd.z > 0) pEnd.z = pointCoordinateZero.z - pEnd.z;
        else pEnd.z = pointCoordinateZero.z + Math.abs( pEnd.z );
        path.addCircle(pointCoordinateZero.x + pEnd.x, pEnd.z,7, Path.Direction.CW);
        canvas.drawPath(path,paint);
    }

    float incrementSearch(StringBuffer frame, String axis){
        Expression expression=new Expression();
        StringBuilder temp=new StringBuilder(  );
        int n = frame.indexOf(axis);

        if(frame.charAt( n+axis.length()  )=='('){
            for (int i = n+axis.length(); i <frame.length() ; i++) {
                if (readUp( frame.charAt( i ) )){
                    temp.append( frame.charAt( i ) );
                }else {break;}
            }
            return expression.calculate(temp.toString()) ;
        }
        return Float.parseFloat(temp.toString());
    }

    private boolean isG17(ArrayList<StringBuffer> programList)
    {
        for (int i = 0; i < programList.size(); i++)
            if (programList.get( i ).toString().contains( "G17" )) {
                return true;
            }
        return false;
    }

    private static boolean readUp(char input)
    {
        switch (input)
        {
            case 'C':
            case 'X':
            case 'G':
            case 'M':
            case 'F':
            case 'W':
            case 'Z':
            case 'D':
            case 'S':
            case 'A':
            case 'U':
            case 'L':
            case 'O':
                return false;
        }
        return true;
    }

    private boolean isDigit(char input)
    {
        switch (input)
        {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return true;
        }
        return false;
    }

    float coordinateSearch(StringBuffer frame, String axis){
        Expression expression=new Expression();
        StringBuffer temp=new StringBuffer(  );
        int n = frame.indexOf(axis);

        if(isDigit(frame.charAt( n+axis.length()))||frame.charAt( n+axis.length())=='-'||frame.charAt( n+axis.length())=='+'){
            for (int i = n+axis.length(); i <frame.length() ; i++) {
                if (readUp( frame.charAt( i ) )){
                    temp.append( frame.charAt( i ) );
                }else {break;}
            }
            return Float.parseFloat(temp.toString());
        }else if(frame.charAt( n+axis.length()  )=='='){
            for (int i = n+axis.length()+1; i <frame.length() ; i++) {
                if (readUp( frame.charAt( i ) )){
                    temp.append( frame.charAt( i ) );
                }else {break;}
            }
            return expression.calculate(temp.toString()) ;
        }
        return FIBO;
    }

    void selectCoordinateSystem(ArrayList<StringBuffer> programList){
        for (int i = 0; i <programList.size() ; i++) {
            if (programList.get( i ).toString().contains( "X" ))
                x++;
            if (programList.get( i ).toString().contains( "U" ))
                u++;
            if(x>u){
                horizontalAxis ="X";
                verticalAxis ="Z";
            }else {
                horizontalAxis ="U";
                verticalAxis ="W";
            }
        }
    }

    void containsGCode(String frame)
    {
        boolean isG17=isG17(programList);
        if (frame.contains("G"))
        {
            StringBuilder G = new StringBuilder("G");
            for (int i = 0; i < frame.length(); i++)
            {
                char c=frame.charAt( i );
                if (c == 'G')
                {
                    for (int j = i + 1; j < frame.length(); j++)
                    {
                        char t=frame.charAt( j );
                        if (isDigit(t))
                        {
                            G.append(t);
                        }
                        else { break; }
                    }
                    switch (G.toString())
                    {
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
                    G = new StringBuilder("G");
                }
            }
        }
    }

    boolean contains(StringBuffer sb, String findString){
        return sb.indexOf(findString) > -1;
    }
}
