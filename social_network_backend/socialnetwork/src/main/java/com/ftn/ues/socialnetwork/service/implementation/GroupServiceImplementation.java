package com.ftn.ues.socialnetwork.service.implementation;

import com.ftn.ues.socialnetwork.contract.GroupDTO;
import com.ftn.ues.socialnetwork.contract.PostDTO;
import com.ftn.ues.socialnetwork.infrastructure.repository.GroupRepository;
import com.ftn.ues.socialnetwork.infrastructure.repository.UserRepository;
import com.ftn.ues.socialnetwork.model.Group;
import com.ftn.ues.socialnetwork.model.Post;
import com.ftn.ues.socialnetwork.service.GroupService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupServiceImplementation implements GroupService {

    final GroupRepository groupRepository;

    final UserRepository userRepository;

    final ModelMapper modelMapper;

    @Autowired
    public GroupServiceImplementation(final GroupRepository groupRepository,
                                      final ModelMapper modelMapper,
                                      final UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    @Override
    public Group addGroup(GroupDTO groupDTO) {
        Group group = new Group();
        group.setName(groupDTO.getName());
        group.setDescription(groupDTO.getDescription());
        group.setAdmin(userRepository.findById(groupDTO.getAdminId()).orElse(null));
        group.setCreatedAt(groupDTO.getCreatedAt());

        Set<Post> posts = new HashSet<>();

        for (PostDTO dto : groupDTO.getPosts()) {
            Post post = modelMapper.map(dto, Post.class);
            posts.add(post);
        }
        for (Post post : posts) {
            post.setGroup(group);
        }
        group.setPosts(posts);

        return groupRepository.save(group);
    }

    @Override
    public List<Group> getAllGroups() {
        return groupRepository.findAll().stream()
                .filter(group -> !group.isDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public void deleteGroup(Long id) {
        Optional<Group> group = groupRepository.findById(id);
        group.ifPresent(groupRepository::delete);
    }
}
