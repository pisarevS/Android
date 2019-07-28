package pisarev.com.modeling.interfaces;

public interface MainMvp {

    interface ViewMvp {
        void showProgram(String program);

        void showParameter(String parameter);
    }

    interface PresenterMainMvp {
        void openProgram(String path);

        void openParameter(String path);

        void saveAll(String program,String parameter);
    }
}
