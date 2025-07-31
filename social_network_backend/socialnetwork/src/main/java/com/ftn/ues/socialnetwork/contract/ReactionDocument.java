package com.ftn.ues.socialnetwork.contract;

import com.ftn.ues.socialnetwork.model.Post;
import com.ftn.ues.socialnetwork.model.Reaction;
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
    private String createdAt;
    private Long userId;
    private ReactionType reactionType;
    private Long postId;

    public static ReactionDocument fromReactions(Reaction reaction) {
        ReactionDocument docs = new ReactionDocument();
        docs.setId(reaction.getId());
        docs.setCreatedAt(reaction.getCreatedAt().toString());
        docs.setReactionType(reaction.getReactionType());
        docs.setUserId(reaction.getUser().getId());
        docs.setPostId(reaction.getPost().getId());
        return docs;
    }
}

