package com.sonic.control.dao;

import com.sonic.control.models.Versions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface VersionsRepository extends JpaRepository<Versions,Integer> {
    List<Versions> findByProjectId(int projectId);

    @Transactional
    void deleteByProjectId(int projectId);
}
