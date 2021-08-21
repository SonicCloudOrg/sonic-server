package com.sonic.control.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des Package数据库操作
 * @date 2021/8/16 20:32
 */
public interface PackagesRepository extends JpaRepository<Packages, Integer> {
    Page<Packages> findByProjectIdAndPlatform(int projectId, int platform, Pageable pageable);

    List<Packages> findByProjectIdAndPlatformAndBranchNameAndIsOpen(
            int projectId, int platform, String branchName, int isOpen);

    Packages findTopByProjectIdAndPlatformAndIsOpenOrderByRunNumAsc(int projectId, int platform, int isOpen);

    List<Packages> findByProjectIdAndPlatformAndIsOpen(int projectId, int platform, int isOpen);
}
