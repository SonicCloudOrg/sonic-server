package org.cloud.sonic.controller.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.cloud.sonic.controller.models.domain.StepsElements;

import java.util.List;

/**
 * Mapper 接口
 *
 * @author JayWenStar
 * @since 2021-12-17
 */
@Mapper
public interface StepsElementsMapper extends BaseMapper<StepsElements> {
    @Select("SELECT steps_elements.*  " +
            "FROM steps,steps_elements  " +
            "WHERE steps.id = steps_elements.steps_id AND " +
            " steps.case_id =#{caseId}")
    List<StepsElements> selectCopyElements(@Param("caseId") int caseId);


}
