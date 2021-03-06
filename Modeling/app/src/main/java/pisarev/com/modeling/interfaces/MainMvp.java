package pisarev.com.modeling.interfaces;

public interface MainMvp {

    interface ViewMvp {
        void showProgram(String program);
    }

    interface PresenterMainMvp {
        void openProgram(String path);

        void saveAll(String program, String path);
    }
}
