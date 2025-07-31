package com.ftn.ues.socialnetwork.controller;


import com.ftn.ues.socialnetwork.contract.*;
import com.ftn.ues.socialnetwork.model.Post;
import com.ftn.ues.socialnetwork.service.IndexingService;
import com.ftn.ues.socialnetwork.service.PostService;
import com.ftn.ues.socialnetwork.util.SerbianLatinConverter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping(value = "api/post")
public class PostController {

    final PostService postService;

    final IndexingService indexingService;

    final ModelMapper modelMapper;

    @Autowired
    public PostController(PostService postService, IndexingService indexingService,
                          ModelMapper modelMapper) {
        this.postService = postService;
        this.indexingService = indexingService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<PostDTO> addPost(@RequestBody PostDTO postDTO) {
        Post post = postService.addPost(postDTO);
        PostDTO postDtoToIndex = modelMapper.map(post, PostDTO.class);
        indexingService.indexPost(postDtoToIndex);

        PostDTO responseDTO = modelMapper.map(post, PostDTO.class);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<PostDTO>> getAll() {
        List<Post> posts = postService.getAllPosts();
        List<PostDTO> postDTOS = posts.stream()
                .map(post -> modelMapper.map(post, PostDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(postDTOS);
    }

    @GetMapping("/all-group")
    public ResponseEntity<List<PostDTO>> getAllFromGroup(@RequestParam Long id) {
        List<PostDTO> posts = postService.getPostsGroup(id);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getById(@PathVariable Long id) {
        PostDTO post = postService.getPost(id);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/all-names")
    public ResponseEntity<List<PostDisplayDTO>> getAllWithNames() {
        List<Post> posts = postService.getAllPosts();
        List<PostDisplayDTO> postDTOS = posts.stream().map(post -> {
            PostDisplayDTO dto = new PostDisplayDTO();
            dto.setId(post.getId());
            dto.setCreationDate(post.getCreationDate());
            dto.setContent(post.getContent());
            dto.setGroupId(post.getGroup().getId());
            dto.setUserName(post.getUser().getName());

            return getPostDisplayDTO(post, dto);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(postDTOS);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PostDisplayDTO>> searchPosts(@RequestParam String keyword) {
        String normalizedKeyword = SerbianLatinConverter.toLatinLowercase(keyword);
        List<Post> posts = postService.searchPostsByContent(normalizedKeyword);
        List<PostDisplayDTO> dtos = posts.stream().map(post -> {
            PostDisplayDTO dto = new PostDisplayDTO();
            dto.setId(post.getId());
            dto.setContent(post.getContent());
            dto.setCreationDate(post.getCreationDate());
            dto.setUserName(post.getUser().getName());
            dto.setGroupId(post.getGroup().getId());

            return getPostDisplayDTO(post, dto);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @NotNull
    private PostDisplayDTO getPostDisplayDTO(Post post, PostDisplayDTO dto) {
        Set<ReactionDTO> reactionDTOs = post.getReactions().stream().map(reaction -> {
            ReactionDTO reactionDTO = new ReactionDTO();
            reactionDTO.setId(reaction.getId());
            reactionDTO.setReactionType(reaction.getReactionType());
            reactionDTO.setUserId(reaction.getUser().getId());
            return reactionDTO;
        }).collect(Collectors.toSet());

        dto.setReactions(reactionDTOs);

        return dto;
    }

    @GetMapping("/search-content")
    public ResponseEntity<List<PostDocument>> searchPostsByContent(@RequestParam String keyword){
        String normalizedKeyword = SerbianLatinConverter.toLatinLowercase(keyword);
        List<PostDocument> postDocuments = postService.searchPostsByText(normalizedKeyword);
        return ResponseEntity.ok(postDocuments);
    }
}
