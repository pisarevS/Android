package pisarev.com.modeling.mvp.model;

import android.graphics.Canvas;

import pisarev.com.modeling.interfaces.ViewMvp;

public class Draw extends BaseDraw {

    public Draw(ViewMvp.MyViewMvp myViewMvp) {
        super( myViewMvp );
    }

    @Override
    public void drawContour(Canvas canvas, Point pointCoordinateZero, float zoom, int index) {
        boolean isHorizontalAxis = false;
        boolean isVerticalAxis = false;
        boolean isCR = false;
        StringBuffer frame;
        Point pStart = new Point();
        Point pEnd = new Point();
        pStart.setX( 650f );
        pStart.setZ( 250f );
        pEnd.setX( 650f );
        pEnd.setZ( 250f );
        float radius = 0;
        selectCoordinateSystem( programList );

        for (int i = 0; i < index; i++) {
            frame = programList.get( i );
            containsGCode( frame.toString() );
            try {
                if (contains( frame, horizontalAxis + "=IC" )) {
                    pEnd.setX( pEnd.getX() + incrementSearch( frame, horizontalAxis + "=IC" ) );
                    isHorizontalAxis = true;
                } else if (containsAxis( frame, horizontalAxis )) {
                    float tempHorizontal = coordinateSearch( frame, horizontalAxis );
                    if (tempHorizontal != FIBO) {
                        pEnd.setX( tempHorizontal );
                        isHorizontalAxis = true;
                    } else {
                        myViewMvp.showError( "Error " + horizontalAxis + "\n" + frame.toString() );
                    }
                }
            }catch (Exception e){
                myViewMvp.showError( "Error " + horizontalAxis + "\n" + frame.toString() );
            }
            try {
                if (contains( frame, verticalAxis + "=IC" )) {
                    pEnd.setZ( pEnd.getZ() + incrementSearch( frame, verticalAxis + "=IC" ) );
                    isVerticalAxis = true;
                } else if (containsAxis( frame, verticalAxis )) {
                    float tempVertical = coordinateSearch( frame, verticalAxis );
                    if (tempVertical != FIBO) {
                        pEnd.setZ( tempVertical );
                        isVerticalAxis = true;
                    } else {
                        myViewMvp.showError( "Error " + verticalAxis + "\n" + frame.toString() );
                    }
                }
            }catch (Exception e){
                myViewMvp.showError( "Error " + verticalAxis + "\n" + frame.toString() );
            }
            String radiusCR = "CR=";
            try {
                if (contains( frame, radiusCR )) {
                    float tempCR = coordinateSearch( frame, radiusCR );
                    if (tempCR != FIBO) {
                        radius = tempCR;
                        isCR = true;
                    } else {
                        myViewMvp.showError( "Error " + radiusCR + "\n" + frame.toString() );
                    }
                }
            }catch (Exception e){
                myViewMvp.showError( "Error " + radiusCR + "\n" + frame.toString() );
            }
            if (isHorizontalAxis && isVerticalAxis && isCR) {
                drawArc( canvas, line, pointCoordinateZero, pStart, pEnd, radius, zoom, clockwise );
                pStart.setX( pEnd.getX() );
                pStart.setZ( pEnd.getZ() );
                isHorizontalAxis = false;
                isVerticalAxis = false;
                isCR = false;
            }
            if (isHorizontalAxis || isVerticalAxis) {
                drawLine( canvas, line, pointCoordinateZero, pStart, pEnd, zoom );
                pStart.setX( pEnd.getX() );
                pStart.setZ( pEnd.getZ() );
                isHorizontalAxis = false;
                isVerticalAxis = false;
            }
        }
        drawPoint( canvas, pointCoordinateZero, pEnd, zoom );
    }
}

