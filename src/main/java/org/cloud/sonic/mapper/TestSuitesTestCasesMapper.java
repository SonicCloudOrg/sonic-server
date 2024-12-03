package org.cloud.sonic.controller.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.cloud.sonic.controller.models.domain.TestSuites;
import org.cloud.sonic.controller.models.domain.TestSuitesTestCases;

import java.util.List;

/**
 * Mapper 接口
 *
 * @author JayWenStar
 * @since 2021-12-17
 */
@Mapper
public interface TestSuitesTestCasesMapper extends BaseMapper<TestSuitesTestCases> {

    @Select("select ts.* from test_suites_test_cases tetc inner join test_suites ts on tetc.test_suites_id = ts.id where ts.id = #{testCasesId}")
    List<TestSuites> listTestSuitesByTestCasesId(@Param("testCasesId") int testCasesId);

}
