package com.noprobit.servingwebcontent.service;

import com.noprobit.servingwebcontent.domain.Person;
import com.noprobit.servingwebcontent.exception.PersonAlreadyExistException;
import com.noprobit.servingwebcontent.exception.PersonNotFoundException;
import com.noprobit.servingwebcontent.repository.PersonRepository;
import com.noprobit.servingwebcontent.requests.NewPersonRequest;
import com.noprobit.servingwebcontent.requests.UpdatePersonRequest;
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
public class PersonServiceImpl implements PersonService {

    private String uuidRegex = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}";
    private final String randomPassword = BCrypt.hashpw("123456", BCrypt.gensalt());

    private PersonRepository personRepository;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Person createPerson(String name, String email) {
        Assert.hasText(email, "Email should be specified");

        if (isPersonExists(email)) {
            throw new PersonAlreadyExistException("Person with email " + email + " is already exist");
        }

        Person person = new Person(name, email);
        person.setUuid(UUID.randomUUID().toString());
        person.setPassword(randomPassword);

        return personRepository.save(person);
    }

    @Override
    public Person createPerson(NewPersonRequest personRequest) {
        Assert.notNull(personRequest, "Empty person credentials received");

        String personName = personRequest.getName();
        String personEmail = personRequest.getEmail();

        return createPerson(personName, personEmail);
    }

    @Override
    public List<Person> getAllPersons() {
        return personRepository.findAll().stream()
                .collect(Collectors.toList());
    }

    @Override
    public Person getPersonByUuid(String key) {
        validateKey(key);
        return personRepository.findByUuid(key)
                .orElse(null);
    }

    @Override
    @Transactional
    public Long deletePersonByUuid(String key) {
        validateKey(key);
        return personRepository.deleteByUuid(key);
    }

    @Override
    @Transactional
    public Person updatePerson(UpdatePersonRequest updatePersonRequest) {
        Assert.notNull(updatePersonRequest, "No person requested to update");

        Person existingPerson = this.getPersonByUuid(updatePersonRequest.getKey());

        if (existingPerson == null) {
            throw new PersonNotFoundException("Person with email " + updatePersonRequest.getEmail() + " not found");
        }

        if (StringUtils.isNoneBlank(updatePersonRequest.getName())) {
            existingPerson.setName(updatePersonRequest.getName());
        }

        if (StringUtils.isNoneBlank(updatePersonRequest.getEmail())) {
            String email = updatePersonRequest.getEmail();
            this.personRepository.findByEmail(email).ifPresent(person -> {
                if (!person.getUuid().equals(existingPerson.getUuid())) {
                    throw new PersonAlreadyExistException("Person with email " + email + " is already exist");
                }
            });

            existingPerson.setEmail(updatePersonRequest.getEmail());
        }

//        if (StringUtils.isNoneBlank(updatePersonRequest.getPassword())) {
//            String passwd = BCrypt.hashpw(updatePersonRequest.getPassword(), BCrypt.gensalt());
//            existingPerson.setPassword(passwd);
//        }
        return personRepository.save(existingPerson);
    }

    private boolean isPersonExists(String email) {
        return personRepository.findByEmail(email).isPresent();
    }

    private void validateKey(String key) {
        Assert.hasText(key, "'uuid' should be specified");
        Assert.isTrue(key.matches(uuidRegex), "'uuid' should be a UUID key");
    }
}
