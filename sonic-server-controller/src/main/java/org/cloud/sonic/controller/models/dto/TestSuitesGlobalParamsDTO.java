package org.cloud.sonic.controller.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.TestSuitesGlobalParams;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author mmagi
 * @since 2023-03-25
 */
@Schema(name = "TestSuitesGlobalParamsDTO 对象", description = "")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestSuitesGlobalParamsDTO implements Serializable, TypeConverter<TestSuitesGlobalParamsDTO, TestSuitesGlobalParams> {

    private Integer testSuitesId;

    private String paramsKey;

    private String paramsValue;
}

