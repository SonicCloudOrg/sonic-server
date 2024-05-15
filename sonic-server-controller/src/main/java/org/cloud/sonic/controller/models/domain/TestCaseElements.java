package org.cloud.sonic.controller.models.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
import org.cloud.sonic.controller.models.dto.TestCaseElementsDTO;

import java.io.Serializable;

/**
 * @author JayWenStar
 * @since 2021-12-17
 */
@Schema(name ="Elements对象", description = "")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("testcase_elements")
@TableComment("用例控件元素关系表")
@TableCharset(MySqlCharsetConstant.DEFAULT)
@TableEngine(MySqlEngineConstant.InnoDB)
public class TestCaseElements implements Serializable, TypeConverter<TestCaseElements, TestCaseElementsDTO> {

    @TableId(value = "id", type = IdType.AUTO)
    @IsAutoIncrement
    private Integer id;

    @TableField
    @Column(value = "testcase_id", isNull = false, comment = "关联用例id")
    private Integer testcaseId;

    @TableField
    @Column(value = "step_id", isNull = false, comment = "关联步骤id")
    private Integer stepId;

    @TableField
    @Column(value = "element_id",  isNull = false, comment = "关联控件id")
    private Integer elementId;

    @TableField
    @Column(value = "ele_paramkey", isNull = true, comment = "用例控件参数")
    private String eleParamkey;

    @TableField
    @Column(value = "ele_paramvalue", isNull = true, comment = "用例控件参数值")
    private String eleParamvalue;

    @TableField
    @Column(value = "ele_value", isNull = true, comment = "用例控件值")
    private String eleValue;

    @TableField
    @Column(value = "ele_type", isNull = true, comment = "用例控件类型")
    private String eleType;

    @TableField
    @Column(value = "ele_name", isNull = true, comment = "用例控件名称")
    private String eleName;
    public static TestCaseElements newDeletedElement(int id) {
        String tips = "TestCaseElements does not exist.";
        return new TestCaseElements()
                .setId(id)
                .setEleName(tips)
                .setEleType("id")
                .setEleValue(tips);

    }
}
