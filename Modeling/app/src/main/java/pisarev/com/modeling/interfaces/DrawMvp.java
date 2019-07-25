package pisarev.com.modeling.interfaces;

public interface DrawMvp {

    interface PresenterDrawViewMvp {
        void onButtonStart();
        void onButtonCycleStart();
        void onButtonSingleBlock(boolean isSingleBlockDown);
        void onButtonReset();
        void getActivity(DrawViewMvp secondView);

    }

    interface DrawViewMvp {
        void showFrame(String frame);
        void showAxis(String horizontalAxis, String verticalAxis);
    }
}
