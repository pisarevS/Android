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
import pisarev.com.modeling.interfaces.IDraw;
import pisarev.com.modeling.interfaces.ViewMvp;


public abstract class BaseDraw implements IDraw {

    private Paint paintFullLine;
    private Paint paintDottedLine;
    private int x;
    private int u;
    boolean clockwise;
    final float FIBO = 1123581220;
    Paint line;
    String horizontalAxis;
    String verticalAxis;
    ArrayList<StringBuffer> programList;
    ViewMvp.MyViewMvp myViewMvp;
    @Inject
    MyData data;

    BaseDraw(ViewMvp.MyViewMvp myViewMvp) {
        this.myViewMvp = myViewMvp;
        init();
    }

    private void init() {
        App.getComponent().inject( this );
        programList = data.getProgramList();
        line = new Paint();
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

    @Override
    public void drawLine(Canvas canvas, Paint paint, Point pointCoordinateZero, Point pointStart, Point pointEnd, float zoom) {
        Path path = new Path();
        Point pStart = new Point( pointStart.getX(), pointStart.getZ() );
        Point pEnd = new Point( pointEnd.getX(), pointEnd.getZ() );
        pStart.setX( pStart.getX() * zoom );
        pStart.setZ( pStart.getZ() * zoom );
        pEnd.setX( pEnd.getX() * zoom );
        pEnd.setZ( pEnd.getZ() * zoom );
        if (pStart.getZ() > 0) pStart.setZ( pointCoordinateZero.getZ() - pStart.getZ() );
        else pStart.setZ( pointCoordinateZero.getZ() + Math.abs( pStart.getZ() ) );
        if (pEnd.getZ() > 0) pEnd.setZ( pointCoordinateZero.getZ() - pEnd.getZ() );
        else pEnd.setZ( pointCoordinateZero.getZ() + Math.abs( pEnd.getZ() ) );
        path.moveTo( pointCoordinateZero.getX() + pStart.getX(), pStart.getZ() );
        path.lineTo( pointCoordinateZero.getX() + pEnd.getX(), pEnd.getZ() );
        canvas.drawPath( path, paint );
    }

    @Override
    public void drawArc(Canvas canvas, Paint paint, Point pointCoordinateZero, Point pointStart, Point pointEnd, float radius, float zoom, boolean clockwise) {
        Path path = new Path();
        Point pStart = new Point( pointStart.getX(), pointStart.getZ() );
        Point pEnd = new Point( pointEnd.getX(), pointEnd.getZ() );
        RectF rectF = new RectF();
        pStart.setX( pStart.getX() * zoom );
        pStart.setZ( pStart.getZ() * zoom );
        pEnd.setX( pEnd.getX() * zoom );
        pEnd.setZ( pEnd.getZ() * zoom );
        radius *= zoom;
        float startAngle = 0, sweetAngle, cathetus;
        float chord = (float) Math.sqrt( Math.pow( pStart.getX() - pEnd.getX(), 2 ) + Math.pow( pStart.getZ() - pEnd.getZ(), 2 ) );
        float h = (float) Math.sqrt( radius * radius - (chord / 2) * (chord / 2) );
        if (clockwise) {
            float x01 = (pStart.getX() + (pEnd.getX() - pStart.getX()) / 2 + h * (pEnd.getZ() - pStart.getZ()) / chord);
            float z01 = (pStart.getZ() + (pEnd.getZ() - pStart.getZ()) / 2 - h * (pEnd.getX() - pStart.getX()) / chord);
            if (pStart.getX() > x01 && pStart.getZ() >= z01) {
                cathetus = pStart.getX() - x01;
                if (pStart.getZ() == z01)
                    startAngle = 0;
                else startAngle = (float) (360 - Math.acos( cathetus / radius ) * (180 / Math.PI));
            }
            if (pStart.getX() >= x01 && pStart.getZ() < z01) {
                cathetus = pStart.getX() - x01;
                if (pStart.getX() == x01)
                    startAngle = 90;
                else startAngle = (float) (Math.acos( cathetus / radius ) * (180 / Math.PI));
            }
            if (pStart.getX() < x01 && pStart.getZ() <= z01) {
                cathetus = x01 - pStart.getX();
                if (pStart.getZ() == z01)
                    startAngle = 180;
                else startAngle = (float) (180 - Math.acos( cathetus / radius ) * (180 / Math.PI));
            }
            if (pStart.getX() <= x01 && pStart.getZ() > z01) {
                cathetus = x01 - pStart.getX();
                if (pStart.getX() == x01)
                    startAngle = 270;
                else startAngle = (float) (180 + Math.acos( cathetus / radius ) * (180 / Math.PI));
            }
            rectF.set( pointCoordinateZero.getX() + x01 - radius, pointCoordinateZero.getZ() - z01 - radius, pointCoordinateZero.getX() + x01 + radius, pointCoordinateZero.getZ() - z01 + radius );
        } else {
            float x02 = pStart.getX() + (pEnd.getX() - pStart.getX()) / 2 - h * (pEnd.getZ() - pStart.getZ()) / chord;
            float z02 = pStart.getZ() + (pEnd.getZ() - pStart.getZ()) / 2 + h * (pEnd.getX() - pStart.getX()) / chord;
            if (pEnd.getX() > x02 && pEnd.getZ() >= z02) {
                cathetus = pEnd.getX() - x02;
                if (pEnd.getZ() == z02)
                    startAngle = 0;
                else startAngle = (float) (360 - Math.acos( cathetus / radius ) * (180 / Math.PI));
            }
            if (pEnd.getX() >= x02 && pEnd.getZ() < z02) {
                cathetus = pEnd.getX() - x02;
                if (pEnd.getX() == x02)
                    startAngle = 90;
                else startAngle = (float) (Math.acos( cathetus / radius ) * (180 / Math.PI));
            }
            if (pEnd.getX() < x02 && pEnd.getZ() <= z02) {
                cathetus = x02 - pEnd.getX();
                if (pEnd.getZ() == z02)
                    startAngle = 180;
                else startAngle = (float) (180 - Math.acos( cathetus / radius ) * (180 / Math.PI));
            }

            if (pEnd.getX() <= x02 && pEnd.getZ() > z02) {
                cathetus = x02 - pEnd.getX();
                if (pEnd.getX() == x02)
                    startAngle = 270;
                else startAngle = (float) (180 + Math.acos( cathetus / radius ) * (180 / Math.PI));
            }
            rectF.set( pointCoordinateZero.getX() + x02 - radius, pointCoordinateZero.getZ() - z02 - radius, pointCoordinateZero.getX() + x02 + radius, pointCoordinateZero.getZ() - z02 + radius );
        }
        sweetAngle = (float) (2 * Math.asin( chord / (2 * radius) ) * (180 / Math.PI));
        path.addArc( rectF, startAngle, sweetAngle );
        canvas.drawPath( path, paint );
    }

    @Override
    public void drawPoint(Canvas canvas, Point pointCoordinateZero, Point pointEnd, float zoom) {
        float radiusPoint = 7F;
        Paint paint = new Paint();
        paint.setStyle( Paint.Style.FILL );
        paint.setColor( Color.RED );
        Path path = new Path();
        Point pEnd = new Point( pointEnd.getX(), pointEnd.getZ() );
        pEnd.setX( pEnd.getX() * zoom );
        pEnd.setZ( pEnd.getZ() * zoom );
        if (pEnd.getZ() > 0) pEnd.setZ( pointCoordinateZero.getZ() - pEnd.getZ() );
        else pEnd.setZ( pointCoordinateZero.getZ() + Math.abs( pEnd.getZ() ) );
        path.addCircle( pointCoordinateZero.getX() + pEnd.getX(), pEnd.getZ(), radiusPoint, Path.Direction.CW );
        canvas.drawPath( path, paint );
    }

    float incrementSearch(StringBuffer frame, String axis) {
        Expression expression = new Expression();
        StringBuilder temp = new StringBuilder();
        int n = frame.indexOf( axis );

        if (frame.charAt( n + axis.length() ) == '(') {
            for (int i = n + axis.length(); i < frame.length(); i++) {
                if (readUp( frame.charAt( i ) )) {
                    temp.append( frame.charAt( i ) );
                } else {
                    break;
                }
            }
            return expression.calculate( temp.toString() );
        }
        return Float.parseFloat( temp.toString() );
    }

    private boolean isG17(ArrayList<StringBuffer> programList) {
        for (int i = 0; i < programList.size(); i++)
            if (programList.get( i ).toString().contains( "G17" )) {
                return true;
            }
        return false;
    }

    private static boolean readUp(char input) {
        switch (input) {
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

    private boolean isDigit(char input) {
        switch (input) {
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

    float coordinateSearch(StringBuffer frame, String axis) {
        Expression expression = new Expression();
        StringBuffer temp = new StringBuffer();
        int n = frame.indexOf( axis );

        if (isDigit( frame.charAt( n + axis.length() ) ) || frame.charAt( n + axis.length() ) == '-' || frame.charAt( n + axis.length() ) == '+') {
            for (int i = n + axis.length(); i < frame.length(); i++) {
                if (readUp( frame.charAt( i ) )) {
                    temp.append( frame.charAt( i ) );
                } else {
                    break;
                }
            }
            return Float.parseFloat( temp.toString() );
        } else if (frame.charAt( n + axis.length() ) == '=') {
            for (int i = n + axis.length() + 1; i < frame.length(); i++) {
                if (readUp( frame.charAt( i ) )) {
                    temp.append( frame.charAt( i ) );
                } else {
                    break;
                }
            }
            return expression.calculate( temp.toString() );
        }
        return FIBO;
    }

    boolean containsAxis(StringBuffer frame,String axis){
        if(contains(frame, axis)){
            int n=frame.indexOf(axis)+1;
            char c=frame.charAt(n);
            switch(c){
                case '-':
                case '=':
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
        }
        return false;
    }

    void selectCoordinateSystem(ArrayList<StringBuffer> programList) {
        for (int i = 0; i < programList.size(); i++) {
            if (programList.get( i ).toString().contains( "X" ))
                x++;
            if (programList.get( i ).toString().contains( "U" ))
                u++;
            if (x > u) {
                horizontalAxis = "X";
                verticalAxis = "Z";
            } else {
                horizontalAxis = "U";
                verticalAxis = "W";
            }
        }
    }

    void containsGCode(String frame) {
        boolean isG17 = isG17( programList );
        if (frame.contains( "G" )) {
            StringBuilder G = new StringBuilder( "G" );
            for (int i = 0; i < frame.length(); i++) {
                char c = frame.charAt( i );
                if (c == 'G') {
                    for (int j = i + 1; j < frame.length(); j++) {
                        char t = frame.charAt( j );
                        if (isDigit( t )) {
                            G.append( t );
                        } else {
                            break;
                        }
                    }
                    switch (G.toString()) {
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
                    G = new StringBuilder( "G" );
                }
            }
        }
    }

    boolean contains(StringBuffer sb, String findString) {
        return sb.indexOf( findString ) > -1;
    }
}
