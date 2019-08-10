package pisarev.com.modeling.di.module;

import javax.inject.Singleton;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import pisarev.com.modeling.application.App;
import pisarev.com.modeling.mvp.model.MyData;

@Module
public class AppModule {

    App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Singleton
    @Provides
    MyData provideMyData() {
        return new MyData();
    }


    @Singleton
    @Provides
    Context provideContext() {
        return app.getApplicationContext();
    }


}
