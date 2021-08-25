package com.sonic.controller.dao;

import com.sonic.controller.models.Agents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des Agent数据库操作
 * @date 2021/8/16 20:29
 */
public interface AgentsRepository extends JpaRepository<Agents, Integer> {
    Agents findByIp(String ip);

    @Query(value = "select count(*) from agents where status?=1", nativeQuery = true)
    int findCountByStatus(int status);

    Agents findTopByName(String name);
}
