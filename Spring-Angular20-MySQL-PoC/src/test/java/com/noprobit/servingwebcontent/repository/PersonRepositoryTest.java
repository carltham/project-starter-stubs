package com.noprobit.servingwebcontent.repository;

import com.noprobit.servingwebcontent.domain.Person;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;

//@RunWith(SpringRunner.class)
@ContextConfiguration
@DataJpaTest
public class PersonRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PersonRepository personRepository;

    private final String TEST_NAME = "test";
    private final String TEST_EMAIL = TEST_NAME + "@mail.com";
    private final String TEST_PASSWORD = "123456";

    @Test
    public void updateByUuid_ShouldUpdateNameAndEmail() throws Exception {
        Person operator = new Person(TEST_NAME, TEST_EMAIL, TEST_PASSWORD);
        operator.setUuid(UUID.randomUUID().toString());
        entityManager.persistAndFlush(operator);

        String expectedName = "new_test";
        String expectedEmail = expectedName + "@mail.com";
        personRepository.updateByUuid(expectedName, expectedEmail, operator.getUuid());

        Person actualUser = personRepository.findByUuid(operator.getUuid()).orElse(null);

        assertThat(actualUser).isNotNull();

        assertThat(actualUser.getName()).isEqualTo(expectedName)
                .isNotEqualTo(TEST_NAME);

        assertThat(actualUser.getEmail()).isEqualTo(expectedEmail)
                .isNotEqualTo(TEST_EMAIL);

        assertThat(actualUser.getUuid()).isEqualTo(operator.getUuid());
    }

    @Test
    public void updateByUuid_ShouldNotUpdate_WhenUserDoesNotExist() throws Exception {
        Person operator = new Person(TEST_NAME, TEST_EMAIL, TEST_PASSWORD);
        operator.setUuid(UUID.randomUUID().toString());
        entityManager.persistAndFlush(operator);

        personRepository.updateByUuid("new_test", "new_test@mail.com", UUID.randomUUID().toString());

        Person actualUser = personRepository.findByUuid(operator.getUuid()).orElse(null);

        assertThat(actualUser).isNotNull();
        assertThat(actualUser.getName()).isEqualTo(TEST_NAME);
        assertThat(actualUser.getEmail()).isEqualTo(TEST_EMAIL);
    }
}
