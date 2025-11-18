package ua.sernikov.service;

import com.noprobit.servingwebcontent.domain.Book;
import com.noprobit.servingwebcontent.exception.BookAlreadyExistException;
import com.noprobit.servingwebcontent.exception.BookNotFoundException;
import com.noprobit.servingwebcontent.repository.BookRepository;
import com.noprobit.servingwebcontent.requests.UpdateBookRequest;
import com.noprobit.servingwebcontent.service.BookService;
import com.noprobit.servingwebcontent.service.BookServiceImpl;
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
public class BookServiceTest {

    private BookService bookService;
    private BookRepository personRepositoryMock;

    private final String TEST_NAME = "test";
    private final String TEST_EMAIL = "test@mail.com";

    @BeforeEach
    public void setUp() throws Exception {
        personRepositoryMock = Mockito.mock(BookRepository.class);
        bookService = new BookServiceImpl(personRepositoryMock);
    }

    @Test
    public void createBook_ShouldCreateNewBook() throws Exception {
        when(personRepositoryMock.findByEmail(anyString())).thenReturn(Optional.empty());
        doAnswer(returnsFirstArg()).when(personRepositoryMock).save(any(Book.class));

        Book actualBook = bookService.createBook(TEST_NAME, TEST_EMAIL);

        assertThat(actualBook).isNotNull();
        assertThat(actualBook.getUuid()).isNotEmpty();
        assertThat(actualBook.getName()).isEqualTo(TEST_NAME);
        assertThat(actualBook.getEmail()).isEqualTo(TEST_EMAIL);
    }

    @Test
    public void createBook_ShouldCreateBooksWithUniqueKeys() throws Exception {
        when(personRepositoryMock.findByEmail(anyString())).thenReturn(Optional.empty());
        doAnswer(returnsFirstArg()).when(personRepositoryMock).save(any(Book.class));

        String name1 = "test1";
        String email1 = name1 + "@mail.com";
        Book person1 = bookService.createBook(name1, email1);

        String name2 = "test2";
        String email2 = name2 + "@mail.com";
        Book person2 = bookService.createBook(name2, email2);

        assertThat(person1.getUuid()).isNotEqualTo(person2.getUuid());
    }

    @Test()
    public void createBook_ShouldThrowBookAlreadyExistException_WhenBookExistsWithGivenEmail() throws Exception {
        BookAlreadyExistException exception = assertThrows(BookAlreadyExistException.class, () -> {
            when(personRepositoryMock.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty())
                    .thenReturn(Optional.of(mock(Book.class)));
            String name1 = "test1";
            String name2 = "test2";

            bookService.createBook(name1, TEST_EMAIL);
            bookService.createBook(name2, TEST_EMAIL);
        });
        assertEquals("Book with email test@mail.com is already exist", exception.getMessage());
    }

    @Test()
    public void createBook_ShouldThrowIllegalArgumentException_WhenEmailIsNotPresentedOrEmpty() throws Exception {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {

            bookService.createBook(TEST_NAME, null);
            bookService.createBook(TEST_NAME, "");
        });

