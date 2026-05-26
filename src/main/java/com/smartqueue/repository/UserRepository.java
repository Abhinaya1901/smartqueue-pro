package com.smartqueue.repository;

import com.smartqueue.model.User;
import com.smartqueue.model.enums.Category;
import com.smartqueue.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);
    List<User> findByRoleAndSpecialization(Role role, Category category);
    List<User> findByRoleAndActive(Role role, boolean active);
}