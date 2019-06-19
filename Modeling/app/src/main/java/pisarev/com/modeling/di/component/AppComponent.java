package pisarev.com.modeling.di.component;

import javax.inject.Singleton;

import dagger.Component;
import pisarev.com.modeling.activity.SecondActivity;
import pisarev.com.modeling.di.module.AppModule;
import pisarev.com.modeling.mvp.model.ChangeVariables;
import pisarev.com.modeling.mvp.model.Draw;

import pisarev.com.modeling.mvp.model.File;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(Draw draw);
    void inject(ChangeVariables changeVariables);
    void inject(File file);
    void inject(SecondActivity secondActivity);
}
