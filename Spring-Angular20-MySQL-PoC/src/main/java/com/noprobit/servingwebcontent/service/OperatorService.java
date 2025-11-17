package com.noprobit.servingwebcontent.service;

import com.noprobit.servingwebcontent.domain.Person;
import com.noprobit.servingwebcontent.requests.UpdateUserRequest;
import java.util.List;

public interface OperatorService {

    Person createOperator(String name, String email);

    List<Person> getAllOperators();

    Person getOperatorByKey(String operatorKey);

    void deleteOperatorByKey(String operatorKey);

    Person updateOperator(UpdateUserRequest updateOperatorRequest);
}
