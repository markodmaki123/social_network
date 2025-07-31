package com.ftn.ues.socialnetwork.service.implementation;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.ftn.ues.socialnetwork.contract.*;
import com.ftn.ues.socialnetwork.model.Group;
import com.ftn.ues.socialnetwork.model.Post;
import com.ftn.ues.socialnetwork.service.IndexingService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IndexingServiceImplementation implements IndexingService {

    final ElasticsearchClient elasticsearchClient;

    @Autowired
    public IndexingServiceImplementation(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    public void indexPost(PostDTO post) {
        try {
            Set<ReactionDocument> reactionDocs = post.getReactions().stream()
                    .map(reaction -> ReactionDocument.builder()
                            .id(reaction.getId())
                            .createdAt(reaction.getCreatedAt())
                            .userId(reaction.getUserId())
                            .reactionType(reaction.getReactionType())
                            .postId(post.getId())
                            .build())
                    .collect(Collectors.toSet());

            PostDocument doc = PostDocument.builder()
                    .id(post.getId())
                    .content(post.getContent())
                    .creationDate(post.getCreationDate())
                    .userId(post.getUserId())
                    .groupId(post.getGroupId())
                    .reactions(reactionDocs)
                    .build();

            elasticsearchClient.index(i -> i
                    .index("posts")
                    .id(String.valueOf(doc.getId()))
                    .document(doc)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void indexGroup(GroupDTO group) {
        try {
            GroupDocument doc = GroupDocument.builder()
                    .id(group.getId())
                    .name(group.getName())
                    .description(group.getDescription())
                    .createdAt(group.getCreatedAt())
                    .adminId(group.getAdminId())
                    .build();

            elasticsearchClient.index(i -> i
                    .index("groups")
                    .id(String.valueOf(doc.getId()))
                    .document(doc)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
