package com.sonic.control.dao;

import com.sonic.control.models.Projects;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectsRepository extends JpaRepository<Projects, Integer> {
}
