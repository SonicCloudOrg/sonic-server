package org.cloud.sonic.controller.models.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
//import com.gitee.sunchenbin.mybatis.actable.annotation.*;
//import com.gitee.sunchenbin.mybatis.actable.constants.MySqlCharsetConstant;
//import com.gitee.sunchenbin.mybatis.actable.constants.MySqlEngineConstant;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.dto.TestSuitesDevicesDTO;
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
@ApiModel(value = "TestSuitesDevices对象", description = "")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("test_suites_devices")
public class TestSuitesDevices implements Serializable, TypeConverter<TestSuitesDevices, TestSuitesDevicesDTO> {

    @TableField
    private Integer testSuitesId;

    @TableField
    private Integer devicesId;

    @TableField
    private Integer sort;
}
