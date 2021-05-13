package pisarev.com.modeling.mvp.model;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;

import android.text.*;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;
import com.github.akshay_naik.texthighlighterapi.TextHighlighter;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@TargetApi(Build.VERSION_CODES.O)
public class StyleText {

    public static void setStyle(EditText editText){
        TextHighlighter highlighter=new TextHighlighter();
        highlighter.setLanguage("JAVA");
        String highlightedText=highlighter.getHighlightedText(editText.getText().toString());

        editText.setText(Html.fromHtml(highlightedText));
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

    public static final List<String> KEYWORDS = Arrays.asList("BORE_DIAM", "WHEEL_UNMACHINED", "WHEEL_MACHINED", "SYM_FACTOR", "TREAD_HEIGHT_S1",
            "TREAD_HEIGHT_S2", "GLOBAL_ALLOWANCE", "TREAD_ALLOWANCE", "TREAD_DIAM", "VYLET_ST",
            "STUPICA_VNUT", "STUPICA_NAR", "DISK_VNUT", "DISK_NAR",
            "YABLOKO_VNUT", "YABLOKO_NAR", "WHEEL_HEIGHT", "N_GANTRYPOS_X", "N_GANTRYPOS_Z",
            "N_WHEEL_UNMACHINED", "N_WHEEL_MACHINED", "N_SYM_FACTOR");


    private static final String[] AXIS = new String[]{
            "X", "Z", "U", "W", "CR", "F"
    };

    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String R_PARAMETER = "R(\\d+)";
    private static final String AXIS_PATTERN = "\\b(" + String.join("|", AXIS) + ")\\b";
    private static final String GCODE_PATTERN = "G(\\d+)";
    private static final String FIGURES_PATTERN = "(?:[^\\w_]|^|\\b)(\\d+)";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String NUMBER_FRAME_PATTERN = "N(\\d+)";
    private static final String SEMICOLON_PATTERN = "\\+|\\-|\\*|\\/|\\=";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_PATTERN = ";[^\n]*";

    private static final Pattern PATTERN = Pattern.compile(
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
    );

    @TargetApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    private static SpannableStringBuilder computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        SpannableStringBuilder spannableStringBuilder=new SpannableStringBuilder(text);

        while (matcher.find()) {
            if( matcher.group("KEYWORD") != null){
                spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.rgb(0,250,250)), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if( matcher.group("RPARAMETER") != null){
                spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.rgb(0,250,250)), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if( matcher.group("PAREN") != null){
                spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.rgb(223,86, 36)), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if( matcher.group("BRACE") != null){
                spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.rgb(0,128,128)), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if( matcher.group("NUMBERFRAME") != null){
                spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.rgb(153, 153, 255)), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if( matcher.group("SEMICOLON") != null){
                spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.rgb(255,255,255)), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if( matcher.group("STRING") != null){
                spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.rgb(0, 153, 76)), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if( matcher.group("COMMENT") != null){
                spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.GRAY), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if( matcher.group("AXIS") != null){
                spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.rgb(255, 102, 102)), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if( matcher.group("GCODE") != null){
                spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.rgb(	153, 218, 140)), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if( matcher.group("FIGURES") != null){
                spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.rgb(	255, 204, 51)), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannableStringBuilder;
    }
}
