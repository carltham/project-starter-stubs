package com.noprobit.servingwebcontent.repository;

import com.noprobit.servingwebcontent.domain.Book;
import java.util.Collection;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {

    @Override
    Collection<Book> findAll();

    Optional<Book> findByUuid(String uuid);

    Optional<Book> findByEmail(String email);

    Long deleteByUuid(String uuid);

    @Modifying(clearAutomatically = true)
    @Query("update Book b set b.fullName=?1, b.email=?2 where b.uuid=?3")
    void updateByUuid(String name, String email, String uuid);
}
