package pisarev.com.modeling.interfaces

interface ISQLiteData {
    var programText: Map<String?, String?>?
    fun deleteProgramText()
}