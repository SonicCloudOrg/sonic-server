package org.cloud.sonic.controller.models.domain;

import com.baomidou.mybatisplus.annotation.*;
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
import org.cloud.sonic.controller.models.dto.TestSuitesDTO;
import org.cloud.sonic.controller.tools.NullableIntArrayTypeHandler;

import java.io.Serializable;

/**
 * @author JayWenStar
 * @since 2021-12-17
 */
@Schema(name ="TestSuites对象", description = "")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "test_suites", autoResultMap = true)
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
    @Column(isNull = false, comment = "是否采集系统性能数据", defaultValue = "0")
    private Integer isOpenPerfmon;

    @TableField
    @Column(isNull = false, comment = "采集性能数据间隔", defaultValue = "1000")
    private Integer perfmonInterval;

    @TableField
    @Column(value = "project_id", isNull = false, comment = "覆盖类型")
    @Index(value = "IDX_PROJECT_ID", columns = {"project_id"})
    private Integer projectId;

    @TableField(typeHandler = NullableIntArrayTypeHandler.class, updateStrategy = FieldStrategy.IGNORED)
    @Column(value = "alert_robot_ids", type = MySqlTypeConstant.VARCHAR, length = 1024, comment = "项目内测试套件默认通知配置，为null时取项目配置的默认值")
    int[] alertRobotIds;
}
