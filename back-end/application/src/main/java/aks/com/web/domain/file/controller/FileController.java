package aks.com.web.domain.file.controller;

import aks.com.sdk.resp.RespEntity;
import aks.com.web.domain.file.entity.FileEntity;
import aks.com.web.domain.file.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author jamesaks
 * @since 2025/1/8
 */
@Tag(name = "文件接口")
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("/test")
    @Operation(summary = "测试接口")
    public RespEntity<List<FileEntity>> test() {
        return RespEntity.success(fileService.list());
    }

}
