package com.tonilopezmr.dagger2rxjava.di;

import com.tonilopezmr.dagger2rxjava.domain.PersonRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Antonio López.
 */
@Module public class MainModule {

    @Provides @Singleton public PersonRepository providePersonRepository(){
        return new PersonRepository();
    }

}
