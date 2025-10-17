package com.urrutia.user_service.repository;

import com.urrutia.user_service.enums.Role;
import com.urrutia.user_service.enums.Status;
import com.urrutia.user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);
    List<User> findByStatus(Status status);

}
