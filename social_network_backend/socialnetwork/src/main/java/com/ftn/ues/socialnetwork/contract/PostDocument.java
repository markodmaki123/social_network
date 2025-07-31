package com.ftn.ues.socialnetwork.contract;

import com.ftn.ues.socialnetwork.model.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDocument {
    private Long id;
    private String content;
    private String creationDate;
    private Long userId;
    private Long groupId;
    private Set<ReactionDocument> reactions;

    public static PostDocument fromPost(Post post) {
        PostDocument doc = new PostDocument();
        doc.setId(post.getId());
        doc.setContent(post.getContent());
        doc.setCreationDate(post.getCreationDate().toString());
        doc.setUserId(post.getUser().getId());
        doc.setGroupId(post.getGroup().getId());

        Set<ReactionDocument> reactionDocs = post.getReactions().stream()
                .map(reaction -> {
                    ReactionDocument rd = new ReactionDocument();
                    rd.setId(reaction.getId());
                    rd.setCreatedAt(reaction.getCreatedAt() != null ? reaction.getCreatedAt().toString() : null);
                    rd.setUserId(reaction.getUser().getId());
                    rd.setReactionType(reaction.getReactionType());
                    rd.setPostId(post.getId());
                    return rd;
                })
                .collect(Collectors.toSet());

        doc.setReactions(reactionDocs);

        return doc;
    }
}
