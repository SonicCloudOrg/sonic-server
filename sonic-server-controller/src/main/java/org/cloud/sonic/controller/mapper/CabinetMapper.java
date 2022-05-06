package org.cloud.sonic.controller.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.cloud.sonic.common.models.domain.Cabinet;

/**
 *  Mapper 接口
 * @author Eason
 * @since 2022-04-26
 */
@Mapper
public interface CabinetMapper extends BaseMapper<Cabinet> {
}
