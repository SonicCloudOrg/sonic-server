package org.cloud.sonic.controller.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.cloud.sonic.common.models.domain.PublicStepsSteps;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 *  Mapper 接口
 * @author JayWenStar
 * @since 2021-12-17
 */
@Mapper
public interface PublicStepsStepsMapper extends BaseMapper<PublicStepsSteps> {

    /**
     * 插入公共步骤关联的Id
     * @param publicStepsId
     * @param stepsId
     */
    @Insert("INSERT INTO public_steps_steps (public_steps_id,steps_id) VALUES (${public_steps_id},${steps_id})")
    Integer InsertPublicStepsSteps(@Param("public_steps_id")Integer publicStepsId, @Param("steps_id")Integer stepsId);

    /**
     *查找公共步骤的子步骤
     * @param publicStepsId
     * @return
     */
    @Select("SELECT steps_id FROM public_steps_steps WHERE public_steps_id = ${public_steps_id}")
    List<Integer> selectPublicStepsStepsId(@Param("public_steps_id")Integer publicStepsId);

}
