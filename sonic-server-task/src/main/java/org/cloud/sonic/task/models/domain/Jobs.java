package org.cloud.sonic.task.models.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlCharsetConstant;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlEngineConstant;
import org.cloud.sonic.task.models.base.TypeConverter;
import org.cloud.sonic.task.models.dto.JobsDTO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@ApiModel(value = "Jobs对象", description = "")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("jobs")
@TableComment("定时任务表")
@TableCharset(MySqlCharsetConstant.DEFAULT)
@TableEngine(MySqlEngineConstant.InnoDB)
public class Jobs implements Serializable, TypeConverter<Jobs, JobsDTO> {

    @TableId(value = "id", type = IdType.AUTO)
    @IsAutoIncrement
    private Integer id;

    @TableField
    @Column(value = "cron_expression", isNull = false, comment = "cron表达式")
    private String cronExpression;

    @TableField
    @Column(isNull = false, comment = "任务名称")
    private String name;

    @TableField
    @Column(value = "project_id", isNull = false, comment = "所属项目id")
    @Index(value = "IDX_PROJECT_ID", columns = "project_id")
    private Integer projectId;

    @TableField
    @Column(isNull = false, comment = "任务状态 1：开启 2：关闭")
    private Integer status;

    @TableField
    @Column(value = "suite_id", isNull = false, comment = "测试套件id")
    private Integer suiteId;
}
