package com.ftn.ues.socialnetwork.service;

import com.ftn.ues.socialnetwork.contract.GroupDTO;
import com.ftn.ues.socialnetwork.contract.PostDTO;
import com.ftn.ues.socialnetwork.model.Group;
import com.ftn.ues.socialnetwork.model.Post;

public interface IndexingService {

    public void indexPost(PostDTO post);

    public void indexGroup(GroupDTO group);
}
