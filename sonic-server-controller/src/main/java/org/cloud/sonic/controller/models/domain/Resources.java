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
import org.cloud.sonic.controller.models.dto.ResourcesDTO;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("resources")
@TableComment("资源信息表")
@TableCharset(MySqlCharsetConstant.DEFAULT)
@TableEngine(MySqlEngineConstant.InnoDB)
public class Resources implements Serializable, TypeConverter<Resources, ResourcesDTO> {

    @TableId(value = "id", type = IdType.AUTO)
    @IsAutoIncrement
    private Integer id;

    @TableField("`desc`")
    @Column(value = "desc", isNull = false, comment = "描述")
    private String desc;

    @TableField
    @Column(value = "parent_id", isNull = false, defaultValue = "0", comment = "父级 id")
    private Integer parentId;

    @TableField
    @Column(value = "method", isNull = false, comment = "请求方法")
    @Index
    private String method;

    @TableField
    @Column(value = "path", isNull = false, comment = "资源路径")
    @Index
    private String path;

    @TableField
    @Column(value = "white", isNull = false, defaultValue = "1", comment = "是否是白名单 url，0是 1 不是")
    private Integer white;

    @Index
    @TableField
    @Column(value = "version", isNull = false, defaultValue = "", comment = "url 资源的版本，每次新增接口需要更新，当接口版本不一致时标记为white")
    private String version;

    @TableField
    @Column(value = "need_auth", isNull = false, defaultValue = "1", comment = "是否需要鉴权，0 不需要 1 需要")
    private Integer needAuth;

    @Schema(description = "创建时间", example = "2021-08-15 11:36:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    @Column(value = "create_time", type = MySqlTypeConstant.DATETIME, isNull = false, comment = "创建时间")
    Date createTime;

    @Schema(description = "更新时间", example = "2021-08-15 11:36:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Column(value = "update_time", type = MySqlTypeConstant.DATETIME, comment = "更新时间")
    Date updateTime;


}
