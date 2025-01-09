
package aks.com.web.domain.file.service;

import aks.com.web.domain.file.entity.FileEntity;
import aks.com.web.domain.file.mapper.FileMapper;
import aks.com.web.domain.common.service.AbstractService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author james.aks
 * @since 2025-01-09 16:35:43
 */
@Service
@RequiredArgsConstructor
public class FileService extends AbstractService<FileMapper,FileEntity> {

}
