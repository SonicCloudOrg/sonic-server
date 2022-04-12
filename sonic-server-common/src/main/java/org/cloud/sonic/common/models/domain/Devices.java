package org.cloud.sonic.common.models.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlCharsetConstant;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlEngineConstant;
import org.cloud.sonic.common.models.base.TypeConverter;
import org.cloud.sonic.common.models.dto.DevicesDTO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author JayWenStar
 * @since 2021-12-17
 */
@ApiModel(value = "Devices对象", description = "")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("devices")
@TableComment("设备表")
@TableCharset(MySqlCharsetConstant.DEFAULT)
@TableEngine(MySqlEngineConstant.InnoDB)
public class Devices implements Serializable, TypeConverter<Devices, DevicesDTO> {

    @TableId(value = "id", type = IdType.AUTO)
    @IsAutoIncrement
    private Integer id;

    @TableField
    @Column(value = "agent_id", isNull = false, comment = "所属agent的id")
    private Integer agentId;

    @TableField
    @Column(comment = "cpu架构", defaultValue = "")
    private String cpu;

    @TableField
    @Column(value = "img_url", comment = "手机封面", defaultValue = "")
    private String imgUrl;

    @TableField
    @Column(comment = "制造商", defaultValue = "")
    private String manufacturer;

    @TableField
    @Column(comment = "手机型号", defaultValue = "")
    private String model;

    @TableField
    @Column(comment = "设备名称", defaultValue = "")
    private String name;

    @TableField
    @Column(comment = "设备安装app的密码", defaultValue = "")
    private String password;

    @TableField
    @Column(isNull = false, comment = "系统类型 1：android 2：ios")
    private Integer platform;

    @TableField
    @Column(comment = "设备分辨率", defaultValue = "")
    private String size;

    @TableField
    @Column(comment = "设备状态", defaultValue = "")
    private String status;

    @TableField
    @Column(value = "ud_id", comment = "设备序列号", defaultValue = "")
    @Index(value = "IDX_UD_ID", columns = {"ud_id"})
    private String udId;

    @TableField
    @Column(comment = "设备系统版本", defaultValue = "")
    private String version;

    @TableField
    @Column(value = "nick_name", comment = "设备备注", defaultValue = "")
    private String nickName;

    @TableField
    @Column(comment = "设备当前占用者", defaultValue = "")
    private String user;

    @TableField
    @Column(value = "chi_name", comment = "中文设备", defaultValue = "")
    String chiName;

    @TableField
    @Column(defaultValue = "0", comment = "设备温度")
    Integer temperature;

    @TableField
    @Column(defaultValue = "0", comment = "设备电量")
    Integer level;

    @TableField
    @Column(defaultValue = "0", comment = "Hub接口")
    Integer hubNum;

    public static Devices newDeletedDevice(int id) {
        String tips = "Device does not exist.";
        return new Devices()
                .setAgentId(0)
                .setStatus("DISCONNECTED")
                .setPlatform(0)
                .setId(id)
                .setVersion("unknown")
                .setSize("unknown")
                .setCpu("unknown")
                .setManufacturer("unknown")
                .setName(tips)
                .setModel(tips)
                .setChiName(tips)
                .setNickName(tips)
                .setName(tips)
                .setUser(tips)
                .setUdId(tips)
                .setTemperature(0)
                .setHubNum(0)
                .setLevel(0);
    }
}
