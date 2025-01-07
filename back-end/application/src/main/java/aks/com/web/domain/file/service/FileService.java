package aks.com.web.domain.file.service;

import aks.com.web.domain.common.service.AbstractService;
import aks.com.web.domain.file.entity.FileEntity;
import aks.com.web.domain.file.mapper.FileMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @author jamesaks
 * @since 2025/1/7
 */
@Service
@RequiredArgsConstructor
public class FileService extends AbstractService<FileMapper, FileEntity> {

    private final SqlSessionFactory factory;

    @Override
    public boolean saveBatch(Collection<FileEntity> entityList) {
        return getBaseMapper().insertBatch(entityList,factory).isEmpty();
    }
}
