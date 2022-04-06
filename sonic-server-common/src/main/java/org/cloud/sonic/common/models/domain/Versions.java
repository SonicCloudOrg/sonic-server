package org.cloud.sonic.common.models.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.sunchenbin.mybatis.actable.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlCharsetConstant;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlEngineConstant;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import org.cloud.sonic.common.models.base.TypeConverter;
import org.cloud.sonic.common.models.dto.VersionsDTO;
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
@ApiModel(value = "Versions对象", description = "")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("versions")
@TableComment("版本表")
@TableCharset(MySqlCharsetConstant.DEFAULT)
@TableEngine(MySqlEngineConstant.InnoDB)
public class Versions implements Serializable, TypeConverter<Versions, VersionsDTO> {

    @TableId(value = "id", type = IdType.AUTO)
    @IsAutoIncrement
    private Integer id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "日期", example = "2021-08-15T16:00:00.000+00:00")
    @TableField(fill = FieldFill.INSERT)
    @Column(value = "create_time", type = MySqlTypeConstant.DATETIME, isNull = false, comment = "创建时间内")
    private Date createTime;

    @TableField
    @Column(value = "project_id", isNull = false, comment = "所属项目id")
    private Integer projectId;

    @TableField
    @Column(value = "version_name", isNull = false, comment = "迭代名称")
    private String versionName;
}
