package pisarev.com.modeling.mvp.presenter;

import pisarev.com.modeling.interfaces.MainMvp;
import pisarev.com.modeling.mvp.model.File;

public class PresenterMainImpl implements MainMvp.PresenterMainMvp {

    private MainMvp.ViewMvp viewMvp;
    private File file;

    public PresenterMainImpl(MainMvp.ViewMvp viewMvp) {
        this.viewMvp = viewMvp;
        file = new File();
    }

    @Override
    public void openProgram(String path) {
        viewMvp.showProgram( file.readFile( path ) );
    }

    @Override
    public void openParameter(String path) {
        viewMvp.showParameter( file.readFile( path ) );
    }

}
