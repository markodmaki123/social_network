package com.ftn.ues.socialnetwork.controller;

import com.ftn.ues.socialnetwork.contract.GroupDTO;
import com.ftn.ues.socialnetwork.contract.GroupDisplayDTO;
import com.ftn.ues.socialnetwork.model.Group;
import com.ftn.ues.socialnetwork.service.GroupService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping(value = "api/group")
public class GroupController {

    final GroupService groupService;
    final ModelMapper modelMapper;

    @Autowired
    public GroupController(GroupService groupService, ModelMapper modelMapper) {
        this.groupService = groupService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<Group> addFacility(@RequestBody GroupDTO groupDTO) {
        Group group = groupService.addGroup(groupDTO);
        return ResponseEntity.ok(group);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeFacility(@PathVariable Long id) {
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<GroupDisplayDTO>> getAll() {
        List<Group> groups = groupService.getAllGroups().stream()
                .filter(group -> !group.isDeleted())
                .toList();
        List<GroupDisplayDTO> groupDisplayDTOS = groups.stream()
                .map(group -> modelMapper.map(group, GroupDisplayDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(groupDisplayDTOS);
    }
}
