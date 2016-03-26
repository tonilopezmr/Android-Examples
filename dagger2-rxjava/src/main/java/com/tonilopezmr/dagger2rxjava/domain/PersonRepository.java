package com.tonilopezmr.dagger2rxjava.domain;

import java.util.List;

import rx.Observable;

/**
 * @author Antonio López.
 */
public interface PersonRepository {

    Observable<List<Person>> getAll();
    Observable<Person> get(Person person);
}
