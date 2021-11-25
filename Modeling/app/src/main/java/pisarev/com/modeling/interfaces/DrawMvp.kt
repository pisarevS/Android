package pisarev.com.modeling.interfaces

import pisarev.com.modeling.mvp.model.core.MyData

interface DrawMvp {
    interface PresenterDrawViewMvp {
        fun onButtonStart()
        fun onButtonCycleStart()
        fun onButtonSingleBlock(isSingleBlockDown: Boolean)
        fun onButtonReset()
        fun setActivity(secondView: DrawViewMvp)
        fun setIndex(index: Int)
        fun setData(data: MyData)
    }

    interface DrawViewMvp {
        fun showFrame(frame: String?)
        fun showAxis(horizontalAxis: String?, verticalAxis: String?)
        fun showIndex(index: Int)
    }
}