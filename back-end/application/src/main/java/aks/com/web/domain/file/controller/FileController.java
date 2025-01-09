
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
 * @author james.aks
 * @since 2025-01-09 16:35:43
 */
@Tag(name = "sys_file api")
@RestController
@RequestMapping("file")
@RequiredArgsConstructor
public class FileController {

    private final FileService service;

    @GetMapping("list")
    @Operation(summary = "获取文件列表")
    public RespEntity<List<FileEntity>> list() {
        return RespEntity.success(service.list());
    }
}
