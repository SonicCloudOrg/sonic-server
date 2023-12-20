package org.cloud.sonic.controller.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.cloud.sonic.controller.models.domain.TestCases;

import java.util.List;

/**
 * Mapper 接口
 *
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

    @Select("select DISTINCT designer from test_cases WHERE project_id= #{projectId} and platform= #{platform}")
    List<String> listAllTestCaseAuthor(@Param("projectId") int projectId, @Param("platform") int platform);
}
