package com.ftn.ues.socialnetwork.controller;

import com.ftn.ues.socialnetwork.contract.PostDTO;
import com.ftn.ues.socialnetwork.contract.ReactionDTO;
import com.ftn.ues.socialnetwork.model.Post;
import com.ftn.ues.socialnetwork.model.Reaction;
import com.ftn.ues.socialnetwork.service.ReactionService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping(value = "api/reaction")
public class ReactionController {

    final ReactionService reactionService;

    @Autowired
    public ReactionController(ReactionService reactionService) {
        this.reactionService = reactionService;
    }

    @PostMapping
    public ResponseEntity<Reaction> addFacility(@RequestBody ReactionDTO reactionDTO) {
        Reaction reaction = reactionService.addReaction(reactionDTO);
        return ResponseEntity.ok(reaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeFacility(@PathVariable Long id) {
        reactionService.removeReaction(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Reaction>> getAll() {
        List<Reaction> reactions = reactionService.getAllReactions();
        return ResponseEntity.ok(reactions);
    }
}
