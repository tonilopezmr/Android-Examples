package com.tonilopezmr.dagger2.di;

import com.tonilopezmr.dagger2.domain.PersonRepository;

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
