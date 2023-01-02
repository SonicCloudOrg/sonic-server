package org.cloud.sonic.controller.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.cloud.sonic.controller.models.domain.RoleResources;

/**
 * Mapper 接口
 *
 * @author yaming116
 * @since 2022-6-2
 */
@Mapper
public interface RoleResourcesMapper extends BaseMapper<RoleResources> {

    @Select("SELECT count(*) from users u " +
            "JOIN resource_roles rr on u.user_role =  rr.role_id and u.user_name = #{userName} " +
            "JOIN resources res on rr.res_id = res.id and res.path = #{path} and res.method = #{method}")
    int checkUserHasResourceAuthorize(@Param("userName") String userName,
                                      @Param("path") String path,
                                      @Param("method") String method);
}
