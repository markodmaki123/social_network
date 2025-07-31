package com.ftn.ues.socialnetwork.service;

import com.ftn.ues.socialnetwork.contract.PostDTO;
import com.ftn.ues.socialnetwork.contract.PostDisplayDTO;
import com.ftn.ues.socialnetwork.contract.PostDocument;
import com.ftn.ues.socialnetwork.model.Post;

import java.util.List;

public interface PostService {

    Post addPost(PostDTO postDTO);

    void deletePost(Long postId);

    List<Post> getAllPosts();

    List<Post> searchPostsByContent(String keyword);

    List<PostDocument> searchPostsByText(String queryText);

    List<PostDTO> getPostsGroup(Long id);

    PostDTO getPost(Long id);
}
