package com.lec.spring.repository;

import com.lec.spring.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    User findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<User> findOneWithAuthoritiesByUsername(String username);


}
