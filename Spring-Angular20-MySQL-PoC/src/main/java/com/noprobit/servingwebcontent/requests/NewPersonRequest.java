package com.noprobit.servingwebcontent.requests;

import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

public class NewPersonRequest {

    @NotBlank
    @Size(min = 3, max = 50)
    private String name;

    @NotBlank
    @Size(max = 50)
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
