package pisarev.com.modeling.interfaces

import pisarev.com.modeling.mvp.model.core.MyData

interface Callback {
    fun callingBack(data: MyData?)
}