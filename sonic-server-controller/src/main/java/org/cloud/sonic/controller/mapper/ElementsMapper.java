package org.cloud.sonic.controller.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.cloud.sonic.common.models.domain.Elements;
import org.cloud.sonic.common.models.dto.ElementsDTO;

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
    void insetSelectById(@Param("id")Integer id);

    @Select("SELECT steps_elements.elements_id FROM  steps,steps_elements WHERE steps.id = steps_elements.steps_id " +
            "AND steps.case_id = ${id} ;")

    List<Integer>  selectNeedCopyEleId(@Param("id") int id);
    @Select("SELECT id FROM elements WHERE ele_name ='${name}' Order BY id DESC LIMIT 1")
    int selectByEleName(@Param("name") String name );

    @Update("UPDATE elements set ele_name='${name}_copy' WHERE id =${id} ")
    void updateName(@Param("id") Integer id,@Param("name") String name);
}
