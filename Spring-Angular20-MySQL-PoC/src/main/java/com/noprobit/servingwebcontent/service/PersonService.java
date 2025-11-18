package com.noprobit.servingwebcontent.service;

import com.noprobit.servingwebcontent.domain.Person;
import com.noprobit.servingwebcontent.requests.NewPersonRequest;
import com.noprobit.servingwebcontent.requests.UpdatePersonRequest;
import java.util.List;

public interface PersonService {

    Person createPerson(String name, String email);

    Person createPerson(NewPersonRequest personRequest);

    List<Person> getAllPersons();

    Person getPersonByUuid(String key);

    Long deletePersonByUuid(String key);

    Person updatePerson(UpdatePersonRequest person);
}
