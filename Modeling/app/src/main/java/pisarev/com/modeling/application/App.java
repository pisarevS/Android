package pisarev.com.modeling.application;

import android.app.Application;

import pisarev.com.modeling.di.component.AppComponent;
import pisarev.com.modeling.di.component.DaggerAppComponent;
import pisarev.com.modeling.di.module.AppModule;

public class App extends Application {

    private static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component=provideAppComponent();
    }

    public static AppComponent getComponent() {
        return component;
    }

    AppComponent provideAppComponent(){
        return component= DaggerAppComponent
                .builder()
                .appModule( new AppModule() )
                .build();
    }

}
