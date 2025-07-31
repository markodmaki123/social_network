package com.ftn.ues.socialnetwork.infrastructure.repository;

import com.ftn.ues.socialnetwork.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUsername(String username);
}
