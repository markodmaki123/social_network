package com.ftn.ues.socialnetwork.service.implementation;

import com.ftn.ues.socialnetwork.contract.*;
import com.ftn.ues.socialnetwork.infrastructure.repository.UserRepository;
import com.ftn.ues.socialnetwork.model.Group;
import com.ftn.ues.socialnetwork.model.Post;
import com.ftn.ues.socialnetwork.model.Reaction;
import com.ftn.ues.socialnetwork.model.User;
import com.ftn.ues.socialnetwork.service.UserService;
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
public class UserServiceImplementation implements UserService {

    final UserRepository userRepository;

    final ModelMapper modelMapper;

    @Autowired
    public UserServiceImplementation(final UserRepository userRepository,
                                     final ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public User addUser(UserAdditionDTO userAdditionDTO) {
        User user = new User();
        user.setName(userAdditionDTO.getName());
        user.setPassword(userAdditionDTO.getPassword());
        user.setEmail(userAdditionDTO.getEmail());
        user.setDescription(userAdditionDTO.getDescription());
        user.setUsername(userAdditionDTO.getUsername());
        user.setLastLogin(userAdditionDTO.getLastLogin());
        user.setDisplayName(user.getDisplayName());
        user.setSurname(userAdditionDTO.getSurname());

        Set<Post> posts = new HashSet<>();

        for (PostDTO dto : userAdditionDTO.getPosts()) {
            Post post = modelMapper.map(dto, Post.class);
            posts.add(post);
        }
        for (Post post : posts) {
            post.setUser(user);
        }
        user.setPosts(posts);

        Set<Reaction> reactions = new HashSet<>();

        for (ReactionDTO dto : userAdditionDTO.getReactions()) {
            Reaction reaction = modelMapper.map(dto, Reaction.class);
            reactions.add(reaction);
        }
        for (Reaction reaction : reactions) {
            reaction.setUser(user);
        }
        user.setReactions(reactions);

        Set<Group> groups = new HashSet<>();

        for (GroupDTO dto : userAdditionDTO.getAdministeredGroups()) {
            Group group = modelMapper.map(dto, Group.class);
            groups.add(group);
        }
        for (Group group : groups) {
            group.setAdmin(user);
        }
        user.setAdministeredGroups(groups);

        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll().stream()
                .filter(user -> !user.isDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);

        return user.map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronadjen: " + username));
    }

    private UserDTO mapToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setDescription(user.getDescription());
        dto.setUsername(user.getUsername());
        dto.setLastLogin(user.getLastLogin());
        dto.setDisplayName(user.getDisplayName());
        return dto;
    }

    @Override
    public void removeUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        user.ifPresent(userRepository::delete);
    }
}
