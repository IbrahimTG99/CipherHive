package com.devsinc.cipherhive.util


import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import kotlinx.serialization.json.Json


object Util {
    //    const val EXTRA_DATASET_NAME = "dataset_name"
//    const val EXTRA_FOR_RESPONSE = "for_response"
//    private const val TAG = "AutofillSample"
//    var sLoggingLevel = LogLevel.Off
//    private fun bundleToString(builder: StringBuilder, data: Bundle) {
//        val keySet = data.keySet()
//        builder.append("[Bundle with ").append(keySet.size).append(" keys:")
//        for (key in keySet) {
//            builder.append(' ').append(key).append('=')
//            val value = data[key]
//            if (value is Bundle) {
//                bundleToString(builder, value)
//            } else {
//                builder.append(if (value is Array<Any>) Arrays.toString(value as Array<Any?>?) else value)
//            }
//        }
//        builder.append(']')
//    }
//
//    fun bundleToString(data: Bundle?): String {
//        if (data == null) {
//            return "N/A"
//        }
//        val builder = StringBuilder()
//        bundleToString(builder, data)
//        return builder.toString()
//    }
//
//    fun getTypeAsString(type: Int): String {
//        when (type) {
//            View.AUTOFILL_TYPE_TEXT -> return "TYPE_TEXT"
//            View.AUTOFILL_TYPE_LIST -> return "TYPE_LIST"
//            View.AUTOFILL_TYPE_NONE -> return "TYPE_NONE"
//            View.AUTOFILL_TYPE_TOGGLE -> return "TYPE_TOGGLE"
//            View.AUTOFILL_TYPE_DATE -> return "TYPE_DATE"
//        }
//        return "UNKNOWN_TYPE"
//    }
//
//    private fun getAutofillValueAndTypeAsString(value: AutofillValue?): String {
//        if (value == null) return "null"
//        val builder = StringBuilder(value.toString()).append('(')
//        if (value.isText) {
//            builder.append("isText")
//        } else if (value.isDate) {
//            builder.append("isDate")
//        } else if (value.isToggle) {
//            builder.append("isToggle")
//        } else if (value.isList) {
//            builder.append("isList")
//        }
//        return builder.append(')').toString()
//    }
//
//    fun dumpStructure(structure: AssistStructure) {
//        if (logVerboseEnabled()) {
//            val nodeCount = structure.windowNodeCount
//            logv(
//                "dumpStructure(): component=%s numberNodes=%d",
//                structure.activityComponent, nodeCount
//            )
//            for (i in 0 until nodeCount) {
//                logv("node #%d", i)
//                val node = structure.getWindowNodeAt(i)
//                dumpNode(StringBuilder(), "  ", node.rootViewNode, 0)
//            }
//        }
//    }
//
//    private fun dumpNode(builder: StringBuilder, prefix: String, node: ViewNode, childNumber: Int) {
//        builder.append(prefix)
//            .append("child #").append(childNumber).append("\n")
//        builder.append(prefix)
//            .append("autoFillId: ").append(node.autofillId)
//            .append("\tidEntry: ").append(node.idEntry)
//            .append("\tid: ").append(node.id)
//            .append("\tclassName: ").append(node.className)
//            .append('\n')
//        builder.append(prefix)
//            .append("focused: ").append(node.isFocused)
//            .append("\tvisibility").append(node.visibility)
//            .append("\tchecked: ").append(node.isChecked)
//            .append("\twebDomain: ").append(node.webDomain)
//            .append("\thint: ").append(node.hint)
//            .append('\n')
//        val htmlInfo = node.htmlInfo
//        if (htmlInfo != null) {
//            builder.append(prefix)
//                .append("HTML TAG: ").append(htmlInfo.tag)
//                .append(" attrs: ").append(htmlInfo.attributes)
//                .append('\n')
//        }
//        val afHints = node.autofillHints
//        val options = node.autofillOptions
//        builder.append(prefix).append("afType: ").append(getTypeAsString(node.autofillType))
//            .append("\tafValue:")
//            .append(getAutofillValueAndTypeAsString(node.autofillValue))
//            .append("\tafOptions:").append(if (options == null) "N/A" else Arrays.toString(options))
//            .append("\tafHints: ").append(if (afHints == null) "N/A" else Arrays.toString(afHints))
//            .append("\tinputType:").append(node.inputType)
//            .append('\n')
//        val numberChildren = node.childCount
//        builder.append(prefix).append("# children: ").append(numberChildren)
//            .append("\ttext: ").append(node.text)
//            .append('\n')
//        val prefix2 = "$prefix  "
//        for (i in 0 until numberChildren) {
//            dumpNode(builder, prefix2, node.getChildAt(i), i)
//        }
//        logv(builder.toString())
//    }
//
//    fun getSaveTypeAsString(type: Int): String {
//        val types: MutableList<String> = ArrayList()
//        if (type and SaveInfo.SAVE_DATA_TYPE_ADDRESS != 0) {
//            types.add("ADDRESS")
//        }
//        if (type and SaveInfo.SAVE_DATA_TYPE_CREDIT_CARD != 0) {
//            types.add("CREDIT_CARD")
//        }
//        if (type and SaveInfo.SAVE_DATA_TYPE_EMAIL_ADDRESS != 0) {
//            types.add("EMAIL_ADDRESS")
//        }
//        if (type and SaveInfo.SAVE_DATA_TYPE_USERNAME != 0) {
//            types.add("USERNAME")
//        }
//        if (type and SaveInfo.SAVE_DATA_TYPE_PASSWORD != 0) {
//            types.add("PASSWORD")
//        }
//        return if (types.isEmpty()) {
//            "UNKNOWN($type)"
//        } else Joiner.on('|').join(types)
//    }
//
//    /**
//     * Gets a node if it matches the filter criteria for the given id.
//     */
//    fun findNodeByFilter(
//        @NonNull contexts: List<FillContext>, @NonNull id: Any?,
//        @NonNull filter: NodeFilter
//    ): ViewNode? {
//        for (context in contexts) {
//            val node = findNodeByFilter(context.structure, id, filter)
//            if (node != null) {
//                return node
//            }
//        }
//        return null
//    }
//
//    /**
//     * Gets a node if it matches the filter criteria for the given id.
//     */
//    fun findNodeByFilter(
//        @NonNull structure: AssistStructure, @NonNull id: Any?,
//        @NonNull filter: NodeFilter
//    ): ViewNode? {
//        logv("Parsing request for activity %s", structure.activityComponent)
//        val nodes = structure.windowNodeCount
//        for (i in 0 until nodes) {
//            val windowNode = structure.getWindowNodeAt(i)
//            val rootNode = windowNode.rootViewNode
//            val node = findNodeByFilter(rootNode, id, filter)
//            if (node != null) {
//                return node
//            }
//        }
//        return null
//    }
//
//    /**
//     * Gets a node if it matches the filter criteria for the given id.
//     */
//    fun findNodeByFilter(
//        @NonNull node: ViewNode, @NonNull id: Any?,
//        @NonNull filter: NodeFilter
//    ): ViewNode? {
//        if (filter.matches(node, id)) {
//            return node
//        }
//        val childrenSize = node.childCount
//        if (childrenSize > 0) {
//            for (i in 0 until childrenSize) {
//                val found = findNodeByFilter(node.getChildAt(i), id, filter)
//                if (found != null) {
//                    return found
//                }
//            }
//        }
//        return null
//    }
//
//    fun logd(message: String?, vararg params: Any?) {
//        if (logDebugEnabled()) {
//            Log.d(TAG, String.format(message!!, *params))
//        }
//    }
//
//    fun logv(message: String?, vararg params: Any?) {
//        if (logVerboseEnabled()) {
//            Log.v(TAG, String.format(message!!, *params))
//        }
//    }
//
//    fun logDebugEnabled(): Boolean {
//        return sLoggingLevel.ordinal >= LogLevel.Debug.ordinal
//    }
//
//    fun logVerboseEnabled(): Boolean {
//        return sLoggingLevel.ordinal >= LogLevel.Verbose.ordinal
//    }
//
//    fun logw(message: String?, vararg params: Any?) {
//        Log.w(TAG, String.format(message!!, *params))
//    }
//
//    fun logw(throwable: Throwable?, message: String?, vararg params: Any?) {
//        Log.w(TAG, String.format(message!!, *params), throwable)
//    }
//
//    fun loge(message: String?, vararg params: Any?) {
//        Log.e(TAG, String.format(message!!, *params))
//    }
//
//    fun loge(throwable: Throwable?, message: String?, vararg params: Any?) {
//        Log.e(TAG, String.format(message!!, *params), throwable)
//    }
//
//    fun setLoggingLevel(level: LogLevel) {
//        sLoggingLevel = level
//    }
//
//    /**
//     * Helper method for getting the index of a CharSequence object in an array.
//     */
//    fun indexOf(@NonNull array: Array<CharSequence>, charSequence: CharSequence?): Int {
//        var index = -1
//        if (charSequence == null) {
//            return index
//        }
//        for (i in array.indices) {
//            if (charSequence == array[i]) {
//                index = i
//                break
//            }
//        }
//        return index
//    }
//
//    enum class LogLevel {
//        Off, Debug, Verbose
//    }
//
//    enum class DalCheckRequirement {
//        Disabled, LoginOnly, AllUrls
//    }
//
    fun Bitmap.toRoundedCorners(cornerRadius: Float = 32F): Bitmap? {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        Canvas(bitmap).apply {
            clipPath(Path().apply {
                addRoundRect(
                    RectF(0f, 0f, width.toFloat(), height.toFloat()),
                    cornerRadius,
                    cornerRadius,
                    Path.Direction.CCW
                )
            })
        }.drawBitmap(this, 0f, 0f, null)

        return bitmap
    }

    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
}
