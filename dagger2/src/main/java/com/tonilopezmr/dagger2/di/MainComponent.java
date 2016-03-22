package com.tonilopezmr.dagger2.di;

import com.tonilopezmr.dagger2.view.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Antonio López.
 */
@Singleton
@Component(modules = MainModule.class) public interface MainComponent {

    void inject(MainActivity activity);
}
