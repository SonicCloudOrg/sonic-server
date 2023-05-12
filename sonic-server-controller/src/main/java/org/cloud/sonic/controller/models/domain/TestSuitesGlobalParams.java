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
import org.cloud.sonic.controller.models.dto.TestSuitesGlobalParamsDTO;

import java.io.Serializable;

/**
 * @author mmagi
 * @since 2023-03-25
 */
@Schema(name ="TestSuitesGlobalParams对象", description = "")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("test_suites_global_params")
@TableComment("测试套件附加全局参数表")
@TableCharset(MySqlCharsetConstant.DEFAULT)
@TableEngine(MySqlEngineConstant.InnoDB)
public class TestSuitesGlobalParams implements Serializable, TypeConverter<TestSuitesGlobalParams, TestSuitesGlobalParamsDTO> {

    @TableField
    @Column(value = "test_suites_id", isNull = false, comment = "测试套件id")
    @Index(value = "idx_test_suites_id_devices_id", columns = {"test_suites_id", "params_key"})
    private Integer testSuitesId;

    @TableField
    @Column(value = "params_key", isNull = false, comment = "参数key")
    private String paramsKey;

    @TableField
    @Column(value = "params_value", isNull = false, comment = "参数value")
    private String paramsValue;
}
