package com.tonilopezmr.dagger2rxjava.domain.usecase;

import com.tonilopezmr.dagger2rxjava.domain.Person;
import com.tonilopezmr.dagger2rxjava.domain.PersonRepositoryImp;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.Scheduler;

/**
 * @author Antonio López.
 */
public class GetAllPersonsUseCase extends UseCase<Person>{
    private final PersonRepositoryImp repository;

    private final Scheduler uiThread;
    private final Scheduler executorThread;

    @Inject
    public GetAllPersonsUseCase(PersonRepositoryImp repository, @Named("ui_thread") Scheduler uiThread, @Named("executor_thread") Scheduler executorThread) {
        this.repository = repository;
        this.uiThread = uiThread;
        this.executorThread = executorThread;
    }

    @Override
    protected Observable<Person> buildUseCaseObservable() {
        return repository.getAll()
                .observeOn(uiThread)
                .subscribeOn(executorThread);
    }
}
