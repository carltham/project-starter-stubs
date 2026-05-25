package com.noprobit.servingwebcontent.controller;

import com.noprobit.servingwebcontent.domain.Book;
import com.noprobit.servingwebcontent.requests.UpdateBookRequest;
import com.noprobit.servingwebcontent.service.BookService;
import com.noprobit.servingwebcontent.service.PersonService;
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
public class APIUserController {

    private PersonService personService;


    /*
    // Create a new User
    this.matchHttpToFunction.post('/', this.userService.create);

    // Retrieve all Users
    this.matchHttpToFunction.get('/', this.userService.findAll);

    // Retrieve all published Users
    this.matchHttpToFunction.get('/published', this.userService.findAllPublished);

    // Retrieve a single User with id
    this.matchHttpToFunction.get('/:id', this.userService.findOne);

    // Update a User with id
    this.matchHttpToFunction.put('/:id', this.userService.update);

    // Delete a User with id
    this.matchHttpToFunction.delete('/:id', this.userService.delete);

    // Delete all Users
    this.matchHttpToFunction.delete('/', this.userService.deleteAll);
     */
    @Autowired
    public APIUserController(BookService bookService) {
        this.personService = bookService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Book createBook(@RequestBody Book book) {
        return personService.createBook(book.getName(), book.getEmail());
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Book> getAllBooks() {
        return personService.getAllBooks();
    }

    @RequestMapping(method = RequestMethod.GET, value = "published")
    public List<Book> getAllPublishedBooks() {
        return personService.getAllByState(State.PUBLISHED);
    }

    @RequestMapping(method = RequestMethod.GET, value = "{id}")
    public Book getBook(@PathVariable("id") String id) {
        Assert.hasText(id, "id is missing");
        return personService.getBookByUuid(id);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "{id}")
    public Book updateBook(@PathVariable("id") String id, @RequestBody UpdateBookRequest updateBookRequest) {
        Assert.hasText(id, "id is missing");
        Assert.notNull(updateBookRequest, "Request does not contain a Book to be modified");

        updateBookRequest.setId(id);
        return personService.updateBook(updateBookRequest);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "{id}")
    public void deleteBook(@PathVariable("id") String id) {
        Assert.hasText(id, "id is missing");
        personService.deleteBookById(id);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void deleteAllBooks() {
        personService.deleteAll();
    }
}
