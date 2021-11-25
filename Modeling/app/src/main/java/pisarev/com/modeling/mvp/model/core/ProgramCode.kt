package pisarev.com.modeling.mvp.model.core

import android.annotation.TargetApi
import android.os.Build
import android.support.annotation.RequiresApi
import pisarev.com.modeling.interfaces.Callback
import pisarev.com.modeling.mvp.model.MyFile
import java.io.File
import java.lang.Exception
import java.lang.StringBuilder
import java.util.*
import java.util.function.Consumer
import java.util.regex.Pattern
import java.util.stream.Collectors
import kotlin.Throws

class ProgramCode(private val path: String ,private val program: String, private val callback: Callback)  : GCode(), Runnable {
    private val defs = arrayOf(DEF_REAL, DEF_INT)
    private var data: MyData = MyData()
    private var horizontalAxis: String = "X"
    private var verticalAxis: String = "Z"
    private var variablesList: MutableMap<String, String> = LinkedHashMap()
    @TargetApi(Build.VERSION_CODES.N)
    override fun run() {
        val listParameterVariables: List<StringBuilder> = MyFile.getParameter(File(path))
        val listParametr: Map<String, String> = readParameterVariables(listParameterVariables)
        replaceParameterVariables(listParametr as MutableMap<String, String>)
        data.programList =
            Arrays.stream(program.split("\n".toRegex()).toTypedArray()).map { str: String? ->  StringBuilder(str!!) }
                .collect(Collectors.toList())
        val programList = Arrays.stream(program.split("\n".toRegex()).toTypedArray())
            .map { str: String? -> StringBuilder(str!!) }
            .peek { frame: StringBuilder? -> removeLockedFrame(frame!!) }
            .peek { frame: StringBuilder? -> removeIgnore(frame!!) }
            .peek { frame: StringBuilder? -> readDefVariables(frame!!) }
            .peek { frame: StringBuilder? -> readRVariables(frame!!) }
            .peek { frame: StringBuilder? -> initVariables(frame!!) }
            .peek { frame: StringBuilder? -> replaceProgramVariables(frame!!) }
            .collect(Collectors.toList())
        gotoF(programList)
        callback.callingBack(addFrameList(programList))
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun addFrameList(programList: List<StringBuilder>): MyData? {
        var error: StringBuilder = StringBuilder()
        val frameList = ArrayList<Frame>()
        val errorListMap: MutableMap<Int, String> = HashMap()
        var tempHorizontal = N_GANTRYPOS_X
        var tempVertical = N_GANTRYPOS_Z
        var tempCR = 0.0
        var tempRND = 0.0
        var tempOFFN = 0.0
        var isVerticalAxis = false
        var isHorizontalAxis = false
        var isCR = false
        var isRND = false
        var isOFFN = false
        var isRadius = false
        var isDiamon = false
        var strFrame: StringBuilder
        selectCoordinateSystem(programList)
        for (i in programList.indices) {
            strFrame = programList[i]
            val frame = Frame()
            try {
                if (containsGCode(strFrame)) {
                    val gCodes: List<String> = searchGCog(strFrame.toString())
                    isRadius = activatedRadius(gCodes)
                    frame.gCodes = gCodes
                    frame.id = i
                    frame.x = tempHorizontal
                    frame.z = tempVertical
                    frameList.add(frame)
                }
            } catch (e: Exception) {
                errorListMap[i] = strFrame.toString()
            }
            try {
                if ( DIAMON in strFrame || DIAMOF in strFrame) {
                    if ( DIAMON in strFrame) {
                        isDiamon = true
                    }
                    if ( DIAMOF in strFrame) {
                        isDiamon = false
                    }
                }
            } catch (e: Exception) {
                errorListMap[i] = strFrame.toString()
            }
            try {
                if ( OFFN in strFrame) {
                    coordinateSearch(
                        frame = strFrame,
                        axis = OFFN
                    )?.also { it ->
                        error = it
                        Expression.calculate(input = it).also {
                            tempOFFN = it
                            isOFFN = true
                        }
                    }
                }
            } catch (e: Exception) {
                errorListMap[i] = "$strFrame \n \"$OFFN=$error\""
            }
            try {
                if ( HOME in strFrame) {
                    isVerticalAxis = true
                    isHorizontalAxis = true
                    frame.isHome = true
                    tempHorizontal = N_GANTRYPOS_X
                    tempVertical = N_GANTRYPOS_Z
                }
            } catch (e: Exception) {
                errorListMap[i] = strFrame.toString()
            }
            try {
                if (horizontalAxis + IC in strFrame) {
                    tempHorizontal += incrementSearch(strFrame, horizontalAxis + IC)
                    isHorizontalAxis = true
                } else if (containsAxis(strFrame, horizontalAxis)) {
                    coordinateSearch(
                        frame = strFrame,
                        axis = horizontalAxis
                    )?.also {
                        error = it
                        Expression.calculate(input = it).also {
                            tempHorizontal = it
                            isHorizontalAxis = true
                        }
                    }
                }
            } catch (e: Exception) {
                errorListMap[i] = "$strFrame \n \"$horizontalAxis=$error\""
            }
            try {
                if (verticalAxis + IC in strFrame) {
                    tempVertical += incrementSearch(strFrame, verticalAxis + IC)
                    isVerticalAxis = true
                } else if (containsAxis(strFrame, verticalAxis)) {
                    coordinateSearch(
                        frame = strFrame,
                        axis = verticalAxis
                    )?.also {
                        error = it
                        Expression.calculate(input = it).also {
                            tempVertical = it
                            isVerticalAxis = true
                        }
                    }
                }
            } catch (e: Exception) {
                errorListMap[i] = "$strFrame \n \"$verticalAxis=$error\""
            }
            try {
                if ( CR in strFrame && isRadius) {
                    coordinateSearch(
                        frame = strFrame,
                        axis = CR
                    )?.also {
                        error = it
                        Expression.calculate(input = it).also {
                            tempCR = it
                            isCR = true
                        }
                    }
                }
            } catch (e: Exception) {
                errorListMap[i] = "$strFrame \n \"$CR=$error\""
            }
            try {
                if (RND in strFrame && isHorizontalAxis &&  CR !in strFrame || RND in strFrame && isVerticalAxis && RND !in strFrame) {
                    coordinateSearch(
                        frame = strFrame,
                        axis = RND
                    )?.also {
                        error = it
                        Expression.calculate(input = it).also {
                            tempRND = it
                            isRND = true
                        }
                    }
                }
            } catch (e: Exception) {
                errorListMap[i] = "$strFrame \n \"$RND=$error\""
            }
            if (containsTool(strFrame)) {
                frame.diamon = isDiamon
                frame.tool = readTool(strFrame)
                frame.isTool = true
                isVerticalAxis = true
                isHorizontalAxis = true
                tempHorizontal = N_GANTRYPOS_X
                tempVertical = N_GANTRYPOS_Z
            }
            if (isCR) {
                frame.id = i
                frame.diamon = isDiamon
                frame.x = tempHorizontal
                frame.z = tempVertical
                frame.offn = tempOFFN
                frame.isOffn = isOFFN
                frame.cr = tempCR
                frame.isCR = isCR
                frame.isAxisContains = true
                frameList.add(frame)
                isHorizontalAxis = false
                isVerticalAxis = false
                isCR = false
            }
            if (isRND) {
                frame.id = i
                frame.diamon = isDiamon
                frame.x = tempHorizontal
                frame.z = tempVertical
                frame.offn = tempOFFN
                frame.isOffn = isOFFN
                frame.rnd = tempRND
                frame.isRND = isRND
                frame.isAxisContains = true
                frameList.add(frame)
                isHorizontalAxis = false
                isVerticalAxis = false
                isRND = false
            }
            if (isHorizontalAxis || isVerticalAxis) {
                frame.id = i
                frame.diamon = isDiamon
                frame.x = tempHorizontal
                frame.z = tempVertical
                frame.offn = tempOFFN
                frame.isOffn = isOFFN
                frame.isAxisContains = true
                frameList.add(frame)
                isHorizontalAxis = false
                isVerticalAxis = false
            }
        }
        data.errorListMap = errorListMap
        val s: Set<Frame> = LinkedHashSet(frameList)
        frameList.clear()
        frameList.addAll(s)
        Offn().correctionForOffn(frameList)
        correctionForDiamon(frameList)
        data.frameList = frameList
        return data
    }

    private fun containsTool(frame: StringBuilder): Boolean {
        MyList().listTools.keys.forEach{ tool -> if( tool in frame ) return true }
        return false
    }

    private fun containsAxis(frame: StringBuilder, axis: String): Boolean {
        if (axis in frame) {
            val n = frame.indexOf(axis) + 1
            when (frame[n]) {
                '-', '=', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> return true
            }
        }
        return false
    }

    private fun containsGCode(frame: StringBuilder): Boolean {
        gCodes.forEach { gCode -> if (gCode in frame) return true }
        return false
    }

    private fun isGCode(g: String): Boolean {
        for (gCode in gCodes) {
            if (g == gCode) {
                return true
            }
        }
        return false
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun removeIgnore(frame: StringBuilder) {
        MyList().listIgnoreFrame!!.forEach(Consumer { ignore: String? ->
            if (frame.toString().contains(
                    ignore!!
                )
            ) frame.delete(0, frame.length)
        })
    }

    private fun searchGCog(frame: String): ArrayList<String> {
        val gCodeList = ArrayList<String>()
        var g = StringBuilder("G")
        if (frame.contains("G")) {
            for (i in 0 until frame.length) {
                val c = frame[i]
                if (c == 'G') {
                    for (j in i + 1 until frame.length) {
                        val t = frame[j]
                        if (Character.isDigit(t)) {
                            g.append(t)
                        } else {
                            if (isGCode(g.toString())) gCodeList.add(g.toString())
                            break
                        }
                    }
                    g = StringBuilder("G")
                }
            }
        }
        return gCodeList
    }

    private fun activatedRadius(gCode: List<String?>): Boolean {
        for (code in gCode) {
            when (code) {
                G2, G02, G3, G03 -> return true
                G01, G1, G0, G00 -> return false
            }
        }
        return false
    }

    private fun gotoF(programList: List<StringBuilder>) {
        var label: String
        for (i in programList.indices) {
            if (GOTOF in programList[i].toString()) {
                label = programList[i].substring(programList[i].indexOf(GOTOF) + GOTOF.length, programList[i].length)
                    .replace(" ", "")
                for (j in i + 1 until programList.size) {
                    if ("$label:" !in programList[j].toString()) {
                        programList[j].delete(0, programList[j].length)
                    } else {
                        break
                    }
                }
            }
        }
    }

    private fun correctionForDiamon(frameList: List<Frame>?) {
        for (frame in frameList!!) {
            if (frame.diamon && frame.isAxisContains) {
                if (frame.tool == null && !frame.isHome) frame.x = frame.x / 2
            }
        }
    }

    private fun readTool(frame: StringBuilder): String {
        MyList().listTools.keys.forEach { tool -> if ( tool in frame ) return tool }
        return ""
    }

    private fun removeLockedFrame(frame: StringBuilder) {
        if (frame.toString().contains(";")) frame.delete(frame.indexOf(";"), frame.length)
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun selectCoordinateSystem(programList: List<StringBuilder>) {
        var x = 0
        var u = 0
        programList.forEach(Consumer { valve: StringBuilder ->
            if ("X" in valve.toString()) x++
            if ("U" in valve.toString()) u++
        })
        if (x > u) {
            horizontalAxis = "X"
            verticalAxis = "Z"
        } else if(u > x) {
            horizontalAxis = "U"
            verticalAxis = "W"
        }
    }

    @Throws(Exception::class)
    private fun incrementSearch(frame: StringBuilder, axis: String): Double {
        val temp = StringBuilder()
        val n = frame.indexOf(axis)
        if (frame[n + axis.length] == '(') {
            for (i in n + axis.length until frame.length) {
                if (!Character.isLetter(frame[i])) {
                    temp.append(frame[i])
                } else break
            }
            return Expression.calculate(temp)
        }
        return temp.toString().toDouble()
    }

    @Throws(Exception::class)
    private fun coordinateSearch(frame: StringBuilder, axis: String): StringBuilder? {
        val tempFrame = StringBuilder()
        (frame.indexOf(axis) + axis.length until frame.length).forEach { i ->
            if (!Character.isLetter(frame[i])) {
                tempFrame.append(frame[i])
            } else return tempFrame
        }
        return tempFrame
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun readParameterVariables(parameterList: List<StringBuilder>): Map<String, String> {
        variablesList.clear()
        parameterList.forEach(Consumer { p: StringBuilder ->
            if (";" in p.toString()) p.delete(p.indexOf(";"), p.length)
            if ("=" in p.toString()) {
                var key = 0
                for (j in p.indexOf("=") - 1 downTo 0) {
                    val c = p[j]
                    if (c == ' ') {
                        key = j
                        break
                    }
                }
                variablesList[p.substring(key, p.indexOf("=")).replace(" ", "")] =
                    p.substring(p.indexOf("=") + 1, p.length).replace(" ", "")
            }
        })
        variablesList["N_GANTRYPOS_X"] = N_GANTRYPOS_X.toString()
        variablesList["N_GANTRYPOS_Z"] = N_GANTRYPOS_Z.toString()
        variablesList["N_GANTRYPOS_U"] = N_GANTRYPOS_X.toString()
        variablesList["N_GANTRYPOS_W"] = N_GANTRYPOS_Z.toString()
        variablesList["\$P_TOOLR"] = "16"
        return variablesList
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun replaceParameterVariables(variablesList: MutableMap<String, String>) {
        variablesList.forEach { (key: String, value1: String) ->
            var value = value1
            for (keys in variablesList.keys) {
                if (value.contains(keys)) {
                    value = value.replace(keys, Objects.requireNonNull(variablesList[keys])!!)
                    variablesList[key] = value
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun replaceProgramVariables(frame: StringBuilder) {
        variablesList.forEach { (key: String?, value: String?) ->
            if (key in frame.toString()) {
                var value1 = value
                if (isContainsOperator(value1)) {
                    var newValve = 0.0
                    try {
                        newValve = Expression.calculate(StringBuilder(value1)).toDouble()
                    } catch (e: EmptyStackException) {
                        e.printStackTrace()
                    }
                    value1 = newValve.toString()
                }
                val str = frame.toString().replace(key, value1)
                frame.replace(0, frame.length, str)
            }
        }
    }

    private fun readDefVariables(frame: StringBuilder) {
        defs.forEach { def ->
            if (def in frame.toString()) {
                frame.delete(0, frame.indexOf(def) + def.length)
                val arrStr = frame.toString().split(",".toRegex()).toTypedArray()
                for (str in arrStr) {
                    if ("=" in str) {
                        val arrVar = str.split("=".toRegex()).toTypedArray()
                        variablesList[arrVar[0].replace(" ", "")] = arrVar[1].replace(" ", "")
                    } else {
                        variablesList[str.replace(" ", "")] = ""
                    }
                }
            }
        }
    }

    private fun readRVariables(frame: StringBuilder) {
        val pattern = Pattern.compile("R(\\d+)" + "=")
        val matcher = pattern.matcher(frame)
        while (matcher.find()) {
            variablesList[matcher.group().replace("=", "")] = ""
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun initVariables(frame: StringBuilder) {
        for (def in defs) {
            if (frame.indexOf(def) == -1) {
                variablesList.forEach { (key: String, value: String?) ->
                    if (frame.toString().contains("$key=")) {
                        val arrStr = frame.toString().split(" ").toTypedArray()
                        for (str in arrStr) {
                            if (str.contains("=")) {
                                val arrVar = str.split("=").toTypedArray()
                                variablesList[arrVar[0].replace(" ", "")] = arrVar[1].replace(" ", "")
                            }
                        }
                    }
                }
            }
        }
        replaceParameterVariables(variablesList)
    }
}