package pisarev.com.modeling.mvp.presenter;

import pisarev.com.modeling.interfaces.ViewMvp;
import pisarev.com.modeling.mvp.model.File;

public class PresenterMainImpl implements ViewMvp.PresenterMainMvp {

    private ViewMvp.MainViewMvp mainViewMvp;
    private File file;

    public PresenterMainImpl(ViewMvp.MainViewMvp mainViewMvp){
        this.mainViewMvp=mainViewMvp;
        file=new File();
    }

    @Override
    public void openProgram(String path) {
        mainViewMvp.showProgram(file.readFile( path )  );
    }

    @Override
    public void openParameter(String path) {
        mainViewMvp.showParameter(file.readFile(path ) );
    }

}
