package com.tonilopezmr.dagger2.domain;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio López.
 */
public class PersonRepository {

    private final List<Person> persons;

    public PersonRepository() {
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

    public List<Person> getAll(){
        return persons;
    }

    public Person get(Person person){
        return persons.get(persons.indexOf(person));
    }
}
