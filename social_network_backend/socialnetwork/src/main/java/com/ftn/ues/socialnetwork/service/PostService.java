package com.ftn.ues.socialnetwork.service;

import com.ftn.ues.socialnetwork.contract.PostDTO;
import com.ftn.ues.socialnetwork.model.Post;

import java.util.List;

public interface PostService {

    Post addPost(PostDTO postDTO);

    void deletePost(Long postId);

    List<Post> getAllPosts();
}
