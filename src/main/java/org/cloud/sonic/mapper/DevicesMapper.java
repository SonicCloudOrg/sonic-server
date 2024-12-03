package org.cloud.sonic.controller.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.cloud.sonic.controller.models.domain.Devices;

import java.util.List;

/**
 * Mapper 接口
 *
 * @author JayWenStar
 * @since 2021-12-17
 */
@Mapper
public interface DevicesMapper extends BaseMapper<Devices> {

    @Select("select cpu from devices group by cpu")
    List<String> findCpuList();

    @Select("select size from devices group by size")
    List<String> findSizeList();

    @Select("select d.* from test_suites_devices tsd " +
            "inner join devices d on d.id = tsd.devices_id " +
            "where tsd.test_suites_id = #{TestSuitesId} " +
            "order by tsd.sort asc")
    List<Devices> listByTestSuitesId(@Param("TestSuitesId") int TestSuitesId);
}
