package com.tonilopezmr.dagger2rxjava.domain.usecase;

import com.tonilopezmr.dagger2rxjava.domain.Person;
import com.tonilopezmr.dagger2rxjava.domain.PersonRepositoryImp;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.Scheduler;

/**
 * @author Antonio López.
 */
public class GetAllPersonsUseCase extends UseCase<List<Person>>{
    private final PersonRepositoryImp repository;

    private final Scheduler uiThread;
    private final Scheduler executorThread;

    @Inject
    public GetAllPersonsUseCase(PersonRepositoryImp repository, @Named("mainThread") Scheduler uiThread, @Named("executorThread") Scheduler executorThread) {
        this.repository = repository;
        this.uiThread = uiThread;
        this.executorThread = executorThread;
    }

    @Override
    protected Observable<List<Person>> buildUseCaseObservable() {
        return repository.getAll()
                .observeOn(uiThread)
                .subscribeOn(executorThread);
    }
}
