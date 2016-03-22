package com.tonilopezmr.dagger2.domain.usecase;

import android.os.Handler;
import android.os.Looper;

import com.tonilopezmr.dagger2.domain.Person;
import com.tonilopezmr.dagger2.domain.PersonRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Antonio López.
 */
public class GetAllPersons {
    private final PersonRepository repository;

    @Inject
    public GetAllPersons(PersonRepository repository) {
        this.repository = repository;
    }

    public void getAll(final Callback callback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Person> persons = repository.getAll();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override public void run() {
                        callback.onPersonsLoader(persons);
                    }
                });
            }
        }).start();
    }

    public interface Callback {
        void onPersonsLoader(List<Person> persons);
    }
}
