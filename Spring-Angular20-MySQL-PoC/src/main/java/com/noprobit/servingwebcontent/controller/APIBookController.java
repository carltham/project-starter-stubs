package com.noprobit.servingwebcontent.controller;

import com.noprobit.servingwebcontent.domain.Book;
import com.noprobit.servingwebcontent.requests.UpdateBookRequest;
import com.noprobit.servingwebcontent.service.BookService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/books",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class APIBookController {

    private BookService bookService;

    @Autowired
    public APIBookController(BookService bookService) {
        this.bookService = bookService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @RequestMapping(method = RequestMethod.GET, value = "{key}")
    public Book getBook(@PathVariable("key") String bookKey) {
        Assert.hasText(bookKey, "Key is missing");
        return bookService.getBookByUuid(bookKey);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Book createBook(@RequestBody Book book) {
        return bookService.createBook(book.getName(), book.getEmail());
    }

    @RequestMapping(method = RequestMethod.PUT, value = "{key}")
    public Book updateBook(@PathVariable("key") String bookKey, @RequestBody UpdateBookRequest updateBookRequest) {
        Assert.hasText(bookKey, "Key is missing");
        Assert.notNull(updateBookRequest, "Request does not contain a Book to be modified");

        updateBookRequest.setKey(bookKey);
        return bookService.updateBook(updateBookRequest);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "{key}")
    public void deleteBook(@PathVariable("key") String bookKey) {
        Assert.hasText(bookKey, "Key is missing");
        bookService.deleteBookByUuid(bookKey);
    }
}
