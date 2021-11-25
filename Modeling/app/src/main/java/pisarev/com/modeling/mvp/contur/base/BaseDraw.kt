package pisarev.com.modeling.mvp.contur.base

import android.graphics.*
import pisarev.com.modeling.interfaces.IDraw
import pisarev.com.modeling.mvp.contur.Point
import pisarev.com.modeling.mvp.contur.Point2D
import pisarev.com.modeling.mvp.model.core.GCode
import kotlin.math.acos
import kotlin.math.pow
import kotlin.math.sqrt

abstract class BaseDraw : GCode {
    protected var draw: IDraw? = null
    protected var isNumberLine = false
    protected var numberLine = 0
    protected var colorPoint = Color.RED
    protected var colorTouchPoint = Color.rgb(0, 128, 255)
    private var line: Paint? = null
    private var paintFullLine: Paint? = null
    private var paintDottedLine: Paint? = null

    protected constructor(draw: IDraw?) {
        this.draw = draw
        init()
    }

    private fun init() {
        line = Paint()
        paintFullLine = Paint()
        paintFullLine!!.color = Color.GREEN
        paintFullLine!!.style = Paint.Style.STROKE
        paintFullLine!!.isAntiAlias = true
        paintDottedLine = Paint()
        paintDottedLine!!.color = Color.GRAY
        paintDottedLine!!.style = Paint.Style.STROKE
        paintDottedLine!!.isAntiAlias = true
        paintDottedLine!!.pathEffect = DashPathEffect(floatArrayOf(12f, 7f), 0f)
    }

    protected fun drawLine(
        canvas: Canvas,
        isRapidFeed: Int,
        pointCoordinateZero: Point,
        pointStart: Point,
        pointEnd: Point,
        zoom: Float
    ) {
        when(isRapidFeed){
            0 -> line = paintDottedLine
            1 -> line =  paintFullLine
        }
        val path = Path()
        val pStart = Point(pointStart)
        val pEnd = Point(pointEnd)
        val pointCoordinateZeroF = Point(pointCoordinateZero)
        pStart.x = pStart.x * zoom
        pStart.z = pStart.z * zoom
        pEnd.x = pEnd.x * zoom
        pEnd.z = pEnd.z * zoom
        if (pStart.z > 0) pStart.z = pointCoordinateZeroF.z - pStart.z else pStart.z =
            pointCoordinateZeroF.z + Math.abs(pStart.z)
        if (pEnd.z > 0) pEnd.z = pointCoordinateZeroF.z - pEnd.z else pEnd.z = pointCoordinateZeroF.z + Math.abs(pEnd.z)

        path.moveTo(pointCoordinateZeroF.x + pStart.x, pStart.z)
        path.lineTo(pointCoordinateZeroF.x + pEnd.x, pEnd.z)
        canvas.drawPath(path, line!!)
    }

    protected fun drawArc(
        canvas: Canvas,
        isRapidFeed: Int,
        pointCoordinateZero: Point,
        pointStart: Point,
        pointEnd: Point,
        radius: Float,
        zoom: Float,
        clockwise: Int
    ) {
        when(isRapidFeed){
            0 -> line = paintDottedLine
            1 -> line =  paintFullLine
        }
        var r = radius
        val path = Path()
        val pStart = Point(pointStart)
        val pEnd = Point(pointEnd)
        val pointCoordinateZeroF = Point(pointCoordinateZero)
        val rectF = RectF()
        pStart.x = pStart.x * zoom
        pStart.z = pStart.z * zoom
        pEnd.x = pEnd.x * zoom
        pEnd.z = pEnd.z * zoom
        r *= zoom
        var startAngle = 0f
        val sweetAngle: Float
        var cathetus: Float
        val chord = sqrt(
            (pStart.x - pEnd.x).toDouble().pow(2.0) + (pStart.z - pEnd.z).toDouble().pow(2.0)
        ).toFloat()
        val h = sqrt((r * r - chord / 2 * (chord / 2)).toDouble()).toFloat()
        if (clockwise == 2) {
            val x01: Float =
                pStart.x + (pEnd.x - pStart.x) / 2 + h * (pEnd.z - pStart.z) / chord
            val z01: Float =
                pStart.z + (pEnd.z - pStart.z) / 2 - h * (pEnd.x - pStart.x) / chord
            if (pStart.x > x01 && pStart.z >= z01) {
                cathetus = pStart.x - x01
                startAngle =
                    if (pStart.z == z01) 0f else (360 - acos((cathetus / r).toDouble()) * (180 / Math.PI)).toFloat()
            }
            if (pStart.x >= x01 && pStart.z < z01) {
                cathetus = pStart.x - x01
                startAngle =
                    if (pStart.x == x01) 90f else (acos((cathetus / r).toDouble()) * (180 / Math.PI)).toFloat()
            }
            if (pStart.x < x01 && pStart.z <= z01) {
                cathetus = x01 - pStart.x
                startAngle =
                    if (pStart.z == z01) 180f else (180 - acos((cathetus / r).toDouble()) * (180 / Math.PI)).toFloat()
            }
            if (pStart.x <= x01 && pStart.z > z01) {
                cathetus = x01 - pStart.x
                startAngle =
                    if (pStart.x == x01) 270f else (180 + acos((cathetus / r).toDouble()) * (180 / Math.PI)).toFloat()
            }
            rectF[pointCoordinateZeroF.x + x01 - r, pointCoordinateZeroF.z - z01 - r, pointCoordinateZeroF.x + x01 + r] =
                pointCoordinateZeroF.z - z01 + r
        }
        if (clockwise == 3) {
            val x02: Float =
                pStart.x + (pEnd.x - pStart.x) / 2 - h * (pEnd.z - pStart.z) / chord
            val z02: Float =
                pStart.z + (pEnd.z - pStart.z) / 2 + h * (pEnd.x - pStart.x) / chord
            if (pEnd.x > x02 && pEnd.z >= z02) {
                cathetus = pEnd.x - x02
                startAngle =
                    if (pEnd.z == z02) 0f else (360 - acos((cathetus / r).toDouble()) * (180 / Math.PI)).toFloat()
            }
            if (pEnd.x >= x02 && pEnd.z < z02) {
                cathetus = pEnd.x - x02
                startAngle =
                    if (pEnd.x == x02) 90f else (acos((cathetus / r).toDouble()) * (180 / Math.PI)).toFloat()
            }
            if (pEnd.x < x02 && pEnd.z <= z02) {
                cathetus = x02 - pEnd.x
                startAngle =
                    if (pEnd.z == z02) 180f else (180 - acos((cathetus / r).toDouble()) * (180 / Math.PI)).toFloat()
            }
            if (pEnd.x <= x02 && pEnd.z > z02) {
                cathetus = x02 - pEnd.x
                startAngle =
                    if (pEnd.x == x02) 270f else (180 + acos((cathetus / r).toDouble()) * (180 / Math.PI)).toFloat()
            }
            rectF[pointCoordinateZeroF.x + x02 - r, pointCoordinateZeroF.z - z02 - r, pointCoordinateZeroF.x + x02 + r] =
                pointCoordinateZeroF.z - z02 + r
        }
        sweetAngle = (2 * Math.asin((chord / (2 * r)).toDouble()) * (180 / Math.PI)).toFloat()
        path.addArc(rectF, startAngle, sweetAngle)
        canvas.drawPath(path, line!!)
    }

