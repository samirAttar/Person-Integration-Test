package com.Test;

import com.Test.DAO.PersonDAO;
import com.Test.Model.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestApplicationTests {


    @LocalServerPort
    private int port;


    private String baseUrl = "http://localhost";

    @Autowired
    private PersonDAO personDAO;

    static RestTemplate restTemplate;

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void setUp() {
        baseUrl = baseUrl.concat(":").concat(port + "").concat("/person");
    }


    @Test
    public void testAddPerson() {
        Person person = new Person(6, "sample5", "sam5");

        Person response = restTemplate.postForObject(baseUrl, person, Person.class);

        Assertions.assertEquals("sample5", response.getName());

    }

    @Test
    @Sql(statements = "Insert into PERSON (id,name,last_Name) Values(6,'JohnA','DC')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "Delete from person where id=6", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getPerson() {
        List<Person> response = restTemplate.getForObject(baseUrl, List.class);

        Assertions.assertEquals(1, personDAO.findAll().size());
    }

    @Test
    @Sql(statements = "Insert into PERSON (id,name,last_Name) Values(6,'JohnA','DC')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "Delete from person where id=6", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getPersonById() {
        Person person = restTemplate.getForObject(baseUrl + "/{id}", Person.class, 6);

        Assertions.assertEquals(1, personDAO.findAll().size());
    }

    
    
    
    
    @Test
    @Sql(statements = "Insert into person(id,name,last_Name) Values(6,'JohnA','DC')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "Delete from person where id=6", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updatePerson() {

        Person person = new Person(6, "JohnB", "NYC");

        restTemplate.put(baseUrl + "/{id}", person, 6);
        Person person1 = personDAO.findById(6).get();

        Assertions.assertNotNull(person1);
        Assertions.assertEquals("JohnB", person1.getName());

        Assertions.assertEquals("NYC", person1.getLastName());
    }


    @Test
    @Sql(statements = "Insert into person(id,name,last_Name) Values(1,'JohnA','DC')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testDeletePerson(){
        int reco = personDAO.findAll().size();
        Assertions.assertEquals(1,reco);
        restTemplate.delete(baseUrl+"/{id}",1);
        Assertions.assertEquals(0,personDAO.findAll().size());
    }






}
