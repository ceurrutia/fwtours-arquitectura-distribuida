package com.urrutia.user_service.service;

import com.urrutia.user_service.dto.UserRegistrationDTO;
import com.urrutia.user_service.dto.UserResponseDTO;
import com.urrutia.user_service.enums.Role;
import com.urrutia.user_service.enums.Status;
import com.urrutia.user_service.mapper.UserMapper;

import com.urrutia.user_service.model.User;
import com.urrutia.user_service.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }


    //Registro de user
    public UserResponseDTO registerUser(UserRegistrationDTO registrationDTO) {
        Optional<User> existingUser = userRepository.findByEmail(registrationDTO.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalStateException("Email already registered: " + registrationDTO.getEmail());
        }

        User newUser = new User();
        newUser.setFirstName(registrationDTO.getFirstName());
        newUser.setLastName(registrationDTO.getLastName());
        newUser.setEmail(registrationDTO.getEmail());
        newUser.setPassword(registrationDTO.getPassword());

        //validar rol permitido al registrarse
        String roleStr = registrationDTO.getRole();
        if (roleStr != null && (roleStr.equalsIgnoreCase("CLIENTE") || roleStr.equalsIgnoreCase("AGENT"))) {
            newUser.setRole(Role.valueOf(roleStr.toUpperCase()));
        } else {
            //rol por defecto
            newUser.setRole(Role.CLIENTE);
        }

        //status por defecto
        newUser.setStatus(Status.ACTIVE);

        User savedUser = userRepository.save(newUser);
        return userMapper.toResponseDTO(savedUser);
    }


    //busca todos
    public List<UserResponseDTO> findAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toResponseDTO(users);
    }

    //buscapor id
    public UserResponseDTO findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalStateException("User not found with ID: " + id));

        return userMapper.toResponseDTO(user);
    }

    //usuario por rol
    public List<UserResponseDTO> findUsersByRole(String role) {
        Role roleEnum;
        try {
            roleEnum = Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Invalid role: " + role);
        }

        List<User> users = userRepository.findByRole(roleEnum);
        return userMapper.toResponseDTO(users);
    }

    //usuario por status activo o inactivo
    public List<UserResponseDTO> findUsersByStatus(String status) {
        Status statusEnum;
        try {
            statusEnum = Status.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Invalid status: " + status);
        }

        List<User> users = userRepository.findByStatus(statusEnum);
        return userMapper.toResponseDTO(users);
    }

    //update usuario
    public UserResponseDTO updateUser(Long id, UserRegistrationDTO registrationDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("User not found with ID: " + id));

        existingUser.setFirstName(registrationDTO.getFirstName());
        existingUser.setLastName(registrationDTO.getLastName());
        existingUser.setEmail(registrationDTO.getEmail());
        existingUser.setPassword(registrationDTO.getPassword());

        User updatedUser = userRepository.save(existingUser);
        return userMapper.toResponseDTO(updatedUser);
    }

    //elimina usuario
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalStateException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }

    //actualizar el rol usuario solo el admin puede
    public UserResponseDTO updateRole(Long id, String role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("User not found with ID: " + id));

        //solo permite roles válidos
        if (!role.equalsIgnoreCase("ADMIN") &&
                !role.equalsIgnoreCase("CLIENTE") &&
                !role.equalsIgnoreCase("AGENT")) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }

        user.setRole(Role.valueOf(role.toUpperCase()));
        User saved = userRepository.save(user);
        return userMapper.toResponseDTO(saved);
    }

}

