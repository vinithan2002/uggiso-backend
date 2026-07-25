package com.uggiso.uggiso_backend.repository;

import com.uggiso.uggiso_backend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailsRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUserName(String userName);

    Optional<Users> findByEmail(String email);

    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);

}