package pisarev.com.modeling.mvp.contur

import kotlin.math.acos
import kotlin.math.sqrt

class Point2D {

    var x: Double
    var z: Double

    constructor(x: Float, z: Float) {
        this.x = x.toDouble()
        this.z = z.toDouble()
    }

    fun distance(x: Float, z: Float): Float {
        val a = x - x
        val b = z - z
        return sqrt((a * a + b * b).toDouble()).toFloat()
    }

    fun angle(x: Float, y: Float): Float {
        val ax = x
        val ay = z
        val delta = ((ax * x + ay * y) / sqrt(
            (
                    (ax * ax + ay * ay) * (x * x + y * y))
        )).toFloat()
        if (delta > 1.0) {
            return 0.0F
        }
        return if (delta < -1.0) {
            180.0F
        } else Math.toDegrees(acos(delta.toDouble())).toFloat()
    }

}