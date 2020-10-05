package com.mcg.jwt.demo.domain.repository;

import com.mcg.jwt.demo.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User save(User user);

    boolean existsUserByEmail(String email);

    Optional<User> findByEmail(String email);

    User findById(int id);
}
