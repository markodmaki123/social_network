package com.ftn.ues.socialnetwork.infrastructure.repository;

import com.ftn.ues.socialnetwork.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {

    List<Post> findByContentContainingIgnoreCase(String keyword);
}
