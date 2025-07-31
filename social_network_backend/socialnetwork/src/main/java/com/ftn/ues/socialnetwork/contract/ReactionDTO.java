package com.ftn.ues.socialnetwork.contract;

import com.ftn.ues.socialnetwork.util.ReactionType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReactionDTO {

    Long id;
    LocalDateTime createdAt;
    Long userId;
    ReactionType reactionType;
    Long postId;
}
