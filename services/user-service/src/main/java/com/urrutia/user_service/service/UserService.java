package com.urrutia.user_service.service;

import com.urrutia.user_service.dto.LoginResponseDTO;
import com.urrutia.user_service.dto.UserRegistrationDTO;
import com.urrutia.user_service.dto.UserResponseDTO;
import com.urrutia.user_service.enums.Role;
import com.urrutia.user_service.enums.Status;
import com.urrutia.user_service.mapper.UserMapper;
import com.urrutia.user_service.model.User;
import com.urrutia.user_service.repository.UserRepository;
import com.urrutia.user_service.security.JwtTokenUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;


    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder,
                       JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    //login
    public LoginResponseDTO login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found with email: " + email));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalStateException("Invalid credentials");
        }

        // Genera token JWT pasando email y rol
        String token = jwtTokenUtil.generateToken(user.getEmail(), user.getRole().name());

        return new LoginResponseDTO(user.getEmail(), token);
    }


    //register
    public UserResponseDTO registerUser(UserRegistrationDTO registrationDTO) {
        Optional<User> existingUser = userRepository.findByEmail(registrationDTO.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalStateException("Email already registered: " + registrationDTO.getEmail());
        }

        User newUser = new User();
        newUser.setFirstName(registrationDTO.getFirstName());
        newUser.setLastName(registrationDTO.getLastName());
        newUser.setEmail(registrationDTO.getEmail());
        newUser.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

        //rol
        String roleStr = registrationDTO.getRole();
        if (roleStr != null && (roleStr.equalsIgnoreCase("CLIENTE") || roleStr.equalsIgnoreCase("AGENT"))) {
            newUser.setRole(Role.valueOf(roleStr.toUpperCase()));
        } else {
            newUser.setRole(Role.CLIENTE);
        }

        newUser.setStatus(Status.ACTIVE);

        User savedUser = userRepository.save(newUser);
        return userMapper.toResponseDTO(savedUser);
    }

    // crud
    public List<UserResponseDTO> findAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toResponseDTO(users);
    }

    public UserResponseDTO findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("User not found with ID: " + id));
        return userMapper.toResponseDTO(user);
    }

    public UserResponseDTO updateUser(Long id, UserRegistrationDTO registrationDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("User not found with ID: " + id));

        existingUser.setFirstName(registrationDTO.getFirstName());
        existingUser.setLastName(registrationDTO.getLastName());
        existingUser.setEmail(registrationDTO.getEmail());

        //hashea  si contraseña fue enviada
        if (registrationDTO.getPassword() != null && !registrationDTO.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        }

        User updatedUser = userRepository.save(existingUser);
        return userMapper.toResponseDTO(updatedUser);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalStateException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }

    //rol
    public UserResponseDTO updateRole(Long id, String role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("User not found with ID: " + id));

        if (!role.equalsIgnoreCase("ADMIN") &&
                !role.equalsIgnoreCase("CLIENTE") &&
                !role.equalsIgnoreCase("AGENT")) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }

        user.setRole(Role.valueOf(role.toUpperCase()));
        User saved = userRepository.save(user);
        return userMapper.toResponseDTO(saved);
    }

    //busca por rol
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

    //busca por status
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

    //busca por email
    public UserResponseDTO findUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found with email: " + email));
        return userMapper.toResponseDTO(user);
    }
}
