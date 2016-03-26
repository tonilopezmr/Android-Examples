package com.tonilopezmr.dagger2rxjava.domain.usecase;


import rx.Observable;

/**
 * @author Antonio López.
 */
public abstract class UseCase<T> {

    public Observable<T> execute(){
        return buildUseCaseObservable();
    }

    protected abstract Observable<T> buildUseCaseObservable();
}
