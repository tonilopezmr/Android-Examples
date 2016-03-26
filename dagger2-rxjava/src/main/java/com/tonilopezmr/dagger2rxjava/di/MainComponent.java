package com.tonilopezmr.dagger2rxjava.di;

import com.tonilopezmr.dagger2rxjava.view.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Antonio López.
 */
@Singleton
@Component(modules = MainModule.class) public interface MainComponent {

    void inject(MainActivity activity);
}
