package com.ftn.ues.socialnetwork.service;

import com.ftn.ues.socialnetwork.contract.GroupDTO;
import com.ftn.ues.socialnetwork.model.Group;

import java.util.List;

public interface GroupService {

    Group addGroup(GroupDTO groupDTO);

    void deleteGroup(Long Id);

    List<Group> getAllGroups();
}
