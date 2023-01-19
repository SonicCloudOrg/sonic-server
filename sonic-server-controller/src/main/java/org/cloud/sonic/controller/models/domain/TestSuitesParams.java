package org.cloud.sonic.controller.models.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlCharsetConstant;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlEngineConstant;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.dto.GlobalParamsDTO;
import org.cloud.sonic.controller.models.dto.TestSuitesParamsDTO;

import java.io.Serializable;

/**
 * @author JayWenStar
 * @since 2021-12-17
 */
@ApiModel(value = "TestSuiteParams对象", description = "")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("test_suites_params")
@TableComment("套件参数表")
@TableCharset(MySqlCharsetConstant.DEFAULT)
@TableEngine(MySqlEngineConstant.InnoDB)
public class TestSuitesParams implements Serializable, TypeConverter<TestSuitesParams, TestSuitesParamsDTO> {

    @TableId(value = "id", type = IdType.AUTO)
    @IsAutoIncrement
    private Integer id;

    @TableField
    @Column(value = "params_key", isNull = false, comment = "参数key")
    private String paramsKey;

    @TableField
    @Column(value = "params_value", isNull = false, comment = "参数value")
    private String paramsValue;

    @TableField
    @Column(value = "suite_id", isNull = false, comment = "所属套件id")
    @Index(value = "IDX_PROJECT_ID", columns = {"suite_id"})
    private Integer suiteId;
}