    protected fun drawRND(
        canvas: Canvas,
        isRapidFeed: Int,
        pointSystemCoordinate: Point,
        pointStart: Point,
        pointEnd: Point,
        pointF: Point,
        radiusRND: Float,
        zoom: Float
    ) {
        when(isRapidFeed){
            0 -> line = paintDottedLine
            1 -> line =  paintFullLine
        }
        val pStart = Point(pointStart)
        val pEnd = Point(pointEnd)
        val pointStartCR = Point()
        val pointEndCR = Point()
        val pointF = Point(pointF)
        val cathet: Float
        var clockwiseRND = 3
        val angle = Point2D(pEnd.x - pStart.x, pEnd.z - pStart.z).angle(pEnd.x - pointF.x, pEnd.z) - pointF.z
        val firstDistance = Point2D(pStart.x, pStart.z).distance(pEnd.x, pEnd.z)
        val secondDistance = Point2D(pEnd.x, pEnd.z).distance(pointF.x, pointF.z)
        cathet = if (angle == 90F) {
            radiusRND
        } else {
            ((180 - angle) / 2 * (Math.PI / 180) * radiusRND).toFloat()
        }
        var differenceX = pStart.x - pEnd.x
        var differenceZ = pStart.z - pEnd.z
        pointStartCR.x = differenceX * cathet / firstDistance
        pointStartCR.z = differenceZ * cathet / firstDistance
        pointStartCR.x = pEnd.x + pointStartCR.x
        pointStartCR.z = pEnd.z + pointStartCR.z
        differenceX = pointF.x - pEnd.x
        differenceZ = pointF.z - pEnd.z
        pointEndCR.x = differenceX * cathet / secondDistance
        pointEndCR.z = differenceZ * cathet / secondDistance
        pointEndCR.x = pEnd.x + pointEndCR.x
        pointEndCR.z = pEnd.z + pointEndCR.z
        if (pStart.x > pointF.x && (pStart.z + pointF.z) / 2 > pEnd.z) {
            clockwiseRND = 2
        }
        if (pStart.x > pointF.x && (pStart.z + pointF.z) / 2 < pEnd.z) {
            clockwiseRND = 3
        }
        if (pStart.x < pointF.x && (pStart.z + pointF.z) / 2 < pEnd.z) {
            clockwiseRND = 2
        }
        drawLine(canvas, isRapidFeed, pointSystemCoordinate, Point(pStart), Point(pointStartCR), zoom)
        drawArc(
            canvas,
            isRapidFeed,
            pointSystemCoordinate,
            Point(pointStartCR),
            Point(pointEndCR),
            radiusRND,
            zoom,
            clockwiseRND
        )
        pEnd.x = pointEndCR.x
        pEnd.z = pointEndCR.z
    }

    protected fun drawPoint(canvas: Canvas, pointCoordinateZero: Point, pointEnd: Point, color: Int, zoom: Float) {
        val radiusPoint = 9f
        val paint = Paint()
        paint.style = Paint.Style.FILL
        paint.color = color
        val path = Path()
        val pEnd = Point(pointEnd)
        val pointCoordinateZeroF = Point(pointCoordinateZero)
        pEnd.x = pEnd.x * zoom
        pEnd.z = pEnd.z * zoom
        if (pEnd.z > 0) pEnd.z = pointCoordinateZeroF.z - pEnd.z else pEnd.z = pointCoordinateZeroF.z + Math.abs(pEnd.z)

        path.addCircle(pointCoordinateZeroF.x + pEnd.x, pEnd.z, radiusPoint, Path.Direction.CW)
        canvas.drawPath(path, paint)
    }

    protected fun isG17(gCodes: List<String>): Boolean {
        if(gCodes.toString().contains(G17)) return true
        return false
    }


}