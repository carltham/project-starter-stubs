package com.noprobit.servingwebcontent.service;

import com.noprobit.servingwebcontent.domain.Person;
import com.noprobit.servingwebcontent.requests.UpdatePersonRequest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperatorServiceImpl implements OperatorService {

    private PersonService personService;

    @Autowired
    public OperatorServiceImpl(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public Person createOperator(String name, String email) {
        return personService.createPerson(name, email);
    }

    @Override
    public List<Person> getAllOperators() {
        return personService.getAllPersons();
    }

    @Override
    public Person getOperatorByUuid(String operatorKey) {
        return personService.getPersonByUuid(operatorKey);
    }

    @Override
    public void deleteOperatorByUuid(String operatorKey) {
        personService.deletePersonByUuid(operatorKey);
    }

    @Override
    public Person updateOperator(UpdatePersonRequest updateOperatorRequest) {
        return personService.updatePerson(updateOperatorRequest);
    }
}
