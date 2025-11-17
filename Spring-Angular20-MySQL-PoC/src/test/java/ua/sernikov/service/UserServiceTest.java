package ua.sernikov.service;

import com.noprobit.servingwebcontent.domain.Person;
import com.noprobit.servingwebcontent.exception.UserAlreadyExistException;
import com.noprobit.servingwebcontent.exception.UserNotFoundException;
import com.noprobit.servingwebcontent.repository.UserRepository;
import com.noprobit.servingwebcontent.requests.UpdateUserRequest;
import com.noprobit.servingwebcontent.service.UserService;
import com.noprobit.servingwebcontent.service.UserServiceImpl;
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
public class UserServiceTest {

    private UserService userService;
    private UserRepository userRepositoryMock;

    private final String TEST_NAME = "test";
    private final String TEST_EMAIL = "test@mail.com";

    @BeforeEach
    public void setUp() throws Exception {
        userRepositoryMock = Mockito.mock(UserRepository.class);
        userService = new UserServiceImpl(userRepositoryMock);
    }

    @Test
    public void createUser_ShouldCreateNewUser() throws Exception {
        when(userRepositoryMock.findByEmail(anyString())).thenReturn(Optional.empty());
        doAnswer(returnsFirstArg()).when(userRepositoryMock).save(any(Person.class));

        Person actualUser = userService.createUser(TEST_NAME, TEST_EMAIL);

        assertThat(actualUser).isNotNull();
        assertThat(actualUser.getKey()).isNotEmpty();
        assertThat(actualUser.getName()).isEqualTo(TEST_NAME);
        assertThat(actualUser.getEmail()).isEqualTo(TEST_EMAIL);
    }

    @Test
    public void createUser_ShouldCreateUsersWithUniqueKeys() throws Exception {
        when(userRepositoryMock.findByEmail(anyString())).thenReturn(Optional.empty());
        doAnswer(returnsFirstArg()).when(userRepositoryMock).save(any(Person.class));

        String name1 = "test1";
        String email1 = name1 + "@mail.com";
        Person user1 = userService.createUser(name1, email1);

        String name2 = "test2";
        String email2 = name2 + "@mail.com";
        Person user2 = userService.createUser(name2, email2);

        assertThat(user1.getKey()).isNotEqualTo(user2.getKey());
    }

    @Test()
    public void createUser_ShouldThrowUserAlreadyExistException_WhenUserExistsWithGivenEmail() throws Exception {
        UserAlreadyExistException exception = assertThrows(UserAlreadyExistException.class, () -> {
            when(userRepositoryMock.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty())
                    .thenReturn(Optional.of(mock(Person.class)));
            String name1 = "test1";
            String name2 = "test2";

            userService.createUser(name1, TEST_EMAIL);
            userService.createUser(name2, TEST_EMAIL);
        });
        assertEquals("User with email test@mail.com is already exist", exception.getMessage());
    }

    @Test()
    public void createUser_ShouldThrowIllegalArgumentException_WhenEmailIsNotPresentedOrEmpty() throws Exception {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {

            userService.createUser(TEST_NAME, null);
            userService.createUser(TEST_NAME, "");
        });

