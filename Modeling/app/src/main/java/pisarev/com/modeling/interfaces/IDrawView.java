package pisarev.com.modeling.interfaces;


public interface IDrawView {
    void onButtonStart( boolean isStartDown);
    void onButtonCycleStart();
    void onButtonSingleBlock(boolean isSingleBlockDown);
    void onButtonReset( boolean isResetDown);
    void getActivity(ISecondView secondView);

}
