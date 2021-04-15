package com.example.userservice.user;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUserId(String userId);

    User findByEmail(String username);
}