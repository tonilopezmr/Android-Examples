package com.tonilopezmr.dagger2rxjava.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import rx.Observable;

/**
 * @author Antonio López.
 */
public class PersonRepositoryImp implements PersonRepository{

    private final List<Person> persons;

    public PersonRepositoryImp() {
        this.persons = new LinkedList<>();
        fillRepo();
    }

    private void fillRepo() {
        persons.add(new Person("Conrado Mateu"));
        persons.add(new Person("Marta"));
        persons.add(new Person("Ana"));
        persons.add(new Person("Angel"));
        persons.add(new Person("Jose"));
        persons.add(new Person("Pedro"));
        persons.add(new Person("Maria"));
        persons.add(new Person("Conrado Mateu"));
        persons.add(new Person("Marta"));
        persons.add(new Person("Ana"));
        persons.add(new Person("Angel"));
        persons.add(new Person("Jose"));
        persons.add(new Person("Pedro"));
        persons.add(new Person("Maria"));
    }

    public Observable<Person> getAll(){
        return Observable.create(subscriber -> {
            for (Person person : persons) {
                fakeWait();
                if (new Random().nextInt(5) != 3) subscriber.onNext(person);
                else subscriber.onError(new Exception());
            }
            subscriber.onCompleted();
        });
    }

    private void fakeWait() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Observable<Person> get(Person person){
        return Observable.create(subscriber -> {
           fakeWait();
            subscriber.onNext(persons.get(persons.indexOf(person)));
            subscriber.onCompleted();
        });
    }
}
