package com.gambarra.money.api.repository;

import com.gambarra.money.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByEmail(String email);

    public List<User> findByPermissionsDescription(String permissionDescription);

}
