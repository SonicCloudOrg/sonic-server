package org.cloud.sonic.controller.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.cloud.sonic.common.models.domain.TestCases;

import java.util.List;

/**
 *  Mapper 接口
 * @author JayWenStar
 * @since 2021-12-17
 */
@Mapper
public interface TestCasesMapper extends BaseMapper<TestCases> {

    @Select("select tc.* from test_suites_test_cases tstc " +
            "inner join test_cases tc on tc.id = tstc.test_cases_id " +
            "where tstc.test_suites_id = #{suiteId} " +
            "order by tstc.sort asc")
    List<TestCases> listByTestSuitesId(@Param("suiteId") int suiteId);


    @Insert("INSERT INTO test_cases(des,designer,edit_time,module,name,platform,project_id,version)  " +
            "SELECT des,designer,edit_time,module,name,platform,project_id,version FROM test_cases WHERE id = ${id}")
    void insertTestById(@Param("id")int id);

    @Select("SELECT id FROM test_cases WHERE name ='${name}' Order BY id DESC LIMIT 1")
    Integer testCopyCaseId(@Param("name") String name);

    @Select("SELECT * FROM test_cases WHERE id =${id}")
    TestCases selectById(@Param("id")int id);

    @Update("UPDATE test_cases set name='${name}_copy',edit_time=NOW() WHERE id =${id} ")
    void updateCaseNameAndTimeById(@Param("id") int id,@Param("name") String name);
}
