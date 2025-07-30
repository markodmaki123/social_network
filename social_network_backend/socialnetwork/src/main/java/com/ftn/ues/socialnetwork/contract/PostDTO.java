package com.ftn.ues.socialnetwork.contract;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostDTO {

    Long id;
    LocalTime creationDate;
    String content;
    Long userId;
    Long groupId;
}
