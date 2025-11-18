package com.noprobit.servingwebcontent.service;

import com.noprobit.servingwebcontent.domain.Person;
import com.noprobit.servingwebcontent.exception.UserAlreadyExistException;
import com.noprobit.servingwebcontent.exception.UserNotFoundException;
import com.noprobit.servingwebcontent.repository.UserRepository;
import com.noprobit.servingwebcontent.requests.NewUserRequest;
import com.noprobit.servingwebcontent.requests.UpdateUserRequest;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class UserServiceImpl implements UserService {

    private String uuidRegex = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}";
    private final String randomPassword = BCrypt.hashpw("123456", BCrypt.gensalt());

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Person createUser(String name, String email) {
        Assert.hasText(email, "Email should be specified");

        if (isUserExists(email)) {
            throw new UserAlreadyExistException("User with email " + email + " is already exist");
        }

        Person user = new Person(name, email);
        user.setKey(UUID.randomUUID().toString());
        user.setPassword(randomPassword);

        return userRepository.save(user);
    }

    @Override
    public Person createUser(NewUserRequest userRequest) {
        Assert.notNull(userRequest, "Empty user credentials received");

        String userName = userRequest.getName();
        String userEmail = userRequest.getEmail();

        return createUser(userName, userEmail);
    }

    @Override
    public List<Person> getAllUsers() {
        return userRepository.findAll().stream()
                .collect(Collectors.toList());
    }

    @Override
    public Person getUserByKey(String userKey) {
        validateKey(userKey);
        return userRepository.findByUserKey(userKey)
                .orElse(null);
    }

    @Override
    @Transactional
    public Long deleteUserByKey(String userKey) {
        validateKey(userKey);
        return userRepository.deleteByUserKey(userKey);
    }

    @Override
    @Transactional
    public Person updateUser(UpdateUserRequest updateUserRequest) {
        Assert.notNull(updateUserRequest, "No user requested to update");

        Person existingUser = this.getUserByKey(updateUserRequest.getKey());

        if (existingUser == null) {
            throw new UserNotFoundException("User with email " + updateUserRequest.getEmail() + " not found");
        }

        if (StringUtils.isNoneBlank(updateUserRequest.getName())) {
            existingUser.setName(updateUserRequest.getName());
        }

        if (StringUtils.isNoneBlank(updateUserRequest.getEmail())) {
            String email = updateUserRequest.getEmail();
            this.userRepository.findByEmail(email).ifPresent(user -> {
                if (!user.getKey().equals(existingUser.getKey())) {
                    throw new UserAlreadyExistException("User with email " + email + " is already exist");
                }
            });

            existingUser.setEmail(updateUserRequest.getEmail());
        }

//        if (StringUtils.isNoneBlank(updateUserRequest.getPassword())) {
//            String passwd = BCrypt.hashpw(updateUserRequest.getPassword(), BCrypt.gensalt());
//            existingUser.setPassword(passwd);
//        }
        return userRepository.save(existingUser);
    }

    private boolean isUserExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    private void validateKey(String key) {
        Assert.hasText(key, "'userKey' should be specified");
        Assert.isTrue(key.matches(uuidRegex), "'userKey' should be a UUID key");
    }
}
