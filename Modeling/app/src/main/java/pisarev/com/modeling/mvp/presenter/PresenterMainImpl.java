package pisarev.com.modeling.mvp.presenter;

import android.os.Environment;
import android.util.Log;

import pisarev.com.modeling.interfaces.MainMvp;
import pisarev.com.modeling.mvp.model.Const;
import pisarev.com.modeling.mvp.model.MyFile;

public class PresenterMainImpl implements MainMvp.PresenterMainMvp {

    private MainMvp.ViewMvp viewMvp;
    private MyFile myFile;

    public PresenterMainImpl(MainMvp.ViewMvp viewMvp) {
        this.viewMvp = viewMvp;
        myFile = new MyFile();
    }

    @Override
    public void openProgram(String path) {
        viewMvp.showProgram( myFile.readFile( path ) );
    }

    @Override
    public void openParameter(String path) {
        viewMvp.showParameter( myFile.readFile( path ) );
    }

    @Override
    public void saveAll(String program,String path) {
    }

}
