/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Test.Service;


import com.Test.DAO.PersonDAO;
import com.Test.Model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author 91976
 */
@Service
public class PersonService {

    @Autowired
    private PersonDAO personDAO;

    public List<Person> getAllPersons() {
        return personDAO.findAll();
    }

    public Person getPersonById(Integer id) {
        return personDAO.findById(id).orElse(null);
    }

    public Person createPerson(Person person) {
        return personDAO.save(person);
    }

    public Person updatePerson(Integer id, Person person) {
        return personDAO.findById(id)
                .map(existingPerson -> {
                    existingPerson.setName(person.getName());
                    existingPerson.setLastName(person.getLastName());
                    return personDAO.save(existingPerson);
                })
                .orElseThrow(null);
    }

    public void deletePerson(Integer id) {
        personDAO.deleteById(id);
    }
    
}
