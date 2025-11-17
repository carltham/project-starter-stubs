package com.noprobit.servingwebcontent.repository;

import com.noprobit.servingwebcontent.domain.Person;
import java.util.Collection;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<Person, Long> {

    @Override
    Collection<Person> findAll();

    Optional<Person> findByUserKey(String key);

    Optional<Person> findByEmail(String email);

    Long deleteByUserKey(String key);

    @Modifying(clearAutomatically = true)
    @Query("update Person u set u.fullName=?1, u.email=?2 where u.userKey=?3")
    void updateByUserKey(String name, String email, String key);
}
