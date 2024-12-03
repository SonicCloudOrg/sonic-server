package org.cloud.sonic.controller.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.cloud.sonic.controller.models.domain.Resources;

/**
 * Mapper 接口
 *
 * @author yaming116
 * @since 2022-5-31
 */
@Mapper
public interface ResourcesMapper extends BaseMapper<Resources> {

}
