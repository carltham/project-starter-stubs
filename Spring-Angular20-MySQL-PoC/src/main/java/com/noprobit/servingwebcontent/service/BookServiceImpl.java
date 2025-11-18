package com.noprobit.servingwebcontent.service;

import com.noprobit.servingwebcontent.domain.Person;
import com.noprobit.servingwebcontent.requests.UpdateUserRequest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperatorServiceImpl implements OperatorService {

    private UserService userService;

    @Autowired
    public OperatorServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Person createOperator(String name, String email) {
        return userService.createUser(name, email);
    }

    @Override
    public List<Person> getAllOperators() {
        return userService.getAllUsers();
    }

    @Override
    public Person getOperatorByKey(String operatorKey) {
        return userService.getUserByKey(operatorKey);
    }

    @Override
    public void deleteOperatorByKey(String operatorKey) {
        userService.deleteUserByKey(operatorKey);
    }

    @Override
    public Person updateOperator(UpdateUserRequest updateOperatorRequest) {
        return userService.updateUser(updateOperatorRequest);
    }
}
