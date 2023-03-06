package org.cloud.sonic.controller.models.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlCharsetConstant;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlEngineConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.dto.TestSuitesDevicesDTO;

import java.io.Serializable;

/**
 * @author JayWenStar
 * @since 2021-12-17
 */
@Schema(name ="TestSuitesDevices对象", description = "")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("test_suites_devices")
@TableComment("测试套件 - 设备 关系映射表")
@TableCharset(MySqlCharsetConstant.DEFAULT)
@TableEngine(MySqlEngineConstant.InnoDB)
public class TestSuitesDevices implements Serializable, TypeConverter<TestSuitesDevices, TestSuitesDevicesDTO> {

    @TableField
    @Column(value = "test_suites_id", isNull = false, comment = "测试套件id")
    @Index(value = "idx_test_suites_id_devices_id", columns = {"test_suites_id", "devices_id"})
    private Integer testSuitesId;

    @TableField
    @Column(value = "devices_id", isNull = false, comment = "设备id")
    private Integer devicesId;

    @TableField
    @Column(value = "sort", isNull = false, defaultValue = "0", comment = "排序用")
    private Integer sort;
}
