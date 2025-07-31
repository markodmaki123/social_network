package com.ftn.ues.socialnetwork.service.implementation;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.ftn.ues.socialnetwork.contract.GroupDTO;
import com.ftn.ues.socialnetwork.contract.GroupDocument;
import com.ftn.ues.socialnetwork.contract.PostDTO;
import com.ftn.ues.socialnetwork.contract.ReactionDTO;
import com.ftn.ues.socialnetwork.infrastructure.repository.GroupRepository;
import com.ftn.ues.socialnetwork.infrastructure.repository.UserRepository;
import com.ftn.ues.socialnetwork.model.Group;
import com.ftn.ues.socialnetwork.model.Post;
import com.ftn.ues.socialnetwork.service.FileStoringService;
import com.ftn.ues.socialnetwork.service.GroupService;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupServiceImplementation implements GroupService {

    final GroupRepository groupRepository;

    final UserRepository userRepository;

    final ModelMapper modelMapper;

    final FileStoringService fileStoringService;

    final ElasticsearchClient elasticsearchClient;

    @Autowired
    public GroupServiceImplementation(final GroupRepository groupRepository,
                                      final ModelMapper modelMapper,
                                      final UserRepository userRepository,
                                      final FileStoringService fileStoringService,
                                      final ElasticsearchClient elasticsearchClient) {
        this.groupRepository = groupRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.fileStoringService = fileStoringService;
        this.elasticsearchClient = elasticsearchClient;
    }

    @Override
    public Group addGroup(GroupDTO groupDTO) {
        Group group = new Group();
        group.setName(groupDTO.getName());
        group.setDescription(groupDTO.getDescription());
        group.setAdmin(userRepository.findById(groupDTO.getAdminId()).orElse(null));
        group.setCreatedAt(LocalDateTime.now());

        Set<Post> posts = new HashSet<>();

        for (PostDTO dto : groupDTO.getPosts()) {
            Post post = modelMapper.map(dto, Post.class);
            posts.add(post);
        }
        for (Post post : posts) {
            post.setGroup(group);
        }
        group.setPosts(posts);

        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, pdfOutputStream);
            document.open();
            document.add(new Paragraph(groupDTO.getDescription()));
            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error creating PDF", e);
        }

        String filename = UUID.randomUUID() + "_group.pdf";
        group.setFileName(filename);

        fileStoringService.uploadFile(pdfOutputStream, filename, "application/pdf");

        return groupRepository.save(group);
    }

    @Override
    public List<Group> getAllGroups() {
        return groupRepository.findAll().stream()
                .filter(group -> !group.isDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public List<Group> searchGroupsByName(String name) {
        return groupRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<Group> searchGroupsByDescription(String description) {
        return groupRepository.findByDescriptionContainingIgnoreCase(description);
    }

    @Override
    public void deleteGroup(Long id) {
        Optional<Group> group = groupRepository.findById(id);
        group.ifPresent(groupRepository::delete);
    }

    @Override
    public List<GroupDocument> searchGroupsByNameOrDescription(String queryText){
        try {
            SearchResponse<GroupDocument> response = elasticsearchClient.search(s -> s
                    .index("groups")
                    .query(q -> q
                            .bool(b -> b
                                    .should(sh -> sh.match(m -> m.field("name").query(queryText)))
                                    .should(sh -> sh.match(m -> m.field("description").query(queryText)))
                            )
                    ), GroupDocument.class);

            return response.hits().hits().stream()
                    .map(hit -> hit.source())
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public GroupDTO getGroupById(Long id) {
        Optional<Group> groupOptional = groupRepository.findById(id);
        if (groupOptional.isEmpty()) {
            throw new RuntimeException("Group not found with id: " + id);
        }

        Group group = groupOptional.get();
        return mapToDTO(group);
    }

    private GroupDTO mapToDTO(Group group) {
        GroupDTO dto = new GroupDTO();
        dto.setId(group.getId());
        dto.setCreatedAt(group.getCreatedAt());
        dto.setName(group.getName());
        dto.setDescription(group.getDescription());
        dto.setAdminId(group.getAdmin().getId());

        Set<PostDTO> postDTOs = group.getPosts()
                .stream()
                .map(this::mapPostToDTO)
                .collect(Collectors.toSet());

        dto.setPosts(postDTOs);
        return dto;
    }

    private PostDTO mapPostToDTO(Post post) {
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
