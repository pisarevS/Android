package pisarev.com.modeling.mvp.model

import android.annotation.TargetApi
import android.os.Build
import android.widget.EditText
import com.github.akshay_naik.texthighlighterapi.TextHighlighter
import android.text.Html
import pisarev.com.modeling.mvp.model.StyleText
import android.annotation.SuppressLint
import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.Spannable
import java.util.*
import java.util.regex.Pattern

@TargetApi(Build.VERSION_CODES.O)
object StyleText {
    fun setStyle(editText: EditText) {
        val highlighter = TextHighlighter()
        highlighter.setLanguage("JAVA")
        val highlightedText = highlighter.getHighlightedText(editText.text.toString())
        editText.setText(Html.fromHtml(highlightedText))
        /*
        editText.setText(computeHighlighting(editText.getText().toString()));
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
    }

    val KEYWORDS = Arrays.asList(
        "BORE_DIAM", "WHEEL_UNMACHINED", "WHEEL_MACHINED", "SYM_FACTOR", "TREAD_HEIGHT_S1",
        "TREAD_HEIGHT_S2", "GLOBAL_ALLOWANCE", "TREAD_ALLOWANCE", "TREAD_DIAM", "VYLET_ST",
        "STUPICA_VNUT", "STUPICA_NAR", "DISK_VNUT", "DISK_NAR",
        "YABLOKO_VNUT", "YABLOKO_NAR", "WHEEL_HEIGHT", "N_GANTRYPOS_X", "N_GANTRYPOS_Z",
        "N_WHEEL_UNMACHINED", "N_WHEEL_MACHINED", "N_SYM_FACTOR"
    )
    private val AXIS = arrayOf(
        "X", "Z", "U", "W", "CR", "F"
    )
    private val KEYWORD_PATTERN = "\\b(" + java.lang.String.join("|", KEYWORDS) + ")\\b"
    private const val R_PARAMETER = "R(\\d+)"
    private val AXIS_PATTERN = "\\b(" + java.lang.String.join("|", *AXIS) + ")\\b"
    private const val GCODE_PATTERN = "G(\\d+)"
    private const val FIGURES_PATTERN = "(?:[^\\w_]|^|\\b)(\\d+)"
    private const val PAREN_PATTERN = "\\(|\\)"
    private const val BRACE_PATTERN = "\\{|\\}"
    private const val NUMBER_FRAME_PATTERN = "N(\\d+)"
    private const val SEMICOLON_PATTERN = "\\+|\\-|\\*|\\/|\\="
    private const val STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\""
    private const val COMMENT_PATTERN = ";[^\n]*"
    private val PATTERN = Pattern.compile(
        "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                + "|(?<RPARAMETER>" + R_PARAMETER + ")"
                + "|(?<PAREN>" + PAREN_PATTERN + ")"
                + "|(?<BRACE>" + BRACE_PATTERN + ")"
                + "|(?<NUMBERFRAME>" + NUMBER_FRAME_PATTERN + ")"
                + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                + "|(?<STRING>" + STRING_PATTERN + ")"
                + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
                + "|(?<AXIS>" + AXIS_PATTERN + ")"
                + "|(?<GCODE>" + GCODE_PATTERN + ")"
                + "|(?<FIGURES>" + FIGURES_PATTERN + ")"
    )

    @TargetApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    private fun computeHighlighting(text: String): SpannableStringBuilder {
        val matcher = PATTERN.matcher(text)
        val spannableStringBuilder = SpannableStringBuilder(text)
        while (matcher.find()) {
            if (matcher.group("KEYWORD") != null) {
                spannableStringBuilder.setSpan(
                    ForegroundColorSpan(Color.rgb(0, 250, 250)),
                    matcher.start(),
                    matcher.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            if (matcher.group("RPARAMETER") != null) {
                spannableStringBuilder.setSpan(
                    ForegroundColorSpan(Color.rgb(0, 250, 250)),
                    matcher.start(),
                    matcher.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            if (matcher.group("PAREN") != null) {
                spannableStringBuilder.setSpan(
                    ForegroundColorSpan(Color.rgb(223, 86, 36)),
                    matcher.start(),
                    matcher.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            if (matcher.group("BRACE") != null) {
                spannableStringBuilder.setSpan(
                    ForegroundColorSpan(Color.rgb(0, 128, 128)),
                    matcher.start(),
                    matcher.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            if (matcher.group("NUMBERFRAME") != null) {
                spannableStringBuilder.setSpan(
                    ForegroundColorSpan(Color.rgb(153, 153, 255)),
                    matcher.start(),
                    matcher.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            if (matcher.group("SEMICOLON") != null) {
                spannableStringBuilder.setSpan(
                    ForegroundColorSpan(Color.rgb(255, 255, 255)),
                    matcher.start(),
                    matcher.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            if (matcher.group("STRING") != null) {
                spannableStringBuilder.setSpan(
                    ForegroundColorSpan(Color.rgb(0, 153, 76)),
                    matcher.start(),
                    matcher.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            if (matcher.group("COMMENT") != null) {
                spannableStringBuilder.setSpan(
                    ForegroundColorSpan(Color.GRAY),
                    matcher.start(),
                    matcher.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            if (matcher.group("AXIS") != null) {
                spannableStringBuilder.setSpan(
                    ForegroundColorSpan(Color.rgb(255, 102, 102)),
                    matcher.start(),
                    matcher.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            if (matcher.group("GCODE") != null) {
                spannableStringBuilder.setSpan(
                    ForegroundColorSpan(Color.rgb(153, 218, 140)),
                    matcher.start(),
                    matcher.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            if (matcher.group("FIGURES") != null) {
                spannableStringBuilder.setSpan(
                    ForegroundColorSpan(Color.rgb(255, 204, 51)),
                    matcher.start(),
                    matcher.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        return spannableStringBuilder
    }
}