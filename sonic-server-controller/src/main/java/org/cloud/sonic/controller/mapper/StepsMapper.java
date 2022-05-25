package org.cloud.sonic.controller.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;
import org.cloud.sonic.common.models.domain.Steps;

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
            "AND elements.ele_name LIKE '%${ele_name}%'  " +
            "union  " +
            "SELECT * FROM steps WHERE content LIKE '%${ele_name}%') t ORDER BY id DESC")
    IPage<Steps> sreachByEleName(Page<?> page , @Param("ele_name") String eleName);

    @Select("SELECT max(id) from steps")
    int maxStepId();

    @Insert("INSERT INTO steps( `parent_id`, `case_id`, `content`, `error`, `platform`, `project_id`, `sort`, `step_type`, `text`, `condition_type`) \n" +
            "SELECT  `parent_id`, `case_id`, `content`, `error`, `platform`, `project_id`, `sort`, `step_type`, `text`, `condition_type`\n" +
            "FROM steps WHERE case_id = ${id}")
    void insertCopyCaseSteps(@Param("id") int id);

    @Select("select id from steps where case_id = ${id}")
    List<Integer> selectCopyCaseIdList(@Param("id") int id);

    @Update("UPDATE steps set case_id = ${case_id} where id = ${id}")
    void updateCopyStepCaseId(@Param("case_id")Integer caseId,@Param("id") Integer stepsId);

    @Select("select * FROM steps WHERE  content = '' AND case_id = ${id}")
    List<Integer> selectNeedInsertElementsSteps(@Param("id")int Id);

}
