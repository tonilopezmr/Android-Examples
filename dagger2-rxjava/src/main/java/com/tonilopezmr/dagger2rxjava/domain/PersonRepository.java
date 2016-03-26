package com.tonilopezmr.dagger2rxjava.domain;

import rx.Observable;

/**
 * @author Antonio López.
 */
public interface PersonRepository {

    Observable<Person> getAll();
    Observable<Person> get(Person person);
}
