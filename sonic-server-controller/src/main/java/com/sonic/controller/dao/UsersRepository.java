package com.sonic.controller.dao;

import com.sonic.controller.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Integer> {
    Users findByUserName(String username);
}
