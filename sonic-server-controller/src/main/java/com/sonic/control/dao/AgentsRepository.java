package com.sonic.control.dao;

import com.sonic.control.models.Agents;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ZhouYiXun
 * @des Agent数据库操作
 * @date 2021/8/16 20:29
 */
public interface AgentsRepository extends JpaRepository<Agents, Integer> {
    Agents findByIp(String ip);

    Agents findTopByName(String name);
}
