package pisarev.com.modeling.di.component;

import javax.inject.Singleton;

import dagger.Component;
import pisarev.com.modeling.di.module.AppModule;
import pisarev.com.modeling.mvp.model.ChangeVariables;
import pisarev.com.modeling.mvp.model.Draw;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(Draw draw);
    void inject(ChangeVariables changeVariables);

}
