package org.acme;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;


@Data
public class Pessoa {
    
    private Integer pessoaId;

    @NotEmpty
    private String firstName;

    private String middleName;

    @NotEmpty
    private String lastName;

    private String suffix;

    @Email
    private String email;

    private String phone;
}
