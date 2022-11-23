package org.cloud.sonic.controller.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.cloud.sonic.controller.models.domain.Jobs;

import java.util.List;

/**
 *  Mapper 接口
 * @author JayWenStar
 */
@Mapper
public interface JobsMapper extends BaseMapper<Jobs> {
    @Select("select * from jobs where type = #{type}")
    Jobs findByType(@Param("type") String type);
}
