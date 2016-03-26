package com.tonilopezmr.dagger2rxjava.di;

import com.tonilopezmr.dagger2rxjava.domain.PersonRepositoryImp;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Antonio López.
 */
@Module public class MainModule {

    @Provides @Singleton public PersonRepositoryImp providePersonRepository(){
        return new PersonRepositoryImp();
    }

    @Provides @Named("executor_thread") public Scheduler provideExecutorThread(){
        return Schedulers.newThread();
    }

    @Provides @Named("ui_thread") public Scheduler provideMainThread(){
        return AndroidSchedulers.mainThread();
    }
}
