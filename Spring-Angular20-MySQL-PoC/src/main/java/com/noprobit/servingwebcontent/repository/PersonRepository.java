package com.noprobit.servingwebcontent.repository;

import com.noprobit.servingwebcontent.domain.Person;
import java.util.Collection;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Long> {

    @Override
    Collection<Person> findAll();

    Optional<Person> findByUuid(String uuid);

    Optional<Person> findByEmail(String email);

    Long deleteByUuid(String uuid);

    @Modifying(clearAutomatically = true)
    @Query("update Person p set p.fullName=?1, p.email=?2 where p.uuid=?3")
    void updateByUuid(String name, String email, String uuid);
}
