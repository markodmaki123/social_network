package com.ftn.ues.socialnetwork.contract;

import com.ftn.ues.socialnetwork.model.Group;
import com.ftn.ues.socialnetwork.model.Post;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupDocument {
    private Long id;
    private String name;
    private String description;
    private String createdAt;
    private Long adminId;
    Set<PostDocument> posts = new HashSet<>();

    public static GroupDocument fromGroup(Group group) {
        GroupDocument doc = new GroupDocument();
        doc.setId(group.getId());
        doc.setName(group.getName());
        doc.setDescription(group.getDescription());
        doc.setAdminId(group.getAdmin().getId());
        doc.setCreatedAt(group.getCreatedAt() != null ? group.getCreatedAt().toString() : null);

        Set<PostDocument> postDocs = group.getPosts().stream()
                .map(post -> {
                    PostDocument pd = new PostDocument();
                    pd.setId(post.getId());
                    pd.setContent(post.getContent());
                    pd.setCreationDate(post.getCreationDate() != null ? post.getCreationDate().toString() : null);
                    pd.setUserId(post.getUser().getId());
                    pd.setGroupId(post.getGroup().getId());

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

                    pd.setReactions(reactionDocs);

                    return pd;
                })
                .collect(Collectors.toSet());

        doc.setPosts(postDocs);

        return doc;
    }

}