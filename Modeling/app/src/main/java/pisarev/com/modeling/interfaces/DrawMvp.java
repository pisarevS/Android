package pisarev.com.modeling.interfaces;

public interface DrawMvp {

    interface PresenterDrawViewMvp {
        void onButtonStart();

        void onButtonCycleStart();

        void onButtonSingleBlock(boolean isSingleBlockDown);

        void onButtonReset();

        void getActivity(DrawViewMvp secondView);

        void getIndex(int index);
    }

    interface DrawViewMvp {
        void showFrame(String frame);

        void showAxis(String horizontalAxis, String verticalAxis);

        void showIndex(int index);
    }
}
