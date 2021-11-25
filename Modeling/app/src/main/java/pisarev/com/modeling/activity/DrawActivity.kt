package pisarev.com.modeling.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import pisarev.com.modeling.R
import pisarev.com.modeling.interfaces.Callback
import pisarev.com.modeling.interfaces.DrawMvp.DrawViewMvp
import pisarev.com.modeling.interfaces.DrawMvp.PresenterDrawViewMvp
import pisarev.com.modeling.mvp.model.MyFile.Companion.getParameter
import pisarev.com.modeling.mvp.model.SQLiteData
import pisarev.com.modeling.mvp.model.core.MyData
import pisarev.com.modeling.mvp.model.core.ProgramCode
import java.io.File
import java.util.function.Consumer


class DrawActivity : AppCompatActivity(), OnTouchListener, DrawViewMvp, Callback {
    private var drawView: PresenterDrawViewMvp? = null
    private var buttonStart: ImageView? = null
    private var buttonCycleStart: ImageView? = null
    private var buttonSingleBlock: ImageView? = null
    private var buttonReset: ImageView? = null
    private var textViewFrame: TextView? = null
    private var textViewX: TextView? = null
    private var textViewZ: TextView? = null
    private var variablesList: MutableMap<String, String>? = null
    private var count = 0
    private var vibrator: Vibrator? = null
    private var index = 0
    private var isSingleBlock = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw)
        drawView = findViewById(R.id.myView)
        drawView!!.setActivity(this)
        drawView!!.setIndex(index)
        textViewFrame = findViewById(R.id.textViewFrame)
        textViewX = findViewById(R.id.textViewX)
        textViewZ = findViewById(R.id.textViewZ)
        buttonStart = findViewById(R.id.start)
        buttonCycleStart = findViewById(R.id.cycle_start)
        buttonSingleBlock = findViewById(R.id.single_block)
        buttonReset = findViewById(R.id.reset)
        buttonStart!!.setOnTouchListener(this)
        buttonCycleStart!!.setOnTouchListener(this)
        buttonSingleBlock!!.setOnTouchListener(this)
        buttonReset!!.setOnTouchListener(this)
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator


        val pathParameter = SQLiteData(this, SQLiteData.DATABASE_PATH).programText?.get(SQLiteData.KEY_PROGRAM).toString()
        val program= SQLiteData(this, SQLiteData.DATABASE_PROGRAM).programText?.get(SQLiteData.KEY_PROGRAM)
        if(program!=null){
            val thread = Thread(ProgramCode(pathParameter,program , this))
            thread.start()
            /* try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (v.id) {
            R.id.start -> when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    drawView!!.onButtonStart()
                    buttonStart!!.setImageResource(R.drawable.start_down)
                }
                MotionEvent.ACTION_UP -> {
                    if (vibrator!!.hasVibrator()) {
                        vibrator!!.vibrate(20)
                    }
                    buttonStart!!.setImageResource(R.drawable.start)
                }
            }
            R.id.cycle_start -> when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    drawView!!.onButtonCycleStart()
                    buttonCycleStart!!.setImageResource(R.drawable.cycle_start_down)
                }
                MotionEvent.ACTION_UP -> {
                    if (vibrator!!.hasVibrator()) {
                        vibrator!!.vibrate(20)
                    }
                    buttonCycleStart!!.setImageResource(R.drawable.cycle_start)
                }
            }
            R.id.single_block -> if (event.action == MotionEvent.ACTION_DOWN) {
                count++
                if (count % 2 != 0) {
                    if (vibrator!!.hasVibrator()) {
                        vibrator!!.vibrate(20)
                    }
                    isSingleBlock = true
                    drawView!!.onButtonSingleBlock(true)
                    buttonSingleBlock!!.setImageResource(R.drawable.single_block_down)
                } else {
                    if (vibrator!!.hasVibrator()) {
                        vibrator!!.vibrate(20)
                    }
                    isSingleBlock = false
                    drawView!!.onButtonSingleBlock(false)
                    buttonSingleBlock!!.setImageResource(R.drawable.single_block)
                }
            }
            R.id.reset -> when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    drawView!!.onButtonReset()
                    index = 0
                    buttonReset!!.setImageResource(R.drawable.reset_down)
                }
                MotionEvent.ACTION_UP -> {
                    if (vibrator!!.hasVibrator()) {
                        vibrator!!.vibrate(20)
                    }
                    buttonReset!!.setImageResource(R.drawable.reset)
                }
            }
        }
        return true
    }

    override fun showFrame(frame: String?) {
        runOnUiThread { textViewFrame!!.text = frame }
    }

    override fun showAxis(horizontalAxis: String?, verticalAxis: String?) {
        runOnUiThread {
            textViewX!!.text = horizontalAxis
            textViewZ!!.text = verticalAxis
        }
    }

    override fun showIndex(index: Int) {
        this.index = index
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("index", index)
        outState.putBoolean("singleBlock", isSingleBlock)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        drawView!!.setIndex(savedInstanceState.getInt("index"))
        drawView!!.onButtonSingleBlock(isSingleBlock)
        if (isSingleBlock) {
            count++
            buttonSingleBlock!!.setImageResource(R.drawable.single_block_down)
        } else buttonSingleBlock!!.setImageResource(R.drawable.single_block)
    }

    private fun readParameterVariables(parameterList: List<StringBuffer>) {
        variablesList = LinkedHashMap()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            parameterList.forEach(Consumer { p: StringBuffer ->
                if (p.toString().contains(";")) p.delete(p.indexOf(";"), p.length)
                if (p.toString().contains("=")) {
                    var key = 0
                    for (j in p.indexOf("=") - 1 downTo 0) {
                        val c = p[j]
                        if (c == ' ') {
                            key = j
                            break
                        }
                    }
                    (variablesList as LinkedHashMap<String, String>)[p.substring(key, p.indexOf("=")).replace(" ", "")] =
                        p.substring(p.indexOf("=") + 1, p.length).replace(" ", "")
                }
            })
        }
    }

    override fun callingBack(data: MyData?) {
        drawView!!.setData(data!!)
    }
}