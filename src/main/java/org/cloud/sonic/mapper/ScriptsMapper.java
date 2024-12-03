package org.cloud.sonic.controller.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.cloud.sonic.controller.models.domain.Scripts;

/**
 * Mapper 接口
 *
 * @author Eason
 */
@Mapper
public interface ScriptsMapper extends BaseMapper<Scripts> {
}
