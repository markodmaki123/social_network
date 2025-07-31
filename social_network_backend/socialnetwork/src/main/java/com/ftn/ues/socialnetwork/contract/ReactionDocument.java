package com.ftn.ues.socialnetwork.contract;

import com.ftn.ues.socialnetwork.util.ReactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReactionDocument {
    private Long id;
    private LocalDateTime createdAt;
    private Long userId;
    private ReactionType reactionType;
    private Long postId;
}

