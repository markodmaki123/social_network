package com.ftn.ues.socialnetwork.infrastructure.repository;

import com.ftn.ues.socialnetwork.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group,Long> {

    List<Group> findByNameContainingIgnoreCase(String name);
    List<Group> findByDescriptionContainingIgnoreCase(String description);
}
