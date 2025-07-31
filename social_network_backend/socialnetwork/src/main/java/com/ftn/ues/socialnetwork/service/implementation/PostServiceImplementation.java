package com.ftn.ues.socialnetwork.service.implementation;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.ftn.ues.socialnetwork.contract.PostDTO;
import com.ftn.ues.socialnetwork.contract.PostDisplayDTO;
import com.ftn.ues.socialnetwork.contract.PostDocument;
import com.ftn.ues.socialnetwork.contract.ReactionDTO;
import com.ftn.ues.socialnetwork.infrastructure.repository.GroupRepository;
import com.ftn.ues.socialnetwork.infrastructure.repository.PostRepository;
import com.ftn.ues.socialnetwork.infrastructure.repository.UserRepository;
import com.ftn.ues.socialnetwork.model.Group;
import com.ftn.ues.socialnetwork.model.Post;
import com.ftn.ues.socialnetwork.model.Reaction;
import com.ftn.ues.socialnetwork.model.User;
import com.ftn.ues.socialnetwork.service.FileStoringService;
import com.ftn.ues.socialnetwork.service.PostService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostServiceImplementation implements PostService {

    final PostRepository postRepository;

    final UserRepository userRepository;

    final GroupRepository groupRepository;

    final FileStoringService fileStoringService;

    final ModelMapper modelMapper;

    final ElasticsearchClient elasticsearchClient;

    @Autowired
    public PostServiceImplementation(final PostRepository postRepository,
                                     final UserRepository userRepository,
                                     final GroupRepository groupRepository,
                                     final FileStoringService fileStoringService,
                                     final ModelMapper modelMapper,
                                     final ElasticsearchClient elasticsearchClient) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.fileStoringService = fileStoringService;
        this.modelMapper = modelMapper;
        this.elasticsearchClient = elasticsearchClient;
    }

    @Override
    public Post addPost(PostDTO postDTO) {
        Post post = new Post();
        post.setContent(postDTO.getContent());
        post.setCreationDate(LocalDateTime.now());

        User user = userRepository.findById(postDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        post.setUser(user);

        Group group = groupRepository.findById(postDTO.getGroupId())
                .orElseThrow(() -> new RuntimeException("Group not found"));
        post.setGroup(group);

        Set<Reaction> reactions = new HashSet<>();
        for (ReactionDTO dto : postDTO.getReactions()) {
            Reaction reaction = modelMapper.map(dto, Reaction.class);
            reactions.add(reaction);
        }
        for (Reaction reaction : reactions) {
            reaction.setPost(post);
        }
        post.setReactions(reactions);

        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, pdfOutputStream);
            document.open();
            document.add(new Paragraph(postDTO.getContent()));
            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error creating PDF", e);
        }

        String filename = UUID.randomUUID() + "_post.pdf";
        post.setFileName(filename);

        fileStoringService.uploadFile(pdfOutputStream, filename, "application/pdf");

        return postRepository.save(post);
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll().stream()
                .filter(post -> !post.isDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public List<Post> searchPostsByContent(String keyword){
        return postRepository.findByContentContainingIgnoreCase(keyword);
    }

    @Override
    public void deletePost(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        post.ifPresent(postRepository::delete);
    }

    @Override
    public List<PostDocument> searchPostsByText(String queryText){
        try {
            SearchResponse<PostDocument> response = elasticsearchClient.search(s -> s
                    .index("posts")
                    .query(q -> q
                            .match(m -> m
                                    .field("content")
                                    .query(queryText)
                            )
                    ), PostDocument.class);

            return response.hits().hits().stream()
                    .map(hit -> hit.source())
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public List<PostDTO> getPostsGroup(Long id){
        Optional<Group> groupOpt = groupRepository.findById(id);

        if (groupOpt.isEmpty()) {
            throw new RuntimeException("Group not found with id: " + id);
        }

        Set<Post> posts = groupOpt.get().getPosts();

        return posts.stream()
                .map(post -> modelMapper.map(post, PostDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public PostDTO getPost(Long id) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isEmpty()) {
            throw new RuntimeException("Post not found with id: " + id);
        }

        Post post = postOptional.get();
        return mapToDTO(post);
    }

    private PostDTO mapToDTO(Post post) {
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setContent(post.getContent());
        dto.setCreationDate(post.getCreationDate());
        dto.setUserId(post.getUser().getId());
        dto.setGroupId(post.getGroup().getId());

        Set<ReactionDTO> reactionDTOs = post.getReactions()
                .stream()
                .map(reaction -> {
                    ReactionDTO rDto = new ReactionDTO();
                    rDto.setId(reaction.getId());
                    rDto.setReactionType(reaction.getReactionType());
                    rDto.setUserId(reaction.getUser().getId());
                    rDto.setPostId(post.getId());
                    rDto.setCreatedAt(reaction.getCreatedAt());
                    return rDto;
                })
                .collect(Collectors.toSet());

        dto.setReactions(reactionDTOs);
        return dto;
    }
}
