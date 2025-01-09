
package aks.com.web.domain.file.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import aks.com.web.domain.common.entity.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文件(sys_file)表实体类
 *
 * @author james.aks
 * @since 2025-01-09 16:35:43
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_file")
public class FileEntity extends Entity {
    
    /**
     *  保存路径
     */
    private String path;
                    
}
