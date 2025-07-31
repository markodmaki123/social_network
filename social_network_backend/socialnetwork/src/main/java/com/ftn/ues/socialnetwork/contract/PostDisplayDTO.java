package com.ftn.ues.socialnetwork.contract;

import com.ftn.ues.socialnetwork.model.Post;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostDisplayDTO {

    Long id;
    LocalDateTime creationDate;
    String content;
    String userName;
    Long groupId;
    Set<ReactionDTO> reactions = new HashSet<>();
}
