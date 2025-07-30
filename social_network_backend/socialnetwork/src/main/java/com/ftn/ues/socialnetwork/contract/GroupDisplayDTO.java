package com.ftn.ues.socialnetwork.contract;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupDisplayDTO {

    Long id;
    LocalDateTime createdAt;
    String name;
    String description;
    Long adminId;
}
