package pisarev.com.modeling.interfaces;

import pisarev.com.modeling.mvp.model.MyData;

public interface DrawMvp {

    interface PresenterDrawViewMvp {
        void onButtonStart();

        void onButtonCycleStart();

        void onButtonSingleBlock(boolean isSingleBlockDown);

        void onButtonReset();

        void getActivity(DrawViewMvp secondView);

        void getData(MyData data);

    }

    interface DrawViewMvp {
        void showFrame(String frame);

        void showAxis(String horizontalAxis, String verticalAxis);
    }
}
