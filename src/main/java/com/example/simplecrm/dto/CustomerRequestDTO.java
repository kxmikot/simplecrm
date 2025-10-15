package com.example.simplecrm.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerRequestDTO {

    @NotBlank(message = "Name is required!")
    @Size(min = 2, max = 50)
    private String name;

    @NotBlank(message = "Email is required!")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Phone is required!")
    @Pattern(regexp = "\\+?[0-9\\- ]{7,15}", message = "Phone must be a valid number")
    private String phone;
}
