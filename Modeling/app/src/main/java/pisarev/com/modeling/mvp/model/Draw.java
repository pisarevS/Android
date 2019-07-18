package pisarev.com.modeling.mvp.model;

import android.graphics.Canvas;

import java.util.ArrayList;

import pisarev.com.modeling.interfaces.ViewMvp;

public class Draw extends BaseDraw {

    public Draw(ViewMvp.MyViewMvp myViewMvp) {
        super( myViewMvp );
    }

    @Override
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
            checkGCode(frmeList.get( i ).getGCode());

            if (frmeList.get( i ).getIsCR()){
                pEnd.setX(frmeList.get(i).getX());
                pEnd.setZ( frmeList.get( i ).getZ());
                radius=frmeList.get( i ).getCr();
                isRadius=true;
            }else {
                pEnd.setX( frmeList.get( i ).getX() );
                pEnd.setZ( frmeList.get( i ).getZ() );
                isLine=true;
            }
            if ( isRadius &&frmeList.get( i ).isAxisContains()) {
                drawArc( canvas, line, pointCoordinateZero, pStart, pEnd, radius, zoom, clockwise );
                pStart.setX( pEnd.getX() );
                pStart.setZ( pEnd.getZ() );
                isLine=false;
                isRadius=false;
            }
            if (isLine && frmeList.get( i ).isAxisContains()) {
                drawLine( canvas, line, pointCoordinateZero, pStart, pEnd, zoom );
                pStart.setX( pEnd.getX() );
                pStart.setZ( pEnd.getZ() );
            }
        }
        drawPoint( canvas, pointCoordinateZero, pEnd, zoom );
    }



}

