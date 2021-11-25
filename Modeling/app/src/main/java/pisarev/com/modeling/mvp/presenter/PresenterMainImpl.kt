package pisarev.com.modeling.mvp.presenter

import pisarev.com.modeling.interfaces.MainMvp.ViewMvp
import pisarev.com.modeling.interfaces.MainMvp.PresenterMainMvp
import pisarev.com.modeling.mvp.model.MyFile

class PresenterMainImpl(private val viewMvp: ViewMvp) : PresenterMainMvp {
    private val myFile: MyFile
    override fun openProgram(path: String?) {
        viewMvp.showProgram(myFile.readFile(path))
    }

    override fun saveAll(program: String?, path: String?) {}

    init {
        myFile = MyFile()
    }
}