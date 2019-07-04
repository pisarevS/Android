package pisarev.com.modeling.interfaces;

import android.graphics.Canvas;
import android.graphics.Paint;

import pisarev.com.modeling.mvp.model.Point;

public interface IDraw {

    void drawLine(Canvas canvas, Paint paint, Point pointCoordinateZero, Point pointStart, Point pointEnd, float zoom) ;

    void drawArc(Canvas canvas, Paint paint, Point pointCoordinateZero, Point pointStart, Point pointEnd, float radius, float zoom, boolean clockwise);

    void drawPoint(Canvas canvas, Point pointCoordinateZero, Point pointEnd, float zoom);

    void drawContour(Canvas canvas, Point pointCoordinateZero, float zoom, int index);
}
