package org.cloud.sonic.controller.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.cloud.sonic.controller.models.domain.Steps;

import java.util.List;

/**
 * Mapper 接口
 *
 * @author JayWenStar
 * @since 2021-12-17
 */
@Mapper
public interface StepsMapper extends BaseMapper<Steps> {

    @Select("select IFNULL(max(sort),0) from steps")
    int findMaxSort();

    @Select("select s.* from steps_elements as se inner join steps as s on se.steps_id = s.id where se.elements_id = #{elementsId}")
    List<Steps> listStepsByElementId(@Param("elementsId") int elementsId);

    @Select("select s.* from public_steps_steps pss " +
            "inner join steps s on pss.steps_id = s.id " +
            "where pss.public_steps_id = #{publicStepsId}")
    List<Steps> listByPublicStepsId(@Param("publicStepsId") int publicStepsId);


    @Select("SELECT t.* FROM(" +
            "SELECT steps.*  FROM  " +
            "steps," +
            "steps_elements," +
            "elements " +
            "WHERE " +
            "steps.id = steps_elements.steps_id  " +
            "AND steps_elements.elements_id = elements.id  " +
            "AND elements.ele_name LIKE concat('%',#{ele_name}, '%')  " +
            "union  " +
            "SELECT * FROM steps WHERE content LIKE concat('%',#{ele_name}, '%')) t ORDER BY id DESC")
    IPage<Steps> searchByEleName(Page<?> page, @Param("ele_name") String eleName);


}
