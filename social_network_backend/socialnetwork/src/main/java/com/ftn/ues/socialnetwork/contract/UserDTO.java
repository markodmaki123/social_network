package com.ftn.ues.socialnetwork.contract;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {

    Long id;
    String email;
    String password;
    String name;
    String surname;
    LocalDate lastLogin;
    String username;
    String displayName;
    String description;
}
