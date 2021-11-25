package pisarev.com.modeling.interfaces

import android.graphics.Canvas
import pisarev.com.modeling.mvp.model.core.MyData
import pisarev.com.modeling.mvp.contur.Point

interface Drawing {
    fun drawContour(canvas: Canvas, data: MyData, pointCoordinateZero: Point, zoom: Float, index: Int)
    fun setNumberLine(numberLine: Int?)
}