package com.urrutia.user_service.mapper;

import com.urrutia.user_service.dto.UserResponseDTO;
import com.urrutia.user_service.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserResponseDTO toResponseDTO(User user) {
        if (user == null) {
            return null;
        }

        String roleAsString = user.getRole() != null ? user.getRole().name() : null;
        String statusAsString = user.getStatus() != null ? user.getStatus().name() : null;

        return new UserResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                roleAsString,
                statusAsString
        );
    }

    public List<UserResponseDTO> toResponseDTO(List<User> users) {
        if (users == null) {
            return List.of();
        }

        return users.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}
