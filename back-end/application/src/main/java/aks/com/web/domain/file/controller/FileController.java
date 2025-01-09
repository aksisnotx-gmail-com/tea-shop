
package aks.com.web.domain.file.controller;

import aks.com.sdk.resp.RespEntity;
import aks.com.web.domain.file.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author james.aks
 * @since 2025-01-09 16:35:43
 */
@Tag(name = "文件")
@RestController
@RequestMapping("file")
@RequiredArgsConstructor
public class FileController {

    private final FileService service;

    @GetMapping("download/{id}")
    @Operation(summary = "下载文件")
    public void download(@PathVariable String id) {
        service.download(id);
    }

    @PostMapping("upload")
    @Operation(summary = "上传文件")
    public RespEntity<String> upload(@RequestPart("file") MultipartFile multipartFile) throws IOException {
        return RespEntity.success(service.upload(multipartFile));
    }

}
