package pisarev.com.modeling.mvp;

import android.graphics.Canvas;

import java.io.IOException;
import java.util.ArrayList;

public interface ViewMvp {

    interface MainViewMvp{
        void showError(IOException e);
        void showProgram(String program);
        void showParameter(String parameter);
    }

    interface FragmentMvp{
        void showProgram();
        void showParameter();
        void showError();
    }
    interface PresenterMainMvp{
        void openProgram(String path);
        void openParameter(String path);
    }
    interface SecondViewMvp{

    }
    interface SecondPresenterMvp{
        void onCreate(String program,String parameter);
        void start();
        void singleBlock();
        void reset();
    }
}
