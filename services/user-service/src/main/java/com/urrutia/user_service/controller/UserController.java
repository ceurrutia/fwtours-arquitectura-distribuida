package com.urrutia.user_service.controller;

import com.urrutia.user_service.dto.UserRegistrationDTO;
import com.urrutia.user_service.dto.UserResponseDTO;
import com.urrutia.user_service.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //crear usuario
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody UserRegistrationDTO registrationDTO) {
        UserResponseDTO newUser = userService.registerUser(registrationDTO);
        return ResponseEntity.ok(newUser);
    }

    //listar todos
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    //buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        UserResponseDTO user = userService.findUserById(id);
        return ResponseEntity.ok(user);
    }

    //update usuario
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @RequestBody UserRegistrationDTO registrationDTO) {

        UserResponseDTO updatedUser = userService.updateUser(id, registrationDTO);
        return ResponseEntity.ok(updatedUser);
    }

    //eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Usuario eliminado con éxito.");
    }

    //rol de usurio por admin solamente
    @PutMapping("/{id}/role")
    public ResponseEntity<UserResponseDTO> updateUserRole(
            @PathVariable Long id,
            @RequestParam String role) {

        UserResponseDTO updatedUser = userService.updateRole(id, role);
        return ResponseEntity.ok(updatedUser);
    }
}
