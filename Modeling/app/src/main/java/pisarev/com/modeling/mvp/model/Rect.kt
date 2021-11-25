package pisarev.com.modeling.mvp.model

import pisarev.com.modeling.mvp.contur.Point

class Rect {
    var height = 0F
    var width = 0F
    var x = 0F
    var z = 0F

    constructor() {}

    fun setRect(x: Float, z: Float, height: Float, width: Float) {
        this.x = x
        this.z = z
        this.height = height
        this.width = width
    }

    fun isInsideRect(point : Point): Boolean {
        val x = point.x
        val z = point.z
        return x >= this.x && x <= this.x + width && z >= this.z && z <= this.z + height
    }
}