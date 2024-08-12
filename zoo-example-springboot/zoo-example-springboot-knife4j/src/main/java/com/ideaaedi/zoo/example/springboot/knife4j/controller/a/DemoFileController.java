package com.ideaaedi.zoo.example.springboot.knife4j.controller.a;

import com.ideaaedi.zoo.commonbase.entity.Result;
import com.ideaaedi.zoo.diy.artifact.apidoc.knife4j.annotation.ApiTag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/file")
@ApiTag(name = "文件", order = 3)
public class DemoFileController {
    @Operation(description = "单纯文件上传，无任何参数",summary = "单纯文件上传")
    @Parameter(name = "file",description = "文件",in = ParameterIn.DEFAULT,required = true,
            schema = @Schema(name = "file",format = "binary"))
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file){
        return Result.success(file.getOriginalFilename());
    }
    
    @Operation(summary = "文件上传-带参数")
    @Parameters({
            @Parameter(name = "file",description = "文件",required = true,in=ParameterIn.DEFAULT,
                    schema = @Schema(name = "file",format = "binary")),
            @Parameter(name = "name",description = "文件名称",required = true),
    })
    @PostMapping("/uploadParam")
    public Result<String> uploadParam(@RequestParam("file")MultipartFile file,@RequestParam("name") String name){
        return Result.success(String.join("\n", name, file.getOriginalFilename()));
    }
    
    @Operation(summary = "文件上传-带参数Header")
    @Parameters({
            @Parameter(name = "token",description = "请求token",required = true,in = ParameterIn.HEADER),
            @Parameter(name = "file",description = "文件",required = true,in=ParameterIn.DEFAULT,
                    schema = @Schema(name = "file",format = "binary")),
            @Parameter(name = "name",description = "文件名称",required = true),
    })
    @PostMapping("/uploadParamHeader")
    public Result<String> uploadParamHeader(@RequestHeader("token") String token, @RequestParam("file")MultipartFile file, @RequestParam("name") String name){
        return Result.success(String.join("\n", token, name, file.getOriginalFilename()));
    }
    
    @Operation(summary = "文件上传-带参数Path")
    @Parameters({
            @Parameter(name = "id",description = "文件id",in = ParameterIn.PATH),
            @Parameter(name = "file",description = "文件",required = true,in=ParameterIn.DEFAULT,
                    schema = @Schema(name = "file",format = "binary")),
            @Parameter(name = "name",description = "文件名称",required = true),
    })
    @PostMapping("/uploadParam/{id}")
    public Result<String> uploadParamPath(@PathVariable("id")String id,@RequestParam("file")MultipartFile file,@RequestParam("name") String name){
        return Result.success(String.join("\n", id, name, file.getOriginalFilename()));
    }
    
    @Operation(summary = "多文件上传")
    @Parameter(name = "files",description = "文件",in = ParameterIn.DEFAULT,
            schema = @Schema(name = "files",format = "binary"))
    @PostMapping("/uploadBatch")
    public Result<String> uploadBatch(@RequestParam("files") List<MultipartFile> files){
        return Result.success(files.stream().map(MultipartFile::getOriginalFilename).collect(Collectors.joining("\n")));
    }
    
    @Operation(description = "多文件上传-array",summary = "多文件上传-array")
    @PostMapping("/uploadBatchArray")
    public Result<String> uploadBatchArray(@RequestParam("files") MultipartFile[] files){
        return Result.success(Arrays.stream(files).map(MultipartFile::getOriginalFilename).collect(Collectors.joining("\n")));
    }
    
}