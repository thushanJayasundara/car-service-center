package com.automiraj.repository;


import com.automiraj.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findById (Long id);

    boolean existsById(String id);

    User findByUserName(String userName);
}
