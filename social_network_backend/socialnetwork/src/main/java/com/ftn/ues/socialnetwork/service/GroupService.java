package com.ftn.ues.socialnetwork.service;

import com.ftn.ues.socialnetwork.contract.GroupDTO;
import com.ftn.ues.socialnetwork.contract.GroupDocument;
import com.ftn.ues.socialnetwork.model.Group;

import java.util.List;

public interface GroupService {

    Group addGroup(GroupDTO groupDTO);

    void deleteGroup(Long Id);

    List<Group> getAllGroups();

    List<Group> searchGroupsByName(String name);

    List<Group> searchGroupsByDescription(String description);

    List<GroupDocument> searchGroupsByNameOrDescription(String queryText);

    GroupDTO getGroupById(Long Id);
}
