package com.sonic.controller.dao;

import com.sonic.controller.models.Projects;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectsRepository extends JpaRepository<Projects, Integer> {
}
