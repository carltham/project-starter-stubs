package ua.sernikov.service;

import com.noprobit.servingwebcontent.domain.Person;
import com.noprobit.servingwebcontent.exception.PersonAlreadyExistException;
import com.noprobit.servingwebcontent.exception.PersonNotFoundException;
import com.noprobit.servingwebcontent.repository.PersonRepository;
import com.noprobit.servingwebcontent.requests.UpdatePersonRequest;
import com.noprobit.servingwebcontent.service.PersonService;
import com.noprobit.servingwebcontent.service.PersonServiceImpl;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mockito;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//@RunWith(JUnit4.class)
public class PersonServiceTest {

    private PersonService personService;
    private PersonRepository personRepositoryMock;

    private final String TEST_NAME = "test";
    private final String TEST_EMAIL = "test@mail.com";

    @BeforeEach
    public void setUp() throws Exception {
        personRepositoryMock = Mockito.mock(PersonRepository.class);
        personService = new PersonServiceImpl(personRepositoryMock);
    }

    @Test
    public void createPerson_ShouldCreateNewPerson() throws Exception {
        when(personRepositoryMock.findByEmail(anyString())).thenReturn(Optional.empty());
        doAnswer(returnsFirstArg()).when(personRepositoryMock).save(any(Person.class));

        Person actualPerson = personService.createPerson(TEST_NAME, TEST_EMAIL);

        assertThat(actualPerson).isNotNull();
        assertThat(actualPerson.getUuid()).isNotEmpty();
        assertThat(actualPerson.getName()).isEqualTo(TEST_NAME);
        assertThat(actualPerson.getEmail()).isEqualTo(TEST_EMAIL);
    }

    @Test
    public void createPerson_ShouldCreatePersonsWithUniqueKeys() throws Exception {
        when(personRepositoryMock.findByEmail(anyString())).thenReturn(Optional.empty());
        doAnswer(returnsFirstArg()).when(personRepositoryMock).save(any(Person.class));

        String name1 = "test1";
        String email1 = name1 + "@mail.com";
        Person person1 = personService.createPerson(name1, email1);

        String name2 = "test2";
        String email2 = name2 + "@mail.com";
        Person person2 = personService.createPerson(name2, email2);

        assertThat(person1.getUuid()).isNotEqualTo(person2.getUuid());
    }

    @Test()
    public void createPerson_ShouldThrowPersonAlreadyExistException_WhenPersonExistsWithGivenEmail() throws Exception {
        PersonAlreadyExistException exception = assertThrows(PersonAlreadyExistException.class, () -> {
            when(personRepositoryMock.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty())
                    .thenReturn(Optional.of(mock(Person.class)));
            String name1 = "test1";
            String name2 = "test2";

            personService.createPerson(name1, TEST_EMAIL);
            personService.createPerson(name2, TEST_EMAIL);
        });
        assertEquals("Person with email test@mail.com is already exist", exception.getMessage());
    }

    @Test()
    public void createPerson_ShouldThrowIllegalArgumentException_WhenEmailIsNotPresentedOrEmpty() throws Exception {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {

            personService.createPerson(TEST_NAME, null);
            personService.createPerson(TEST_NAME, "");
        });

