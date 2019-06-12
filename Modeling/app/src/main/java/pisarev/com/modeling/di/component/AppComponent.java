package pisarev.com.modeling.di.component;

import javax.inject.Singleton;

import dagger.Component;
import pisarev.com.modeling.activity.MainActivity;
import pisarev.com.modeling.activity.SecondActivity;
import pisarev.com.modeling.di.module.AppModule;
import pisarev.com.modeling.mvp.ViewMvp;
import pisarev.com.modeling.mvp.model.ChangeVariables;
import pisarev.com.modeling.mvp.model.Draw;
import pisarev.com.modeling.mvp.view.customview.MyView;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(Draw draw);
    void inject(ChangeVariables changeVariables);
    void inject(MyView myView);
    void inject(SecondActivity secondActivity);

}