        assertEquals("Email should be specified", exception.getMessage());
    }

    @Test
    public void getUserByKey_ShouldGiveUserByKey() throws Exception {
        doAnswer(returnsFirstArg()).when(userRepositoryMock).save(any(Person.class));
        when(userRepositoryMock.findByEmail(anyString())).thenReturn(Optional.empty());

        Person operator1 = userService.createUser(TEST_NAME, "test1@mail.com");
        Person operator2 = userService.createUser(TEST_NAME, "test2@mail.com");

        when(userRepositoryMock.findByUserKey(operator2.getKey())).thenReturn(Optional.of(operator2));
        Person actualOperator = userService.getUserByKey(operator2.getKey());

        assertThat(actualOperator).isNotNull()
                .isEqualTo(operator2)
                .isNotEqualTo(operator1);
    }

    @Test()
    public void getUserByKey_ShouldThrowIllegalArgumentException_WhenUserKeyNotPresentedOrEmpty() throws Exception {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {

            userService.getUserByKey(null);
            userService.getUserByKey("");
        });

        assertEquals("'userKey' should be specified", exception.getMessage());
    }

    @Test()
    public void getUserByKey_ShouldThrowIllegalArgumentException_WhenUserKeyIsNotUUID() throws Exception {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.getUserByKey("test string");
        });

        assertEquals("'userKey' should be a UUID key", exception.getMessage());
    }

    @Test
    public void getUserByKey_ShouldGiveNull_WhenUserDoesNotExistWithGivenKey() throws Exception {
        when(userRepositoryMock.findByUserKey(anyString())).thenReturn(Optional.empty());

        Person operator = userService.getUserByKey(UUID.randomUUID().toString());

        assertThat(operator).isNull();
    }

    @Test
    public void deleteUserByKey_ShouldRemoveUserByKey() throws Exception {
        doAnswer(returnsFirstArg()).when(userRepositoryMock).save(any(Person.class));
        when(userRepositoryMock.findByEmail(anyString())).thenReturn(Optional.empty());

        Person operator = userService.createUser(TEST_NAME, TEST_EMAIL);
        when(userRepositoryMock.deleteByUserKey(operator.getKey())).thenReturn(1L);

        Long deletedCount = userService.deleteUserByKey(operator.getKey());
        List<Person> operators = userService.getAllUsers();

        assertThat(deletedCount).isNotNull()
                .isEqualTo(1L);
        assertThat(operators).isEmpty();
    }

    @Test
    public void deleteUserByKey_ShouldGiveZero_WhenUserDoesNotExist() throws Exception {
        when(userRepositoryMock.deleteByUserKey(anyString())).thenReturn(0L);
        Long deletedCount = userService.deleteUserByKey(UUID.randomUUID().toString());

        assertThat(deletedCount).isNotNull()
                .isEqualTo(0L);
    }

    @Test()
    public void deleteUserByKey_ShouldThrowIllegalArgumentException_WhenUserKeyIsNotUUID() throws Exception {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.deleteUserByKey("test string");
        });

        assertEquals("'userKey' should be a UUID key", exception.getMessage());
    }

    @Test()
    public void deleteUserByKey_ShouldThrowIllegalArgumentException_WhenUserKeyIsNotPresentedOrEmpty() throws Exception {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.deleteUserByKey(null);
            userService.deleteUserByKey("");
        });

        assertEquals("'userKey' should be specified", exception.getMessage());
    }

    @Test
    public void updateUser_ShouldUpdateOnlyNameAndEmail() throws Exception {
        doAnswer(returnsFirstArg()).when(userRepositoryMock).save(any(Person.class));
        when(userRepositoryMock.findByEmail(anyString())).thenReturn(Optional.empty());

        String expectedName = "new_test";
        String expectedEmail = expectedName + "@mail.com";

        Person operator = userService.createUser(TEST_NAME, TEST_EMAIL);
        when(userRepositoryMock.findByUserKey(operator.getKey())).thenReturn(Optional.of(operator));
        when(userRepositoryMock.findByEmail(operator.getKey())).thenReturn(Optional.of(operator));

        UpdateUserRequest updateRequest = new UpdateUserRequest(operator.getKey(), expectedName, expectedEmail);

        Person actualOperator = userService.updateUser(updateRequest);

        assertThat(actualOperator).isNotNull()
                .isEqualTo(operator);

        assertThat(actualOperator.getName()).isEqualTo(expectedName)
                .isNotEqualTo(TEST_NAME);

        assertThat(actualOperator.getEmail()).isEqualTo(expectedEmail)
                .isNotEqualTo(TEST_EMAIL);
    }

    @Test()
    public void updateUser_ShouldThrowIllegalArgumentException_WhenUserIsNull() throws Exception {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUser(null);
        });

        assertEquals("No user requested to update", exception.getMessage());
    }

    @Test()
    public void updateUser_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() throws Exception {
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            String userKey = UUID.randomUUID().toString();
            when(userRepositoryMock.findByUserKey(userKey)).thenReturn(Optional.empty());
            UpdateUserRequest updateRequest = new UpdateUserRequest(userKey, TEST_NAME, TEST_EMAIL);

            userService.updateUser(updateRequest);
        });

        assertEquals("User with email test@mail.com not found", exception.getMessage());
    }
}
