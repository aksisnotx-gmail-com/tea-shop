
package aks.com.web.domain.file.service;

import aks.com.sdk.exception.GlobalException;
import aks.com.sdk.util.file.FileUtils;
import aks.com.web.domain.common.service.AbstractService;
import aks.com.web.domain.file.entity.FileEntity;
import aks.com.web.domain.file.mapper.FileMapper;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

/**
 * @author james.aks
 * @since 2025-01-09 16:35:43
 */
@Service
@RequiredArgsConstructor
public class FileService extends AbstractService<FileMapper,FileEntity> {

    @Value("${file.save-path}")
    private String filePath;

    private final HttpServletResponse response;

    private static final String REQUEST_URL = "/file/download/";

    public void download(String id) {
        FileEntity entity = getById(id);
        if (Objects.isNull(entity)) {
            throw new GlobalException("文件不存在");
        }
        FileUtils.webDownload(FileUtils.readFile(entity.getPath()), response, FileUtils.getFileName(entity.getPath()));
    }

    public String upload(MultipartFile multipartFile) throws IOException {
        String filename = multipartFile.getOriginalFilename();
        if (StrUtil.isBlank(filename)) {
            throw new GlobalException("上传失败");
        }
        filename = System.currentTimeMillis() + "." + FileUtils.getFileSuffix(filename);
        String fileSavePath = FileUtils.saveFile(multipartFile.getInputStream(), filePath + filename);
        FileEntity fileEntity = new FileEntity();
        fileEntity.setPath(fileSavePath);
        this.save(fileEntity);
        return REQUEST_URL + fileEntity.getId();
    }
}
