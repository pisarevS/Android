package pisarev.com.modeling.interfaces;

import java.util.Map;

public interface ISQLiteData {
    void setProgramText(Map<String,String> programs);
    Map<String,String> getProgramText();
    void deleteProgramText();
}
