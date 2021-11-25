package pisarev.com.modeling.mvp.view.customview

import pisarev.com.modeling.interfaces.IDraw
import pisarev.com.modeling.interfaces.DrawMvp.PresenterDrawViewMvp
import pisarev.com.modeling.interfaces.DrawMvp.DrawViewMvp
import android.view.ScaleGestureDetector
import pisarev.com.modeling.mvp.model.core.MyData
import pisarev.com.modeling.interfaces.Drawing
import android.preference.PreferenceManager
import pisarev.com.modeling.mvp.contur.DrawVerticalTurning
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.support.annotation.RequiresApi
import android.os.Build
import android.view.MotionEvent
import android.content.DialogInterface
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.view.View
import pisarev.com.modeling.mvp.model.core.Frame
import pisarev.com.modeling.mvp.contur.Point
import pisarev.com.modeling.mvp.model.Rect
import java.util.*
import kotlin.math.abs

class DrawView : View, IDraw, PresenterDrawViewMvp {
    private var paintCoordinateDottedLine: Paint? = null
    private var drawActivity: DrawViewMvp? = null
    private var pointSystemCoordinate: Point? = null
    private var isTouch = false
    private var isMove = false
    private var moveX = 0f
    private var moveZ = 0f
    private var zooming = 3f
    var button = 0
    val START = 1
    val RESET = 2
    val STOP = 3
    val TAP = 4
    private var index = 0
    private var numberLine = 0
    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var errorList: ArrayList<String>? = null
    private var isSingleBlockDown = false
    private var isResetDown = false
    private var isStartDown = false
    private val TEG = javaClass.name
    private var data: MyData? = null
    private var drawing: Drawing? = null
    private var isDrawPoint = false
    private var pointStopCanvas: Point? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
        val myPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        data = MyData()
        paintCoordinateDottedLine = Paint()
        paintCoordinateDottedLine!!.color = Color.LTGRAY
        paintCoordinateDottedLine!!.style = Paint.Style.STROKE
        paintCoordinateDottedLine!!.isAntiAlias = true
        paintCoordinateDottedLine!!.pathEffect = DashPathEffect(floatArrayOf(20f, 10f), 0f)
        pointSystemCoordinate = Point()
        errorList = ArrayList()
        drawing = DrawVerticalTurning(this)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        when (button) {
            START, STOP -> {
                drawing!!.drawContour(canvas, data!!, Point(pointSystemCoordinate!!), zooming, index)
                invalidate()
            }
            TAP -> {
                drawing!!.setNumberLine(numberLine)
                drawing!!.drawContour(canvas, data!!, Point(pointSystemCoordinate!!), zooming, index)
                invalidate()
            }
            RESET -> {
                button = 0
                invalidate()
                drawing!!.setNumberLine(-1)
                errorList!!.clear()
            }
        }
        drawSystemCoordinate(canvas, isTouch)
    }

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleGestureDetector!!.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val downX = event.x
                val downZ = event.y
                moveX = pointSystemCoordinate!!.x - downX
                moveZ = pointSystemCoordinate!!.z - downZ
                isTouch = true
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                pointSystemCoordinate!!.x=event.x + moveX
                pointSystemCoordinate!!.z=event.y + moveZ
                invalidate()
                isMove = true
            }
            MotionEvent.ACTION_UP -> {
                if (!isMove) {
                    val point = Point()
                    point.x=(pointSystemCoordinate!!.x - event.x) * -1
                    point.z=event.y
                    if (point.z > 0) point.z=pointSystemCoordinate!!.z - point.z else point.z= pointSystemCoordinate!!.z + Math.abs(point.z)
                    point.x=point.x / zooming
                    point.z=point.z/ zooming
                    val frame = getFrame(point)
                    if (frame.isPresent) {
                        numberLine = frame.get().id
                        button = TAP
                        invalidate()
                        drawActivity!!.showFrame(data!!.programList[frame.get().id].toString())
                        drawActivity!!.showAxis("X=" + frame.get().x, "Z=" + frame.get().z)
                        System.out.println("X=" + point.x)
                        System.out.println("Z=" + point.z)
                        println(data!!.programList[frame.get().id])
                        isDrawPoint = true
                    } else if (isDrawPoint) {
                        numberLine = -1
                        button = TAP
                        drawActivity!!.showFrame("")
                        drawActivity!!.showAxis("", "")
                        isDrawPoint = false
                    }
                }
                isMove = false
                invalidate()
            }
        }
        return true
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private fun getFrame(point: Point): Optional<Frame> {
        val side = 20
        val rect = Rect()
        rect.setRect(point.x - (side shr 1), point.z - (side shr 1), side.toFloat(), side.toFloat())
        return data!!.frameList.stream()
            .filter { rect.isInsideRect(point) }
            .min(Comparator.comparingDouble { p: Frame -> (abs(point.x - p.x) + abs(point.z - p.z)) })
    }

    override fun showError(error: String) {
        button = STOP
        if (!errorList!!.contains(error)) {
            errorList!!.add(error)
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Error")
                .setMessage(error)
                .setCancelable(false)
                .setNegativeButton("OK") { dialog: DialogInterface, which: Int -> dialog.cancel() }
            val alertDialog = builder.create()
            alertDialog.show()
            Log.d(TEG, "error $error")
        }
    }

    private fun drawSystemCoordinate(canvas: Canvas, isTouch: Boolean) {
        if (!isTouch || button == RESET) {
            initSystemCoordinate(canvas, true)
            invalidate()
        }
        if (isTouch || button == START) {
            drawing!!.drawContour(canvas, data!!, Point(pointSystemCoordinate!!), zooming, index)
            initSystemCoordinate(canvas, false)
            invalidate()
        }
    }

    private fun initSystemCoordinate(canvas: Canvas, isInit: Boolean) {
        val path: Path
        if (isInit) {
            path = Path()
            pointSystemCoordinate!!.x= (width shr 1).toFloat()
            pointSystemCoordinate!!.z=(height shr 1).toFloat()
            path.moveTo(0f, pointSystemCoordinate!!.z)
            path.lineTo(width.toFloat(), pointSystemCoordinate!!.z)
            path.moveTo(pointSystemCoordinate!!.x, 0f)
            path.lineTo(pointSystemCoordinate!!.x, height.toFloat())
            canvas.drawPath(path, paintCoordinateDottedLine!!)
        } else {
            path = Path()
            path.moveTo(0f, pointSystemCoordinate!!.z)
            path.lineTo(width.toFloat(), pointSystemCoordinate!!.z)
            path.moveTo(pointSystemCoordinate!!.x, 0f)
            path.lineTo(pointSystemCoordinate!!.x, height.toFloat())
            canvas.drawPath(path, paintCoordinateDottedLine!!)
        }
    }

    override fun onButtonStart() {
        isStartDown = true
        index = data!!.frameList.size
        if (data!!.frameList.size > 0) {
            button = START
            isResetDown = false
        }
    }

    override fun onButtonCycleStart() {
        if (isSingleBlockDown && index < data!!.frameList.size) {
            isResetDown = false
            button = START
            index++
            drawActivity!!.showFrame(data!!.programList[data!!.frameList[index - 1].id].toString())
            if (data!!.frameList[index - 1].isAxisContains) {
                drawActivity!!.showAxis("X=" + data!!.frameList[index - 1].x, "Z=" + data!!.frameList[index - 1].z)
                drawActivity!!.showIndex(index)
            }
        }
        if (!isSingleBlockDown && index < data!!.frameList.size && !isStartDown) {
            isResetDown = false
            isStartDown = true
            button = START
            val timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    if (index < data!!.frameList.size && !isSingleBlockDown && !isResetDown && button == START) {
                        index++
                        drawActivity!!.showFrame(data!!.programList[data!!.frameList[index - 1].id].toString())
                        if (data!!.frameList[index - 1].isAxisContains) {
                            drawActivity!!.showAxis(
                                "X=" + data!!.frameList[index - 1].x,
                                "Z=" + data!!.frameList[index - 1].z
                            )
                            drawActivity!!.showIndex(index)
                        }
                    } else {
                        isResetDown = false
                        timer.cancel()
                        if (button == STOP) {
                            drawActivity!!.showFrame(data!!.programList[data!!.frameList[index - 1].id].toString())
                            if (data!!.frameList[index - 1].isAxisContains) {
                                drawActivity!!.showAxis(
                                    "X=" + data!!.frameList[index - 1].x,
                                    "Z=" + data!!.frameList[index - 1].z
                                )
                                drawActivity!!.showIndex(index)
                            }
                        }
                    }
                }
            }, 0, 200)
        }
    }

    override fun onButtonSingleBlock(isSingleBlockDown: Boolean) {
        this.isSingleBlockDown = isSingleBlockDown
        isStartDown = false
    }

    override fun onButtonReset() {
        isResetDown = true
        button = RESET
        index = 0
        isStartDown = false
        drawActivity!!.showFrame("")
        drawActivity!!.showAxis("", "")
    }

    override fun setActivity(secondView: DrawViewMvp) {
        drawActivity = secondView
    }

    override fun setIndex(index: Int) {
        this.index = index
    }

    override fun setData(data: MyData) {
        this.data = data
    }

    inner class ScaleListener : SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            zooming *= detector.scaleFactor
            val point = Point()
            point.x=(pointSystemCoordinate!!.x - detector.focusX) * -1
            point.z=detector.focusY
            if (point.z > 0) point.z=pointSystemCoordinate!!.z - point.z else point.z=
                pointSystemCoordinate!!.z + Math.abs(point.z
            )
            pointStopCanvas = point
            point.x=pointStopCanvas!!.x * zooming
            point.z=pointStopCanvas!!.z * zooming
            pointSystemCoordinate!!.x=pointSystemCoordinate!!.x + pointStopCanvas!!.x - point.x
            pointSystemCoordinate!!.z=pointSystemCoordinate!!.z + point.z - pointStopCanvas!!.z
            pointStopCanvas!!.x=pointStopCanvas!!.x * zooming
            pointStopCanvas!!.z=pointStopCanvas!!.z * zooming
            return true
        }
    }
}