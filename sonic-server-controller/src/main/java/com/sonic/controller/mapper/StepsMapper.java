package com.sonic.controller.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sonic.controller.models.domain.Steps;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *  Mapper 接口
 * @author JayWenStar
 * @since 2021-12-17
 */
@Mapper
public interface StepsMapper extends BaseMapper<Steps> {

    @Select("select IFNULL(max(sort),0) from steps")
    int findMaxSort();

    @Select("select s.* from steps_elements as se inner join steps as s on se.steps_id = s.id where se.elements_id = 2")
    List<Steps> listStepsByElementId(@Param("elements_id") int elementsId);

    @Select("select s.* from public_steps_steps pss " +
                "inner join steps s on pss.steps_id = s.id " +
            "where pss.public_steps_id = #{publicStepsId}")
    List<Steps> listByPublicStepsId(@Param("publicStepsId") int publicStepsId);

}
