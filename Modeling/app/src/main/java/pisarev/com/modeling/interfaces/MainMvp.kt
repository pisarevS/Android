package pisarev.com.modeling.interfaces

interface MainMvp {
    interface ViewMvp {
        fun showProgram(program: String?)
    }

    interface PresenterMainMvp {
        fun openProgram(path: String?)
        fun saveAll(program: String?, path: String?)
    }
}