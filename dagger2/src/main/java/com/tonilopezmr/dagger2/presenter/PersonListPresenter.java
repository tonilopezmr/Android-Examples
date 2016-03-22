package com.tonilopezmr.dagger2.presenter;

import com.tonilopezmr.dagger2.domain.Person;
import com.tonilopezmr.dagger2.domain.usecase.GetAllPersons;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Antonio López.
 */
public class PersonListPresenter implements Presenter<PersonListPresenter.View> {

    private final GetAllPersons personsUseCase;
    private  PersonListPresenter.View view;

    @Inject
    public PersonListPresenter(GetAllPersons personsUseCase) {
        this.personsUseCase = personsUseCase;
    }

    @Override
    public void init() {
        personsUseCase.getAll(new GetAllPersons.Callback() {
            @Override
            public void onPersonsLoader(List<Person> persons) {
                view.showPersons(persons);
                view.hideLoading();
                if (persons.isEmpty()){
                    view.showEmptyView();
                }else{
                    view.hideEmptyView();
                }
            }
        });
    }

    @Override
    public void setView(View view) {
        this.view = view;
    }

    public interface View extends Presenter.View{
        void showEmptyView();
        void hideEmptyView();
        void showPersons(List<Person> persons);
    }
}

