package pisarev.com.modeling.mvp.presenter;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;;

import pisarev.com.modeling.Const;
import pisarev.com.modeling.mvp.ViewMvp;
import pisarev.com.modeling.mvp.model.Draw;

public class SecondPresenterImpl implements ViewMvp.SecondPresenterMvp {

    private ViewMvp.SecondViewMvp secondViewMvp;


    public SecondPresenterImpl(ViewMvp.SecondViewMvp secondViewMvp){
        this.secondViewMvp=secondViewMvp;
    }

    @Override
    public void onCreate(String program,String parameter) {


    }

    @Override
    public void start() {

    }

    @Override
    public void singleBlock() {

    }

    @Override
    public void reset() {

    }


}
