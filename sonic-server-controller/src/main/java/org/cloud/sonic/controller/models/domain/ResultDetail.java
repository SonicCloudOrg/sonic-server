package org.cloud.sonic.controller.models.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
import org.cloud.sonic.controller.models.dto.ResultDetailDTO;

import java.io.Serializable;
import java.util.Date;

/**
 * @author JayWenStar
 * @since 2021-12-17
 */
@Schema(name ="ResultDetail对象", description = "")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("result_detail")
@TableComment("任务结果详情表")
@TableCharset(MySqlCharsetConstant.DEFAULT)
@TableEngine(MySqlEngineConstant.InnoDB)
public class ResultDetail implements Serializable, TypeConverter<ResultDetail, ResultDetailDTO> {

    @TableId(value = "id", type = IdType.AUTO)
    @IsAutoIncrement
    private Integer id;

    @TableField
    @Column(value = "case_id", isNull = false, comment = "测试用例id")
    private Integer caseId;

    @TableField
    @Column(comment = "描述", defaultValue = "")
    private String des;

    @TableField
    @Column(value = "device_id", isNull = false, comment = "设备id")
    private Integer deviceId;

    @TableField
    @Column(type = MySqlTypeConstant.LONGTEXT, comment = "日志信息")
    private String log;

    @TableField
    @Column(value = "result_id", isNull = false, comment = "所属结果id")
    @Index(
            value = "IDX_RESULT_ID_CASE_ID_TYPE_DEVICE_ID",
            columns = {"result_id", "case_id", "type", "device_id"}
    )
    private Integer resultId;

    @TableField
    @Column(isNull = false, comment = "步骤执行状态")
    private Integer status;

    @Schema(description = "时间", example = "16:00:00")
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    @TableField
    @Column(type = MySqlTypeConstant.TIMESTAMP, isNull = false, comment = "步骤执行状态")
    @Index(value = "IDX_TIME", columns = {"time"})
    private Date time;

    @TableField
    @Column(comment = "测试结果详情类型", defaultValue = "")
    @Index(value = "IDX_TYPE", columns = {"type"})
    private String type;
}
