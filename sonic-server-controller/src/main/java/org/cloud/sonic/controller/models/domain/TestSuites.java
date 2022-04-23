package org.cloud.sonic.controller.models.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlCharsetConstant;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlEngineConstant;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.dto.TestSuitesDTO;
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
@ApiModel(value = "TestSuites对象", description = "")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("test_suites")
@TableComment("测试套件表")
@TableCharset(MySqlCharsetConstant.DEFAULT)
@TableEngine(MySqlEngineConstant.InnoDB)
public class TestSuites implements Serializable, TypeConverter<TestSuites, TestSuitesDTO> {

    @TableId(value = "id", type = IdType.AUTO)
    @IsAutoIncrement
    private Integer id;

    @TableField
    @Column(isNull = false, comment = "覆盖类型")
    private Integer cover;

    @TableField
    @Column(isNull = false, comment = "测试套件名字")
    private String name;

    @TableField
    @Column(isNull = false, comment = "测试套件系统类型（android、ios...）")
    private Integer platform;

    @TableField
    @Column(value = "project_id", isNull = false, comment = "覆盖类型")
    @Index(value = "IDX_PROJECT_ID", columns = {"project_id"})
    private Integer projectId;
}
