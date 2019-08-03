package pisarev.com.modeling.interfaces;

public interface ISQLiteData {
    void setProgramText(String text);
    void setParameterText(String text);
    String getProgramText();
    String getParameterText();
}
