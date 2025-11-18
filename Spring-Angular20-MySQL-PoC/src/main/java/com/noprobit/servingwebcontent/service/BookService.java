package com.noprobit.servingwebcontent.service;

import com.noprobit.servingwebcontent.domain.Book;
import com.noprobit.servingwebcontent.requests.NewBookRequest;
import com.noprobit.servingwebcontent.requests.UpdateBookRequest;
import java.util.List;

public interface BookService {

    Book createBook(String name, String email);

    Book createBook(NewBookRequest bookRequest);

    List<Book> getAllBooks();

    Book getBookByUuid(String bookKey);

    Long deleteBookByUuid(String bookKey);

    Book updateBook(UpdateBookRequest book);
}
