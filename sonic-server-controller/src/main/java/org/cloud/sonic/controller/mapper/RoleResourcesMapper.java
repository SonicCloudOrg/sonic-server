package org.cloud.sonic.controller.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.cloud.sonic.common.models.domain.Resources;
import org.cloud.sonic.common.models.domain.RoleResources;

/**
 *  Mapper 接口
 * @author yaming116
 * @since 2022-6-2
 */
@Mapper
public interface RoleResourcesMapper extends BaseMapper<RoleResources> {

    int checkUserHasResourceAuthorize(@Param("userName") String userName,
                                      @Param("path") String path,
                                      @Param("method") String method);
}
