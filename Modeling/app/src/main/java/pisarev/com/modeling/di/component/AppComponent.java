package pisarev.com.modeling.di.component;

import javax.inject.Singleton;

import dagger.Component;
import pisarev.com.modeling.activity.MainActivity;
import pisarev.com.modeling.activity.SecondActivity;
import pisarev.com.modeling.di.module.AppModule;
import pisarev.com.modeling.mvp.model.BaseDraw;
import pisarev.com.modeling.mvp.model.ProgramParameters;
import pisarev.com.modeling.mvp.model.Draw;

import pisarev.com.modeling.mvp.model.File;
import pisarev.com.modeling.mvp.view.customview.DrawView;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(ProgramParameters programParameters);
    void inject(File file);
    void inject(SecondActivity secondActivity);
    void inject(MainActivity mainActivity);
    void inject(DrawView drawView);
    void inject(BaseDraw baseDraw);
    void inject(Draw draw);

}
