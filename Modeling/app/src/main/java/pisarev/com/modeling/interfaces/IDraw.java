package pisarev.com.modeling.interfaces;

import android.graphics.Canvas;

import pisarev.com.modeling.mvp.model.Point;

public interface IDraw {

    void drawContour(Canvas canvas, Point pointCoordinateZero, float zoom,int index);
}
