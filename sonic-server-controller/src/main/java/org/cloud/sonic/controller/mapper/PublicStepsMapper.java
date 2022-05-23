package org.cloud.sonic.controller.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.cloud.sonic.common.models.domain.PublicSteps;
import org.cloud.sonic.common.models.dto.StepsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *  Mapper 接口
 * @author JayWenStar
 * @since 2021-12-17
 */
@Mapper
public interface PublicStepsMapper extends BaseMapper<PublicSteps> {

    List<StepsDTO> listStepsByPublicStepsIds(@Param("publicStepsIds") Collection<Integer> publicStepsIds);

    @Select("select ps.* from public_steps_steps pss " +
                "inner join public_steps ps on ps.id = pss.public_steps_id " +
            "where pss.steps_id = #{stepId}")
    List<PublicSteps> listPubStepsByStepId(@Param("stepId") int stepId);

    @Select("select id,name from public_steps where project_id=#{projectId} and platform=#{platform} order by id desc")
    List<Map<Integer, String>> findByProjectIdAndPlatform(@Param("projectId") int projectId, @Param("platform") int platform);

    /**
     * 查询公共步骤
     * @param id
     * @return
     */
    @Select("select * from public_steps where id =${id}")
    PublicSteps selectPublicSteps(@Param("id") Integer id);

    //查询公共步骤最后一个（ID最大值）
    @Select("select Max(id) from public_steps ")
    Integer selectLastPublicSteps();



    /**
     * 插入公共步骤
     * @param name
     * @param platForm
     * @param projectId
     */
    @Insert("Insert INTO public_steps(name,platform,project_id) VALUES('${name}_copy',${platform},${project_id}) ")
    Integer InsertPublicSteps(@Param("name")String name,
                              @Param("platform") int platForm,@Param("project_id") int projectId);
}
