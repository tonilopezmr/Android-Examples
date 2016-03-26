package com.tonilopezmr.dagger2rxjava.presenter;

import com.tonilopezmr.dagger2rxjava.domain.Person;
import com.tonilopezmr.dagger2rxjava.domain.usecase.GetAllPersonsUseCase;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @author Antonio López.
 */
public class PersonListPresenter implements Presenter<PersonListPresenter.View> {

    private final GetAllPersonsUseCase personsUseCase;
    private  PersonListPresenter.View view;

    private Subscription personsSubscription;

    @Inject
    public PersonListPresenter(GetAllPersonsUseCase personsUseCase) {
        this.personsUseCase = personsUseCase;
    }

    @Override
    public void init() {
        askForPersons();
    }

    private void askForPersons() {
        personsSubscription = personsUseCase.execute()
                .subscribe(this::onPersonReceived, this::showError);
    }

    private void onPersonReceived(Person person) {
        view.showPersons(Arrays.asList(person));
        view.hideLoading();
        view.hideEmptyView();
    }

    private void showError(Throwable error){
        view.hideLoading();
        view.showError();
    }

    @Override
    public void setView(View view) {
        this.view = view;
    }

    @Override
    public void onDestroy() {
        personsSubscription.unsubscribe();
    }

    public interface View extends Presenter.View{
        void showEmptyView();
        void hideEmptyView();
        void showPersons(List<Person> persons);
        void showError();
    }
}

