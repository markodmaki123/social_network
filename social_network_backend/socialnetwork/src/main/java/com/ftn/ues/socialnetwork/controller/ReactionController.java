package com.ftn.ues.socialnetwork.controller;

import com.ftn.ues.socialnetwork.contract.PostDTO;
import com.ftn.ues.socialnetwork.contract.ReactionDTO;
import com.ftn.ues.socialnetwork.model.Post;
import com.ftn.ues.socialnetwork.model.Reaction;
import com.ftn.ues.socialnetwork.service.ReactionService;
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
@RequestMapping(value = "api/reaction")
public class ReactionController {

    final ReactionService reactionService;

    final ModelMapper modelMapper;

    @Autowired
    public ReactionController(ReactionService reactionService,
                              ModelMapper modelMapper) {
        this.reactionService = reactionService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<ReactionDTO> addReaction(@RequestBody ReactionDTO reactionDTO) {
        Reaction reaction = reactionService.addReaction(reactionDTO);

        ReactionDTO newReaction = modelMapper.map(reaction, ReactionDTO.class);
        return ResponseEntity.ok(newReaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeReaction(@PathVariable Long id) {
        reactionService.removeReaction(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<ReactionDTO>> getAll() {
        List<Reaction> reactions = reactionService.getAllReactions();
        List<ReactionDTO> reactionDTOS = reactions.stream()
                .map(reaction -> modelMapper.map(reaction, ReactionDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(reactionDTOS);
    }
}
