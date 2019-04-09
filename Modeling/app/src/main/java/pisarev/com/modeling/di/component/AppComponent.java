package pisarev.com.modeling.di.component;

import javax.inject.Singleton;

import dagger.Component;
import pisarev.com.modeling.activity.MainActivity;
import pisarev.com.modeling.activity.SecondActivity;
import pisarev.com.modeling.di.module.AppModule;
import pisarev.com.modeling.mvp.model.ChangeVariables;
import pisarev.com.modeling.mvp.model.Draw;
import pisarev.com.modeling.mvp.presenter.PresenterMainImpl;
import pisarev.com.modeling.mvp.view.fragments.ParameterFragment;
import pisarev.com.modeling.mvp.view.fragments.ProgramFragment;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(PresenterMainImpl presenterMain);
    void inject(ProgramFragment fragment);
    void inject(ParameterFragment fragment);
    void inject(Draw draw);
    void inject(MainActivity mainActivity);
    void inject(SecondActivity secondActivity);
    void inject(ChangeVariables changeVariables);

}
