package com.ftn.ues.socialnetwork.contract;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupDTO {

    Long id;
    LocalDateTime createdAt;
    String name;
    String description;
    Set<PostDTO> posts = new HashSet<>();
    Long adminId;
}
