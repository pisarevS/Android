package pisarev.com.modeling.interfaces;

import pisarev.com.modeling.mvp.model.MyData;

public interface DrawMvp {

    interface PresenterDrawViewMvp {
        void onButtonStart();

        void onButtonCycleStart();

        void onButtonSingleBlock(boolean isSingleBlockDown);

        void onButtonReset();

        void setActivity(DrawViewMvp secondView);

        void setIndex(int index);

        void setData(MyData data);
    }

    interface DrawViewMvp {
        void showFrame(String frame);

        void showAxis(String horizontalAxis, String verticalAxis);

        void showIndex(int index);
    }
}
