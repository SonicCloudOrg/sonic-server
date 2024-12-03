package org.cloud.sonic.controller.models.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.TableCharset;
import com.gitee.sunchenbin.mybatis.actable.annotation.TableComment;
import com.gitee.sunchenbin.mybatis.actable.annotation.TableEngine;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlCharsetConstant;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlEngineConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.dto.PublicStepsStepsDTO;

import java.io.Serializable;

/**
 * @author JayWenStar
 * @since 2021-12-17
 */
@Schema(name ="PublicStepsSteps对象", description = "")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("public_steps_steps")
@TableComment("公共步骤 - 步骤 关系映射表")
@TableCharset(MySqlCharsetConstant.DEFAULT)
@TableEngine(MySqlEngineConstant.InnoDB)
public class PublicStepsSteps implements Serializable, TypeConverter<PublicStepsSteps, PublicStepsStepsDTO> {

    @TableField
    @Column(value = "public_steps_id", isNull = false, comment = "公共步骤id")
    private Integer publicStepsId;

    @TableField
    @Column(value = "steps_id", isNull = false, comment = "步骤id")
    private Integer stepsId;
}
