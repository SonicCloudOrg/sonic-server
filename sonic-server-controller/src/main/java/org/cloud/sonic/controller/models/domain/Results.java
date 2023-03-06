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
import org.cloud.sonic.controller.models.dto.ResultsDTO;

import java.io.Serializable;
import java.util.Date;

/**
 * @author JayWenStar
 * @since 2021-12-17
 */
@Schema(name ="Results对象", description = "")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("results")
@TableComment("测试结果表")
@TableCharset(MySqlCharsetConstant.DEFAULT)
@TableEngine(MySqlEngineConstant.InnoDB)
public class Results implements Serializable, TypeConverter<Results, ResultsDTO> {

    @TableId(value = "id", type = IdType.AUTO)
    @IsAutoIncrement
    private Integer id;

    @Schema(description = "创建时间", example = "2021-08-15 11:36:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    @Column(value = "create_time", type = MySqlTypeConstant.DATETIME, isNull = false, comment = "任务创建时间")
    Date createTime;

    @Schema(description = "结束时间", example = "2021-08-15 11:36:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Column(value = "end_time", type = MySqlTypeConstant.DATETIME, comment = "任务结束时间")
    Date endTime;

    @TableField
    @Column(value = "project_id", isNull = false, comment = "所属项目id")
    @Index(value = "IDX_PROJECT_ID", columns = {"project_id"})
    private Integer projectId;

    @TableField
    @Column(value = "receive_msg_count", isNull = false, comment = "接受消息数量")
    private Integer receiveMsgCount;

    @TableField
    @Column(value = "send_msg_count", isNull = false, comment = "发送消息数量")
    private Integer sendMsgCount;

    @TableField
    @Column(isNull = false, comment = "结果状态")
    private Integer status;

    @TableField
    @Column(comment = "触发者", defaultValue = "")
    private String strike;

    @TableField
    @Column(value = "suite_id", isNull = false, comment = "测试套件id")
    private Integer suiteId;

    @TableField
    @Column(value = "suite_name", comment = "测试套件名字", defaultValue = "")
    private String suiteName;
}
