package com.nhhp.identityservices.repository;

import com.nhhp.identityservices.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    public boolean existsByUsername(String userName);

    Optional<User> findByUsername(String userName);
}
