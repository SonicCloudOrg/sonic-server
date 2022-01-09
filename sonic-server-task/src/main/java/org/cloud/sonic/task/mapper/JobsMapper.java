package org.cloud.sonic.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.cloud.sonic.task.models.domain.Jobs;
import org.apache.ibatis.annotations.Mapper;

/**
 *  Mapper 接口
 * @author JayWenStar
 */
@Mapper
public interface JobsMapper extends BaseMapper<Jobs> {

}
