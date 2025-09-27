package com.ftn.ues.socialnetwork.controller;

import com.ftn.ues.socialnetwork.contract.LoginRequest;
import com.ftn.ues.socialnetwork.contract.UserAdditionDTO;
import com.ftn.ues.socialnetwork.contract.UserDTO;
import com.ftn.ues.socialnetwork.model.User;
import com.ftn.ues.socialnetwork.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping(value = "api/user")
public class UserController {

    final UserService userService;
    final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<UserDTO> addUser(@RequestBody UserAdditionDTO userAdditionDTO) {
        User user = userService.addUser(userAdditionDTO);
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return ResponseEntity.ok(userDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeUser(@PathVariable Long id) {
        userService.removeUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAll() {
        List<User> users = userService.getAllUsers().stream()
                .filter(user -> !user.isDeleted())
                .toList();
        List<UserDTO> userDTOS = users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOS);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        UserDTO userDTO = userService.getUserByUsername(loginRequest.getUsername());

        if (userDTO == null || !userDTO.getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Pogresni kredencijali!");
        }

        return ResponseEntity.ok(userDTO);
    }
}
