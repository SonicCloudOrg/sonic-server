package org.cloud.sonic.controller.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.cloud.sonic.controller.models.domain.Jobs;

/**
 * Mapper 接口
 *
 * @author JayWenStar
 */
@Mapper
public interface JobsMapper extends BaseMapper<Jobs> {

}
