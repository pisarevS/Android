package pisarev.com.modeling.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pisarev.com.modeling.mvp.model.MyData;

@Module
public class AppModule {

    @Singleton
    @Provides
    MyData provideMyData(){
        return new MyData();
    }
}
