package org.cloud.sonic.common.models.dto;

import org.cloud.sonic.common.models.base.TypeConverter;
import org.cloud.sonic.common.models.domain.TestSuitesTestCases;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author JayWenStar
 * @since 2021-12-17
 */
@ApiModel(value = "TestSuitesTestCasesDTO 对象", description = "")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestSuitesTestCasesDTO implements Serializable, TypeConverter<TestSuitesTestCasesDTO, TestSuitesTestCases> {

    private Integer testSuitesId;

    private Integer testCasesId;

    private Integer sort;
}
