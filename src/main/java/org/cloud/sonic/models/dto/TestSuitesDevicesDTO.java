package org.cloud.sonic.controller.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.TestSuitesDevices;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author JayWenStar
 * @since 2021-12-17
 */
@Schema(name = "TestSuitesDevicesDTO 对象", description = "")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestSuitesDevicesDTO implements Serializable, TypeConverter<TestSuitesDevicesDTO, TestSuitesDevices> {

    private Integer testSuitesId;

    private Integer devicesId;

    private Integer sort;
}
