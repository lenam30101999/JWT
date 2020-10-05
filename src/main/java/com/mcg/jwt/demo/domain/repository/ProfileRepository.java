package com.mcg.jwt.demo.domain.repository;

import com.mcg.jwt.demo.domain.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    Profile save(Profile profile);

    Profile findById(int id);
}
