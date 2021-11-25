package pisarev.com.modeling.mvp.contur

import pisarev.com.modeling.mvp.model.core.GCode

open class Point : GCode {

    var x = N_GANTRYPOS_X.toFloat()
    var z = N_GANTRYPOS_Z.toFloat()

    constructor() {}

    constructor(point: Point) {
        this.x = point.x
        this.z = point.z
    }
}