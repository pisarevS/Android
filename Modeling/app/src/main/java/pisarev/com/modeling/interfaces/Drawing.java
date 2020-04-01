package pisarev.com.modeling.interfaces;

import android.graphics.Canvas;
import pisarev.com.modeling.mvp.model.MyData;
import pisarev.com.modeling.mvp.model.Point;

public interface Drawing {
    void drawContour(Canvas canvas, MyData data, Point pointCoordinateZero, float zoom, int index);
    void setNumberLine(int numberLine);
}
