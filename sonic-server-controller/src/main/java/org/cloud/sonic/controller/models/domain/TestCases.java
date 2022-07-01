package org.cloud.sonic.controller.models.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.dto.TestCasesDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author JayWenStar
 * @since 2021-12-17
 */
@ApiModel(value = "TestCases对象", description = "")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("test_cases")
public class TestCases implements Serializable, TypeConverter<TestCases, TestCasesDTO> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField
    private String des;

    @TableField
    private String designer;

    @ApiModelProperty(value = "最后修改日期", required = true, example = "2021-08-15 11:10:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date editTime;

    @TableField
    private String module;

    @TableField
    private String name;

    @TableField
    private Integer platform;

    @TableField
    private Integer projectId;

    @TableField
    private String version;
}
