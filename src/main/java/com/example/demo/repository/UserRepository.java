package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User,Integer> {

   // @Query(value="SELECT * FROM user WHERE username = ?1", nativeQuery = true)
    User findByUsername(String username);
}
