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
import org.cloud.sonic.controller.models.dto.ProjectsMembersDTO;

import java.io.Serializable;


/**
 * @author liulijun
 * @since 2023-4-28
 */
@Schema(name ="ProjectsUsers对象", description = "")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("users_projects")
@TableComment("项目成员表")
@TableCharset(MySqlCharsetConstant.DEFAULT)
@TableEngine(MySqlEngineConstant.InnoDB)
public class ProjectsMembers implements Serializable, TypeConverter<ProjectsMembers, ProjectsMembersDTO> {

    @TableId(value = "id", type = IdType.AUTO)
    @IsAutoIncrement
    private Integer id;

    @TableField
    @Column(value = "project_id", isNull = false, comment = "项目ID")
    private Integer projectId;

    @TableField
    @Column(value = "user_id", isNull = false, comment = "成员ID")
    private Integer userId;

    @TableField
    @Column(value = "user_name", isNull = false, comment = "成员名称")
    private String userName;

    @TableField
    @Column(value = "member_role", isNull = false, comment = "成员角色：0-成员,1-创建人")
    private Integer memberRole;

}