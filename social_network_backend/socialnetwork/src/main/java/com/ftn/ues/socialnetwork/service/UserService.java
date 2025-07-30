package com.ftn.ues.socialnetwork.service;

import com.ftn.ues.socialnetwork.contract.UserAdditionDTO;
import com.ftn.ues.socialnetwork.model.User;

import java.util.List;

public interface UserService {

    User addUser(UserAdditionDTO userAdditionDTO);

    void removeUser(Long id);

    List<User> getAllUsers();
}
