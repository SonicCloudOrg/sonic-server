package org.cloud.sonic.controller.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.cloud.sonic.controller.models.domain.Projects;

import java.util.List;

/**
 * Mapper 接口
 *
 * @author JayWenStar
 * @since 2021-12-17
 */
@Mapper
public interface ProjectsMapper extends BaseMapper<Projects> {

    @Select("select p.* from projects p inner join users_projects up where p.id = up.project_id and up.user_id =#{userId}")
    List<Projects> listByProjects(@Param("userId") int userId);

}
