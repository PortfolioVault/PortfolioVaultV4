package com.example.testsql.Web;


import com.example.testsql.Model.Person;
import com.example.testsql.Service.PersonService;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;


@Named
@ViewScoped
public class PersonBean implements Serializable {

    private Person person = new Person();

    @Inject
    private PersonService personService;

    public String savePerson() {
        personService.savePerson(person);
        return "success"; // Navigation rule to success page
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
