package com.ftn.ues.socialnetwork.controller;

import com.ftn.ues.socialnetwork.contract.*;
import com.ftn.ues.socialnetwork.model.Group;
import com.ftn.ues.socialnetwork.service.GroupService;
import com.ftn.ues.socialnetwork.service.IndexingService;
import com.ftn.ues.socialnetwork.util.SerbianLatinConverter;
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
    final IndexingService indexingService;

    @Autowired
    public GroupController(GroupService groupService, ModelMapper modelMapper,
                           IndexingService indexingService) {
        this.groupService = groupService;
        this.modelMapper = modelMapper;
        this.indexingService = indexingService;
    }

    @PostMapping
    public ResponseEntity<GroupDTO> addGroup(@RequestBody GroupDTO groupDTO) {
        Group group = groupService.addGroup(groupDTO);

        GroupDTO responseDTO = modelMapper.map(group, GroupDTO.class);
        indexingService.indexGroup(responseDTO);

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeGroup(@PathVariable Long id) {
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

    @GetMapping("/{id}")
    public ResponseEntity<GroupDisplayDTO> getById(@PathVariable Long id) {
        GroupDTO group = groupService.getGroupById(id);
        GroupDisplayDTO groupDisplayDTOS = modelMapper.map(group, GroupDisplayDTO.class);
        return ResponseEntity.ok(groupDisplayDTOS);
    }

    @GetMapping("/search-by-name")
    public ResponseEntity<List<GroupDisplayDTO>> searchByName(@RequestParam String name) {
        String normalizedKeyword = SerbianLatinConverter.toLatinLowercase(name);
        List<Group> groups = groupService.searchGroupsByName(normalizedKeyword).stream()
                .filter(group -> !group.isDeleted())
                .toList();
        List<GroupDisplayDTO> groupDisplayDTOS = groups.stream()
                .map(group -> modelMapper.map(group, GroupDisplayDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(groupDisplayDTOS);
    }

    @GetMapping("/search-by-description")
    public ResponseEntity<List<GroupDisplayDTO>> searchByDescription(@RequestParam String desc) {
        String normalizedKeyword = SerbianLatinConverter.toLatinLowercase(desc);
        List<Group> groups = groupService.searchGroupsByDescription(normalizedKeyword).stream()
                .filter(group -> !group.isDeleted())
                .toList();
        List<GroupDisplayDTO> groupDisplayDTOS = groups.stream()
                .map(group -> modelMapper.map(group, GroupDisplayDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(groupDisplayDTOS);
    }

    @GetMapping("/search-content")
    public ResponseEntity<List<GroupDocument>> searchPostsByContent(@RequestParam String keyword){
        String normalizedKeyword = SerbianLatinConverter.toLatinLowercase(keyword);
        List<GroupDocument> groupDocuments = groupService.searchGroupsByNameOrDescription(normalizedKeyword);
        return ResponseEntity.ok(groupDocuments);
    }
}
