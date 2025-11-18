package com.noprobit.servingwebcontent.service;

import com.noprobit.servingwebcontent.domain.Book;
import com.noprobit.servingwebcontent.exception.BookAlreadyExistException;
import com.noprobit.servingwebcontent.exception.BookNotFoundException;
import com.noprobit.servingwebcontent.repository.BookRepository;
import com.noprobit.servingwebcontent.requests.NewBookRequest;
import com.noprobit.servingwebcontent.requests.UpdateBookRequest;
import com.noprobit.servingwebcontent.supporting.State;
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
public class BookServiceImpl implements BookService {

    private String uuidRegex = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}";
    private final String randomPassword = BCrypt.hashpw("123456", BCrypt.gensalt());

    private BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book createBook(String name, String email) {
        Assert.hasText(email, "Email should be specified");

        if (isBookExists(email)) {
            throw new BookAlreadyExistException("Book with email " + email + " is already exist");
        }

        Book book = new Book(name, email);
        book.setUuid(UUID.randomUUID().toString());
        book.setPassword(randomPassword);

        return bookRepository.save(book);
    }

    @Override
    public Book createBook(NewBookRequest bookRequest) {
        Assert.notNull(bookRequest, "Empty book credentials received");

        String bookName = bookRequest.getName();
        String bookEmail = bookRequest.getEmail();

        return createBook(bookName, bookEmail);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll().stream()
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> getAllByState(State state) {

        return bookRepository.findByState(state);
        //return bookRepository.findAll().stream()
        //        .collect(Collectors.toList());
    }

    @Override
    public Book getBookByUuid(String bookKey) {
        validateKey(bookKey);
        return bookRepository.findByUuid(bookKey)
                .orElse(null);
    }

    @Override
    @Transactional
    public Long deleteBookById(String bookKey) {
        validateKey(bookKey);
        return bookRepository.deleteByUuid(bookKey);
    }

    @Override
    public void deleteAll() {
        bookRepository.deleteAll();
    }

    @Override
    @Transactional
    public Book updateBook(UpdateBookRequest updateBookRequest) {
        Assert.notNull(updateBookRequest, "No book requested to update");

        Book existingBook = this.getBookByUuid(updateBookRequest.getId());

        if (existingBook == null) {
            throw new BookNotFoundException("Book with email " + updateBookRequest.getEmail() + " not found");
        }

        if (StringUtils.isNoneBlank(updateBookRequest.getName())) {
            existingBook.setName(updateBookRequest.getName());
        }

        if (StringUtils.isNoneBlank(updateBookRequest.getEmail())) {
            String email = updateBookRequest.getEmail();
            this.bookRepository.findByEmail(email).ifPresent(book -> {
                if (!book.getUuid().equals(existingBook.getUuid())) {
                    throw new BookAlreadyExistException("Book with email " + email + " is already exist");
                }
            });

            existingBook.setEmail(updateBookRequest.getEmail());
        }

//        if (StringUtils.isNoneBlank(updateBookRequest.getPassword())) {
//            String passwd = BCrypt.hashpw(updateBookRequest.getPassword(), BCrypt.gensalt());
//            existingBook.setPassword(passwd);
//        }
        return bookRepository.save(existingBook);
    }

    private boolean isBookExists(String email) {
        return bookRepository.findByEmail(email).isPresent();
    }

    private void validateKey(String key) {
        Assert.hasText(key, "'uuid' should be specified");
        Assert.isTrue(key.matches(uuidRegex), "'uuid' should be a UUID key");
    }
}
