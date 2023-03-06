package org.cloud.sonic.controller.models.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.sunchenbin.mybatis.actable.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlCharsetConstant;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlEngineConstant;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.dto.TestCasesDTO;

import java.io.Serializable;
import java.util.Date;

/**
 * @author JayWenStar
 * @since 2021-12-17
 */
@Schema(name ="TestCases对象", description = "")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("test_cases")
@TableComment("测试用例表")
@TableCharset(MySqlCharsetConstant.DEFAULT)
@TableEngine(MySqlEngineConstant.InnoDB)
public class TestCases implements Serializable, TypeConverter<TestCases, TestCasesDTO> {

    @TableId(value = "id", type = IdType.AUTO)
    @IsAutoIncrement
    private Integer id;

    @TableField
    @Column(isNull = false, comment = "用例描述")
    private String des;

    @TableField
    @Column(isNull = false, comment = "用例设计人")
    private String designer;

    @Schema(description = "最后修改日期", required = true, example = "2021-08-15 11:10:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Column(value = "edit_time", type = MySqlTypeConstant.DATETIME, isNull = false, comment = "最后修改日期")
    private Date editTime;

    @TableField
    @Column(value = "module_id", isNull = true, comment = "所属模块", defaultValue = "0")
    @Index(value = "IDX_MODULE_ID", columns = {"module_id"})
    private Integer moduleId;

    @TableField
    @Column(isNull = false, comment = "用例名称")
    private String name;

    @TableField
    @Column(isNull = false, comment = "设备系统类型")
    private Integer platform;

    @TableField
    @Column(value = "project_id", isNull = false, comment = "所属项目id")
    @Index(value = "IDX_PROJECT_ID", columns = {"project_id"})
    private Integer projectId;

    @TableField
    @Column(value = "version", isNull = false, comment = "版本号")
    private String version;
}
