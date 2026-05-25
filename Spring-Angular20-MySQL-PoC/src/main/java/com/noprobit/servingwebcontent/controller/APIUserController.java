package com.noprobit.servingwebcontent.controller;

import com.noprobit.servingwebcontent.domain.Book;
import com.noprobit.servingwebcontent.requests.UpdateBookRequest;
import com.noprobit.servingwebcontent.service.BookService;
import com.noprobit.servingwebcontent.supporting.State;
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


    /*
    // Create a new Book
    this.matchHttpToFunction.post('/', this.bookService.create);

    // Retrieve all Books
    this.matchHttpToFunction.get('/', this.bookService.findAll);

    // Retrieve all published Books
    this.matchHttpToFunction.get('/published', this.bookService.findAllPublished);

    // Retrieve a single Book with id
    this.matchHttpToFunction.get('/:id', this.bookService.findOne);

    // Update a Book with id
    this.matchHttpToFunction.put('/:id', this.bookService.update);

    // Delete a Book with id
    this.matchHttpToFunction.delete('/:id', this.bookService.delete);

    // Delete all Books
    this.matchHttpToFunction.delete('/', this.bookService.deleteAll);
     */
    @Autowired
    public APIBookController(BookService bookService) {
        this.bookService = bookService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Book createBook(@RequestBody Book book) {
        return bookService.createBook(book.getName(), book.getEmail());
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @RequestMapping(method = RequestMethod.GET, value = "published")
    public List<Book> getAllPublishedBooks() {
        return bookService.getAllByState(State.PUBLISHED);
    }

    @RequestMapping(method = RequestMethod.GET, value = "{id}")
    public Book getBook(@PathVariable("id") String id) {
        Assert.hasText(id, "id is missing");
        return bookService.getBookByUuid(id);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "{id}")
    public Book updateBook(@PathVariable("id") String id, @RequestBody UpdateBookRequest updateBookRequest) {
        Assert.hasText(id, "id is missing");
        Assert.notNull(updateBookRequest, "Request does not contain a Book to be modified");

        updateBookRequest.setId(id);
        return bookService.updateBook(updateBookRequest);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "{id}")
    public void deleteBook(@PathVariable("id") String id) {
        Assert.hasText(id, "id is missing");
        bookService.deleteBookById(id);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void deleteAllBooks() {
        bookService.deleteAll();
    }
}
