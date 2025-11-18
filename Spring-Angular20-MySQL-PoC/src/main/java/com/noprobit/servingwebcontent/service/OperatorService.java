package com.noprobit.servingwebcontent.service;

import com.noprobit.servingwebcontent.domain.Person;
import com.noprobit.servingwebcontent.requests.UpdatePersonRequest;
import java.util.List;

public interface OperatorService {

    Person createOperator(String name, String email);

    List<Person> getAllOperators();

    Person getOperatorByUuid(String operatorKey);

    void deleteOperatorByUuid(String operatorKey);

    Person updateOperator(UpdatePersonRequest updateOperatorRequest);
}
