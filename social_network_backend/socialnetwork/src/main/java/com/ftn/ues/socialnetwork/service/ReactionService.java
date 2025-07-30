package com.ftn.ues.socialnetwork.service;

import com.ftn.ues.socialnetwork.contract.ReactionDTO;
import com.ftn.ues.socialnetwork.model.Reaction;

import java.util.List;

public interface ReactionService {

    Reaction addReaction(ReactionDTO reactionDTO);

    List<Reaction> getAllReactions();

    void removeReaction(Long reactionId);
}
