package pisarev.com.modeling.mvp.contur

import android.graphics.Canvas
import pisarev.com.modeling.interfaces.IDraw
import pisarev.com.modeling.mvp.contur.base.BaseDraw
import pisarev.com.modeling.interfaces.Drawing
import pisarev.com.modeling.mvp.model.core.MyData

class DrawVerticalTurning(draw: IDraw?) : BaseDraw(draw), Drawing {
    override fun drawContour(canvas: Canvas, data: MyData, pointCoordinateZero: Point, zoom: Float, index: Int) {
        val frameList = data.frameList
        var isDrawPoint = false
        val pStart = Point()
        val pEnd = Point()
        val point = Point()

        var radius: Float
        var radiusRND: Float
        for (i in 0 until index) {
            if (frameList[i].gCodes.contains(G17) || frameList[i].gCodes.contains(G18)) isG17 =
                isG17(frameList[i].gCodes)
            checkGCode(frameList[i].gCodes)
            if (data.errorListMap.containsKey(frameList[i].id)) {
                draw!!.showError(data.errorListMap[frameList[i].id]!!)
                break
            } else {
                if (frameList[i].isCR && frameList[i].isAxisContains) {                                  //draw Arc
                    pEnd.x=frameList[i].x.toFloat()
                    pEnd.z=frameList[i].z.toFloat()
                    radius = frameList[i].cr.toFloat()
                    drawArc(canvas,isRapidFeed, pointCoordinateZero, pStart, pEnd, radius, zoom, clockwise)
                    pStart.x=pEnd.x
                    pStart.z=pEnd.z
                }
                if (!frameList[i].isCR && !frameList[i].isRND && frameList[i].isAxisContains) {    //draw line
                    pEnd.x=frameList[i].x.toFloat()
                    pEnd.z=frameList[i].z.toFloat()
                    drawLine(canvas, isRapidFeed, pointCoordinateZero, pStart, pEnd, zoom)
                    pStart.x=pEnd.x
                    pStart.z=pEnd.z
                }
                if (frameList[i].isRND && frameList[i].isAxisContains) {                                    //draw RND
                    pEnd.x=frameList[i].x.toFloat()
                    pEnd.z=frameList[i].z.toFloat()
                    radiusRND = frameList[i].rnd.toFloat()
                    val pointF = Point()
                    pointF.x=frameList[i + 1].x.toFloat()
                    pointF.z=frameList[i + 1].z.toFloat()
                    drawRND(canvas, isRapidFeed, pointCoordinateZero, pStart, pEnd, pointF, radiusRND, zoom)
                    pStart.x=pEnd.x
                    pStart.z=pEnd.z
                    pEnd.x=frameList[i].x.toFloat()
                    pEnd.z=frameList[i].z.toFloat()
                }
                if (isNumberLine && frameList[i].id == numberLine) {
                    point.x=frameList[i].x.toFloat()
                    point.z=frameList[i].z.toFloat()
                    isDrawPoint = true
                }
            }
        }
        drawPoint(canvas, pointCoordinateZero, pEnd, colorPoint, zoom)
        if (isDrawPoint) {
            drawPoint(canvas, pointCoordinateZero, point, colorTouchPoint, zoom)
        }
    }

    override fun setNumberLine(numberLine: Int?) {
        isNumberLine = true
        this.numberLine = numberLine!!
    }


}