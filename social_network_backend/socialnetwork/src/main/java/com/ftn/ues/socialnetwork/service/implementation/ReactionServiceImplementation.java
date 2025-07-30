package com.ftn.ues.socialnetwork.service.implementation;

import com.ftn.ues.socialnetwork.contract.ReactionDTO;
import com.ftn.ues.socialnetwork.infrastructure.repository.ReactionRepository;
import com.ftn.ues.socialnetwork.infrastructure.repository.UserRepository;
import com.ftn.ues.socialnetwork.model.Post;
import com.ftn.ues.socialnetwork.model.Reaction;
import com.ftn.ues.socialnetwork.model.User;
import com.ftn.ues.socialnetwork.service.ReactionService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReactionServiceImplementation implements ReactionService {

    final ReactionRepository reactionRepository;

    final UserRepository userRepository;

    @Autowired
    public ReactionServiceImplementation(final ReactionRepository reactionRepository,
                                         final UserRepository userRepository) {
        this.reactionRepository = reactionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Reaction addReaction(ReactionDTO reactionDTO) {
        Reaction reaction = new Reaction();
        reaction.setReactionType(reactionDTO.getReactionType());
        reaction.setCreatedAt(reactionDTO.getCreatedAt());

        User user = userRepository.findById(reactionDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        reaction.setUser(user);

        return reactionRepository.save(reaction);
    }

    @Override
    public void removeReaction(Long reactionId) {
        Optional<Reaction> reaction = reactionRepository.findById(reactionId);
        reaction.ifPresent(reactionRepository::delete);
    }

    @Override
    public List<Reaction> getAllReactions() {
        return reactionRepository.findAll().stream()
                .filter(reaction -> !reaction.isDeleted())
                .collect(Collectors.toList());
    }
}
