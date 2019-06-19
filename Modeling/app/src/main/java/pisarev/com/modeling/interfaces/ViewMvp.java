package pisarev.com.modeling.interfaces;

public interface ViewMvp {

    interface MainViewMvp{
        void showProgram(String program);
        void showParameter(String parameter);
    }
    interface PresenterMainMvp{
        void openProgram(String path);
        void openParameter(String path);
    }
    interface MyViewMvp {
        void showError(String error);
    }
}
