package org.cloud.sonic.controller.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.cloud.sonic.common.models.domain.Elements;
import org.cloud.sonic.common.models.dto.ElementsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 *  Mapper 接口
 * @author JayWenStar
 * @since 2021-12-17
 */
@Mapper
public interface ElementsMapper extends BaseMapper<Elements> {

    List<ElementsDTO> listElementsByStepsIds(@Param("stepIds") Set<Integer> stepIds);

    @Select("select e.* from steps_elements se " +
                "inner join elements e on se.elements_id = e.id " +
            "where se.steps_id = #{stepId}")
    List<ElementsDTO> listElementsByStepsId(@Param("stepId") Integer stepId);

    @Insert("INSERT INTO elements(ele_name,ele_type,ele_value,project_id) " +
            "SELECT ele_name,ele_type,ele_value,project_id FROM elements WHERE id =${id}")
    void insetById(@Param("id")Integer id);
}
