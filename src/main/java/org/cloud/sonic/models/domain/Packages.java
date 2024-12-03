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
import org.cloud.sonic.controller.models.dto.PackageDTO;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yaming116, Eason
 * @date 2022/5/26 1:22
 */
@Schema(name ="Packages对象", description = "")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("packages")
@TableComment("安装包表")
@TableCharset(MySqlCharsetConstant.DEFAULT)
@TableEngine(MySqlEngineConstant.InnoDB)
public class Packages implements Serializable, TypeConverter<Packages, PackageDTO> {

    @TableId(value = "id", type = IdType.AUTO)
    @IsAutoIncrement
    private Integer id;

    @TableField
    @Column(value = "project_id", isNull = false, comment = "项目描述")
    private Integer projectId;

    @TableField
    @Column(value = "pkg_name", isNull = false, comment = "安装包名称")
    private String pkgName;

    @TableField
    @Column(value = "platform", isNull = false, comment = "平台")
    private String platform;

    @TableField
    @Column(value = "branch", isNull = false, comment = "构建分支")
    private String branch;

    @TableField
    @Column(value = "url", isNull = false, comment = "下载地址")
    private String url;

    @TableField
    @Column(value = "build_url", isNull = false, comment = "来源地址")
    private String buildUrl;

    @Schema(description = "创建时间", example = "2021-08-15 11:36:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    @Column(value = "create_time", type = MySqlTypeConstant.DATETIME, isNull = false, comment = "任务创建时间")
    Date createTime;

    @Schema(description = "更新时间", example = "2021-08-15 11:36:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Column(value = "update_time", type = MySqlTypeConstant.DATETIME, comment = "更新时间")
    Date updateTime;
}
