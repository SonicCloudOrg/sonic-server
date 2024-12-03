package org.cloud.sonic.controller.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.cloud.sonic.controller.models.domain.PublicSteps;
import org.cloud.sonic.controller.models.dto.StepsDTO;

import java.util.Collection;
import java.util.List;

/**
 * Mapper 接口
 *
 * @author JayWenStar
 * @since 2021-12-17
 */
@Mapper
public interface PublicStepsMapper extends BaseMapper<PublicSteps> {

    @Select("<script>" +
            "select pss.public_steps_id, s.* from public_steps_steps pss\n" +
            "inner join steps s on s.id = pss.steps_id\n" +
            "where public_steps_id in\n" +
            "<foreach collection='publicStepsIds' item='item' index='index' open='(' close=')' separator=','>#{item}</foreach>\n" +
            "order by pss.public_steps_id asc" +
            "</script>")
    List<StepsDTO> listStepsByPublicStepsIds(@Param("publicStepsIds") Collection<Integer> publicStepsIds);

    @Select("select ps.* from public_steps_steps pss " +
            "inner join public_steps ps on ps.id = pss.public_steps_id " +
            "where pss.steps_id = #{stepId}")
    List<PublicSteps> listPubStepsByStepId(@Param("stepId") int stepId);

}
