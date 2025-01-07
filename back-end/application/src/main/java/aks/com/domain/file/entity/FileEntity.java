package aks.com.domain.file.entity;

import aks.com.domain.common.entity.Entity;
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
public class FileEntity extends Entity {

    //保存路径
    private String path;
}

