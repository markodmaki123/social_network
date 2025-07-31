package com.ftn.ues.socialnetwork.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDocument {
    private Long id;
    private String content;
    private LocalDateTime creationDate;
    private Long userId;
    private Long groupId;
    private Set<ReactionDocument> reactions;
}
