package org.cloud.sonic.controller.models.dto;

import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.TestSuitesDevices;
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
@ApiModel(value = "TestSuitesDevicesDTO 对象", description = "")
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
