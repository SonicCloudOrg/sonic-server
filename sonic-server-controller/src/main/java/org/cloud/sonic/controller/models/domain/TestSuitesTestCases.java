package org.cloud.sonic.controller.models.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.dto.TestSuitesTestCasesDTO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author JayWenStar
 * @since 2021-12-17
 */
@ApiModel(value = "TestSuitesTestCases对象", description = "")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("test_suites_test_cases")
public class TestSuitesTestCases implements Serializable, TypeConverter<TestSuitesTestCases, TestSuitesTestCasesDTO> {

    @TableField
    private Integer testSuitesId;

    @TableField
    private Integer testCasesId;

    @TableField
    private Integer sort;
}