        assertEquals("Email should be specified", exception.getMessage());
    }

    @Test
    public void getBookByUuid_ShouldGiveBookByUuid() throws Exception {
        doAnswer(returnsFirstArg()).when(personRepositoryMock).save(any(Book.class));
        when(personRepositoryMock.findByEmail(anyString())).thenReturn(Optional.empty());

        Book operator1 = bookService.createBook(TEST_NAME, "test1@mail.com");
        Book operator2 = bookService.createBook(TEST_NAME, "test2@mail.com");

        when(personRepositoryMock.findByUuid(operator2.getUuid())).thenReturn(Optional.of(operator2));
        Book actualOperator = bookService.getBookByUuid(operator2.getUuid());

        assertThat(actualOperator).isNotNull()
                .isEqualTo(operator2)
                .isNotEqualTo(operator1);
    }

    @Test()
    public void getBookByUuid_ShouldThrowIllegalArgumentException_WhenKeyNotPresentedOrEmpty() throws Exception {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {

            bookService.getBookByUuid(null);
            bookService.getBookByUuid("");
        });

        assertEquals("'uuid' should be specified", exception.getMessage());
    }

    @Test()
    public void getBookByUuid_ShouldThrowIllegalArgumentException_WhenKeyIsNotUUID() throws Exception {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bookService.getBookByUuid("test string");
        });

        assertEquals("'uuid' should be a UUID key", exception.getMessage());
    }

    @Test
    public void getBookByUuid_ShouldGiveNull_WhenBookDoesNotExistWithGivenKey() throws Exception {
        when(personRepositoryMock.findByUuid(anyString())).thenReturn(Optional.empty());

        Book operator = bookService.getBookByUuid(UUID.randomUUID().toString());

        assertThat(operator).isNull();
    }

    @Test
    public void deleteBookByUuid_ShouldRemoveBookByUuid() throws Exception {
        doAnswer(returnsFirstArg()).when(personRepositoryMock).save(any(Book.class));
        when(personRepositoryMock.findByEmail(anyString())).thenReturn(Optional.empty());

        Book operator = bookService.createBook(TEST_NAME, TEST_EMAIL);
        when(personRepositoryMock.deleteByUuid(operator.getUuid())).thenReturn(1L);

        Long deletedCount = bookService.deleteBookByUuid(operator.getUuid());
        List<Book> operators = bookService.getAllBooks();

        assertThat(deletedCount).isNotNull()
                .isEqualTo(1L);
        assertThat(operators).isEmpty();
    }

    @Test
    public void deleteBookByUuid_ShouldGiveZero_WhenBookDoesNotExist() throws Exception {
        when(personRepositoryMock.deleteByUuid(anyString())).thenReturn(0L);
        Long deletedCount = bookService.deleteBookByUuid(UUID.randomUUID().toString());

        assertThat(deletedCount).isNotNull()
                .isEqualTo(0L);
    }

    @Test()
    public void deleteBookByUuid_ShouldThrowIllegalArgumentException_WhenKeyIsNotUUID() throws Exception {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bookService.deleteBookByUuid("test string");
        });

        assertEquals("'uuid' should be a UUID key", exception.getMessage());
    }

    @Test()
    public void deleteBookByUuid_ShouldThrowIllegalArgumentException_WhenKeyIsNotPresentedOrEmpty() throws Exception {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bookService.deleteBookByUuid(null);
            bookService.deleteBookByUuid("");
        });

        assertEquals("'uuid' should be specified", exception.getMessage());
    }

    @Test
    public void updateBook_ShouldUpdateOnlyNameAndEmail() throws Exception {
        doAnswer(returnsFirstArg()).when(personRepositoryMock).save(any(Book.class));
        when(personRepositoryMock.findByEmail(anyString())).thenReturn(Optional.empty());

        String expectedName = "new_test";
        String expectedEmail = expectedName + "@mail.com";

        Book operator = bookService.createBook(TEST_NAME, TEST_EMAIL);
        when(personRepositoryMock.findByUuid(operator.getUuid())).thenReturn(Optional.of(operator));
        when(personRepositoryMock.findByEmail(operator.getUuid())).thenReturn(Optional.of(operator));

        UpdateBookRequest updateRequest = new UpdateBookRequest(operator.getUuid(), expectedName, expectedEmail);

        Book actualOperator = bookService.updateBook(updateRequest);

        assertThat(actualOperator).isNotNull()
                .isEqualTo(operator);

        assertThat(actualOperator.getName()).isEqualTo(expectedName)
                .isNotEqualTo(TEST_NAME);

        assertThat(actualOperator.getEmail()).isEqualTo(expectedEmail)
                .isNotEqualTo(TEST_EMAIL);
    }

    @Test()
    public void updateBook_ShouldThrowIllegalArgumentException_WhenBookIsNull() throws Exception {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bookService.updateBook(null);
        });

        assertEquals("No book requested to update", exception.getMessage());
    }

    @Test()
    public void updateBook_ShouldThrowBookNotFoundException_WhenBookDoesNotExist() throws Exception {
        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> {
            String key = UUID.randomUUID().toString();
            when(personRepositoryMock.findByUuid(key)).thenReturn(Optional.empty());
            UpdateBookRequest updateRequest = new UpdateBookRequest(key, TEST_NAME, TEST_EMAIL);

            bookService.updateBook(updateRequest);
        });

        assertEquals("Book with email test@mail.com not found", exception.getMessage());
    }
}
