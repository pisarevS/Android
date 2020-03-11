package pisarev.com.modeling.di.component;

import javax.inject.Singleton;

import dagger.Component;
import pisarev.com.modeling.activity.DrawActivity;
import pisarev.com.modeling.di.module.AppModule;

import pisarev.com.modeling.mvp.model.Program;
import pisarev.com.modeling.mvp.view.customview.DrawView;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(DrawActivity drawActivity);

    void inject(DrawView drawView);
}
