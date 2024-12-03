package org.cloud.sonic.controller.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.cloud.sonic.controller.models.domain.Roles;

/**
 * Mapper 接口
 *
 * @author yaming116
 * @since 2022-6-2
 */
@Mapper
public interface RolesMapper extends BaseMapper<Roles> {

}
