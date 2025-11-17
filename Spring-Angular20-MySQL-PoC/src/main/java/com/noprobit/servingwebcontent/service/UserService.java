package com.noprobit.servingwebcontent.service;

import com.noprobit.servingwebcontent.domain.Person;
import com.noprobit.servingwebcontent.requests.NewUserRequest;
import com.noprobit.servingwebcontent.requests.UpdateUserRequest;
import java.util.List;

public interface UserService {

    Person createUser(String name, String email);

    Person createUser(NewUserRequest userRequest);

    List<Person> getAllUsers();

    Person getUserByKey(String userKey);

    Long deleteUserByKey(String userKey);

    Person updateUser(UpdateUserRequest user);
}
