package org.cloud.sonic.controller.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.TestSuitesTestCases;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author JayWenStar
 * @since 2021-12-17
 */
@Schema(name = "TestSuitesTestCasesDTO 对象", description = "")
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
