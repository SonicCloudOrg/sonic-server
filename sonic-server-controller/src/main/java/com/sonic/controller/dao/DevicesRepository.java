package com.sonic.controller.dao;

import com.sonic.controller.models.Devices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des Devices数据库操作
 * @date 2021/8/16 20:29
 */
public interface DevicesRepository extends JpaRepository<Devices, Integer>, JpaSpecificationExecutor<Devices> {
    Devices findByAgentIdAndUdId(int agentId, String udId);

    List<Devices> findByAgentId(int agentId);

    List<Devices> findByIdIn(List<Integer> ids);

    List<Devices> findByPlatformOrderByIdDesc(int platform);

    @Query(value = "select cpu from devices group by cpu", nativeQuery = true)
    List<String> findCpuList();

    @Query(value = "select size from devices group by size", nativeQuery = true)
    List<String> findSizeList();
}
