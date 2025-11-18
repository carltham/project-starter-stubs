package com.noprobit.servingwebcontent.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.noprobit.servingwebcontent.annotation.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

@Entity
public class Person implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    @Column(unique = true, nullable = false)
    @UUID
    private String uuid;

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true, nullable = false)
    @NotBlank
    @Size(max = 50)
    private String email;

    @Column(nullable = false)
    @NotBlank
    @Size(min = 5, max = 255)
    private String password;

    public Person() {
    }

    public Person(String name, String email) {
        this.fullName = name;
        this.email = email;
    }

    public Person(String name, String email, String password) {
        this(name, email);
        this.password = password;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return fullName;
    }

    public void setName(String name) {
        this.fullName = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("uuid", uuid)
                .append("name", fullName)
                .append("email", email)
                .append("password", password)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Person)) {
            return false;
        }
        Person person = (Person) o;
        return Objects.equals(uuid, person.uuid)
                && Objects.equals(email, person.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, email);
    }
}
