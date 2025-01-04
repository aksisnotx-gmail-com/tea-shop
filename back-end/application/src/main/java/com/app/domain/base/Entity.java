package com.app.domain.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 实体类
 *
 * @author xxl
 * @since 2024/2/27
 */
@Data
public class Entity implements Serializable {

    @Serial
    private static final long serialVersionUID = -1284347847778673827L;

    @TableId
    @JsonView({UPDATE.class})
    @NotBlank(message = "id不能为空",groups = {UPDATE.class})
    private String id;

    @JsonView({IGNORE.class})
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

    @JsonView({IGNORE.class})
    @TableField(value = "update_time",fill = FieldFill.UPDATE)
    private Date updateTime;

    public interface INSERT {}
    public interface IGNORE {}
    public interface UPDATE {}

    public interface LOGIN{}
}
