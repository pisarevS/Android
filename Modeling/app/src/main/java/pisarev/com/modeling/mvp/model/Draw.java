package pisarev.com.modeling.mvp.model;

import android.graphics.Canvas;

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
            checkGCode( frameList.get( i ).getGCode());

            if(data.getErrorListMap().containsKey( frameList.get( i ).getId() )){
                myViewMvp.showError( data.getErrorListMap().get( frameList.get( i ).getId() ) );
                break;
            }else {
                if (frameList.get( i ).getIsCR()){
                    pEnd.setX( frameList.get(i).getX());
                    pEnd.setZ( frameList.get( i ).getZ());
                    radius= frameList.get( i ).getCr();
                    isRadius=true;
                }else {
                    pEnd.setX( frameList.get( i ).getX() );
                    pEnd.setZ( frameList.get( i ).getZ() );
                    isLine=true;
                }
                if ( isRadius && frameList.get( i ).isAxisContains()) {
                    drawArc( canvas, line, pointCoordinateZero, pStart, pEnd, radius, zoom, clockwise );
                    pStart.setX( pEnd.getX() );
                    pStart.setZ( pEnd.getZ() );
                    isLine=false;
                    isRadius=false;
                }
                if (isLine && frameList.get( i ).isAxisContains()) {
                    drawLine( canvas, line, pointCoordinateZero, pStart, pEnd, zoom );
                    pStart.setX( pEnd.getX() );
                    pStart.setZ( pEnd.getZ() );
                }
            }
        }
        drawPoint( canvas, pointCoordinateZero, pEnd, zoom );
    }

}

