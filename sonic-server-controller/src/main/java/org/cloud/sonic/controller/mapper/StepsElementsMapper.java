package org.cloud.sonic.controller.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.cloud.sonic.common.models.domain.StepsElements;
import org.apache.ibatis.annotations.Mapper;

/**
 *  Mapper 接口
 * @author JayWenStar
 * @since 2021-12-17
 */
@Mapper
public interface StepsElementsMapper extends BaseMapper<StepsElements> {
    @Insert("INSERT into steps_elements(steps_id,elements_id) VALUES (${stepsId}, 0)")
    void insertByStepsId(@Param("stepsId")int stepsId);

    @Update("UPDATE steps_elements set elements_id = ${element_id} WHERE steps_id = ${steps_id}")
    void updateElementById(@Param("element_id") int elementId,@Param("steps_id")int stepsId);

}
