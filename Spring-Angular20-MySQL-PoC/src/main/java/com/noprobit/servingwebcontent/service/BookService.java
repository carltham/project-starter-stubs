package com.noprobit.servingwebcontent.service;

import com.noprobit.servingwebcontent.domain.Book;
import com.noprobit.servingwebcontent.requests.NewBookRequest;
import com.noprobit.servingwebcontent.requests.UpdateBookRequest;
import com.noprobit.servingwebcontent.supporting.State;
import java.util.List;

public interface BookService {

    Book createBook(String name, String email);

    Book createBook(NewBookRequest bookRequest);

    List<Book> getAllBooks();

    public List<Book> getAllByState(State state);

    Book getBookByUuid(String bookKey);

    Long deleteBookById(String bookKey);

    Book updateBook(UpdateBookRequest book);

    public void deleteAll();
}