        assertEquals("Email should be specified", exception.getMessage());
    }

    @Test
    public void getPersonByUuid_ShouldGivePersonByUuid() throws Exception {
        doAnswer(returnsFirstArg()).when(personRepositoryMock).save(any(Person.class));
        when(personRepositoryMock.findByEmail(anyString())).thenReturn(Optional.empty());

        Person operator1 = personService.createPerson(TEST_NAME, "test1@mail.com");
        Person operator2 = personService.createPerson(TEST_NAME, "test2@mail.com");

        when(personRepositoryMock.findByUuid(operator2.getUuid())).thenReturn(Optional.of(operator2));
        Person actualOperator = personService.getPersonByUuid(operator2.getUuid());

        assertThat(actualOperator).isNotNull()
                .isEqualTo(operator2)
                .isNotEqualTo(operator1);
    }

    @Test()
    public void getPersonByUuid_ShouldThrowIllegalArgumentException_WhenKeyNotPresentedOrEmpty() throws Exception {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {

            personService.getPersonByUuid(null);
            personService.getPersonByUuid("");
        });

        assertEquals("'uuid' should be specified", exception.getMessage());
    }

    @Test()
    public void getPersonByUuid_ShouldThrowIllegalArgumentException_WhenKeyIsNotUUID() throws Exception {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            personService.getPersonByUuid("test string");
        });

        assertEquals("'uuid' should be a UUID key", exception.getMessage());
    }

    @Test
    public void getPersonByUuid_ShouldGiveNull_WhenPersonDoesNotExistWithGivenKey() throws Exception {
        when(personRepositoryMock.findByUuid(anyString())).thenReturn(Optional.empty());

        Person operator = personService.getPersonByUuid(UUID.randomUUID().toString());

        assertThat(operator).isNull();
    }

    @Test
    public void deletePersonByUuid_ShouldRemovePersonByUuid() throws Exception {
        doAnswer(returnsFirstArg()).when(personRepositoryMock).save(any(Person.class));
        when(personRepositoryMock.findByEmail(anyString())).thenReturn(Optional.empty());

        Person operator = personService.createPerson(TEST_NAME, TEST_EMAIL);
        when(personRepositoryMock.deleteByUuid(operator.getUuid())).thenReturn(1L);

        Long deletedCount = personService.deletePersonByUuid(operator.getUuid());
        List<Person> operators = personService.getAllPersons();

        assertThat(deletedCount).isNotNull()
                .isEqualTo(1L);
        assertThat(operators).isEmpty();
    }

    @Test
    public void deletePersonByUuid_ShouldGiveZero_WhenPersonDoesNotExist() throws Exception {
        when(personRepositoryMock.deleteByUuid(anyString())).thenReturn(0L);
        Long deletedCount = personService.deletePersonByUuid(UUID.randomUUID().toString());

        assertThat(deletedCount).isNotNull()
                .isEqualTo(0L);
    }

    @Test()
    public void deletePersonByUuid_ShouldThrowIllegalArgumentException_WhenKeyIsNotUUID() throws Exception {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            personService.deletePersonByUuid("test string");
        });

        assertEquals("'uuid' should be a UUID key", exception.getMessage());
    }

    @Test()
    public void deletePersonByUuid_ShouldThrowIllegalArgumentException_WhenKeyIsNotPresentedOrEmpty() throws Exception {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            personService.deletePersonByUuid(null);
            personService.deletePersonByUuid("");
        });

        assertEquals("'uuid' should be specified", exception.getMessage());
    }

    @Test
    public void updatePerson_ShouldUpdateOnlyNameAndEmail() throws Exception {
        doAnswer(returnsFirstArg()).when(personRepositoryMock).save(any(Person.class));
        when(personRepositoryMock.findByEmail(anyString())).thenReturn(Optional.empty());

        String expectedName = "new_test";
        String expectedEmail = expectedName + "@mail.com";

        Person operator = personService.createPerson(TEST_NAME, TEST_EMAIL);
        when(personRepositoryMock.findByUuid(operator.getUuid())).thenReturn(Optional.of(operator));
        when(personRepositoryMock.findByEmail(operator.getUuid())).thenReturn(Optional.of(operator));

        UpdatePersonRequest updateRequest = new UpdatePersonRequest(operator.getUuid(), expectedName, expectedEmail);

        Person actualOperator = personService.updatePerson(updateRequest);

        assertThat(actualOperator).isNotNull()
                .isEqualTo(operator);

        assertThat(actualOperator.getName()).isEqualTo(expectedName)
                .isNotEqualTo(TEST_NAME);

        assertThat(actualOperator.getEmail()).isEqualTo(expectedEmail)
                .isNotEqualTo(TEST_EMAIL);
    }

    @Test()
    public void updatePerson_ShouldThrowIllegalArgumentException_WhenPersonIsNull() throws Exception {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            personService.updatePerson(null);
        });

        assertEquals("No person requested to update", exception.getMessage());
    }

    @Test()
    public void updatePerson_ShouldThrowPersonNotFoundException_WhenPersonDoesNotExist() throws Exception {
        PersonNotFoundException exception = assertThrows(PersonNotFoundException.class, () -> {
            String key = UUID.randomUUID().toString();
            when(personRepositoryMock.findByUuid(key)).thenReturn(Optional.empty());
            UpdatePersonRequest updateRequest = new UpdatePersonRequest(key, TEST_NAME, TEST_EMAIL);

            personService.updatePerson(updateRequest);
        });

        assertEquals("Person with email test@mail.com not found", exception.getMessage());
    }
}
