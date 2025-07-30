package com.ftn.ues.socialnetwork.contract;

import com.ftn.ues.socialnetwork.model.Group;
import com.ftn.ues.socialnetwork.model.Post;
import com.ftn.ues.socialnetwork.model.Reaction;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAdditionDTO {

    Long id;
    String email;
    String password;
    String name;
    String surname;
    LocalDate lastLogin;
    String username;
    String displayName;
    String description;
    Set<PostDTO> posts = new HashSet<>();
    Set<ReactionDTO> reactions = new HashSet<>();
    Set<GroupDTO> administeredGroups = new HashSet<>();
}
