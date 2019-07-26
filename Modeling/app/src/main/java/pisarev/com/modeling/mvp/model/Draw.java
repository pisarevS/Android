package pisarev.com.modeling.mvp.model;

import android.graphics.Canvas;

<<<<<<< HEAD
import pisarev.com.modeling.interfaces.IDraw;
import pisarev.com.modeling.mvp.model.base.BaseDraw;
=======
import pisarev.com.modeling.interfaces.ViewMvp;
import pisarev.com.modeling.mvp.view.customview.DrawView;
>>>>>>> b469e35f2e1080e66554d0301c51fb0b9b3081e5

public class Draw extends BaseDraw {

    public Draw(IDraw draw, MyData data) {
        super( draw, data );
    }

    public void drawContour(Canvas canvas, Point pointCoordinateZero, float zoom, int index) {
        boolean isLine = false;
        boolean isRadius = false;
        Point pStart = new Point();
        Point pEnd = new Point();
        pStart.setX( 650f );
        pStart.setZ( 250f );
        pEnd.setX( 650f );
        pEnd.setZ( 250f );

        float radius = 0;
        for (int i = 0; i < index; i++) {
<<<<<<< HEAD
            checkGCode( frameList.get( i ).getGCode() );

            if (data.getErrorListMap().containsKey( frameList.get( i ).getId() )) {
                draw.showError( data.getErrorListMap().get( frameList.get( i ).getId() ) );
                break;
            } else {
                if (frameList.get( i ).getIsCR()) {
                    pEnd.setX( frameList.get( i ).getX() );
                    pEnd.setZ( frameList.get( i ).getZ() );
                    radius = frameList.get( i ).getCr();
                    isRadius = true;
                } else {
                    pEnd.setX( frameList.get( i ).getX() );
                    pEnd.setZ( frameList.get( i ).getZ() );
                    isLine = true;
                }
                if (isRadius && frameList.get( i ).isAxisContains()) {
                    drawArc( canvas, line, pointCoordinateZero, pStart, pEnd, radius, zoom, clockwise );
                    pStart.setX( pEnd.getX() );
                    pStart.setZ( pEnd.getZ() );
                    isLine = false;
                    isRadius = false;
                }
                if (isLine && frameList.get( i ).isAxisContains()) {
                    drawLine( canvas, line, pointCoordinateZero, pStart, pEnd, zoom );
                    pStart.setX( pEnd.getX() );
                    pStart.setZ( pEnd.getZ() );
                }
=======
            frame = programList.get( i );
            checkGCode2( frame.toString() );
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
                        myViewMvp.showError( "Invalid coordinate " + horizontalAxis + "\n" + frame.toString() );
                    }
                }
            }catch (Exception e){
                myViewMvp.showError( "Invalid coordinate " + horizontalAxis + "\n" + frame.toString() );
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
                        myViewMvp.showError( "Invalid coordinate " + verticalAxis + "\n" + frame.toString() );
                    }
                }
            }catch (Exception e){
                myViewMvp.showError( "Invalid coordinate " + verticalAxis + "\n" + frame.toString() );
            }
            String radiusCR = "CR=";
            try {
                if (contains( frame, radiusCR )) {
                    float tempCR = coordinateSearch( frame, radiusCR );
                    if (tempCR != FIBO) {
                        radius = tempCR;
                        isCR = true;
                    } else {
                        myViewMvp.showError( "Invalid radius CR" + "\n" + frame.toString() );
                    }
                }
            }catch (Exception e){
                myViewMvp.showError( "Invalid radius CR" + "\n" + frame.toString() );
            }
            if (isHorizontalAxis && isVerticalAxis && isCR&& gCode.equals(G2) || gCode.equals(G3)) {
                drawArc( canvas, line, pointCoordinateZero, pStart, pEnd, radius, zoom, clockwise );
                pStart.setX( pEnd.getX() );
                pStart.setZ( pEnd.getZ() );
                isHorizontalAxis = false;
                isVerticalAxis = false;
                isCR = false;
            }
            if((isHorizontalAxis && isVerticalAxis && isCR&& !gCode.equals(G2) )||
                    (isHorizontalAxis && isVerticalAxis && isCR&& !gCode.equals(G3))){
                myViewMvp.showError( "Radius direction not specified G code" + "\n" + frame.toString() );
                isCR = false;
            }
            if (isHorizontalAxis || isVerticalAxis) {
                drawLine( canvas, line, pointCoordinateZero, pStart, pEnd, zoom );
                pStart.setX( pEnd.getX() );
                pStart.setZ( pEnd.getZ() );
                isHorizontalAxis = false;
                isVerticalAxis = false;
>>>>>>> b469e35f2e1080e66554d0301c51fb0b9b3081e5
            }
        }
        drawPoint( canvas, pointCoordinateZero, pEnd, zoom );
    }

}

