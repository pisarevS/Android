package pisarev.com.modeling.di.component;

import javax.inject.Singleton;

import dagger.Component;
import pisarev.com.modeling.activity.DrawActivity;
import pisarev.com.modeling.activity.MainActivity;
import pisarev.com.modeling.di.module.AppModule;


import pisarev.com.modeling.mvp.model.MyFile;
import pisarev.com.modeling.mvp.model.Program;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(Program program);

    void inject(MyFile myFile);

    void inject(DrawActivity drawActivity);

    void inject(MainActivity mainActivity);
}
