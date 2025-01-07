package aks.com.web.domain.file.entity;

import aks.com.web.domain.common.entity.Entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文件(SysFile)表实体类
 *
 * @author makejava
 * @since 2025-01-07 11:56:45
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_file")
public class FileEntity extends Entity {

    //保存路径
    private String path;
}

