package com.ideaaedi.zoo.example.springboot.file.xfilestorage;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.google.common.collect.Lists;
import com.ideaaedi.commonds.path.PathUtil;
import com.ideaaedi.zoo.diy.feature.file.api.entity.FileInfoDTO;
import com.ideaaedi.zoo.diy.feature.file.api.entity.FileUploadDTO;
import com.ideaaedi.zoo.diy.feature.file.api.service.FileUploadDownload;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dromara.x.file.storage.spring.SpringFileStorageProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * 删除测试
 *
 * @author <font size = "20" color = "#3CAA3C"><a href="https://gitee.com/JustryDeng">JustryDeng</a></font> <img src="https://gitee.com/JustryDeng/shared-files/raw/master/JustryDeng/avatar.jpg" />
 * @since 1.0.0
 */
@Slf4j
@SpringBootTest(classes = ExampleSpringbootXFileStorageApplication.class)
public class DeleteTest {
    
    @Resource
    private FileUploadDownload fileUploadDownload;
    
    @Resource
    private SpringFileStorageProperties fileStorageProperties;
    
    String testResourceDir = PathUtil.getProjectRootDir(DeleteTest.class)
            .replace("/target/classes/",
                    "/src/test/resources/");

    private volatile boolean isInit = false;
    @BeforeEach
    public void init () {
        if (isInit) {
            return;
        }
        // 这里通过代码保证对应的minio桶存在（注：一般来讲，生产环境最好手动创建桶）
        MinioUtil.createBucketIfNecessary(fileStorageProperties.getMinio());
        isInit = true;
    }
    
    /**
     * 测试{@link FileUploadDownload#delete(String, Collection)}
     */
    @Test
    @SneakyThrows
    void testDelete1() {
        FileInfoDTO fileInfoDTO = demoFile();
        System.err.println(JSON.toJSONString(fileInfoDTO, JSONWriter.Feature.PrettyFormat));
    
        String filename = fileInfoDTO.getFilename();
        String dirPath = fileInfoDTO.getDirPath();
        fileUploadDownload.delete(dirPath, Lists.newArrayList(filename));
        //noinspection RedundantStringFormatCall
        System.err.println(String.format("去【%s】看一下【%s】是否已删除", dirPath, filename));
    
        TimeUnit.MILLISECONDS.sleep(2000);
    }
    
    /**
     * 测试{@link FileUploadDownload#delete(String, String, Collection)}
     */
    @Test
    @SneakyThrows
    void testDelete2() {
        FileInfoDTO fileInfoDTO = demoFile();
        System.err.println(JSON.toJSONString(fileInfoDTO, JSONWriter.Feature.PrettyFormat));
    
        String filename = fileInfoDTO.getFilename();
        String dirPath = fileInfoDTO.getDirPath();
        String thFilename = fileInfoDTO.getThFilename();
        fileUploadDownload.delete(fileInfoDTO.getPlatform(), dirPath, Lists.newArrayList(filename, thFilename));
        //noinspection RedundantStringFormatCall
        System.err.println(String.format("去【%s】看一下【%s】和【%s】是否已删除", dirPath, filename, thFilename));
    
        TimeUnit.MILLISECONDS.sleep(2000);
    }
    
    @Test
    @SneakyThrows
    private FileInfoDTO demoFile() {
        FileInfoDTO upload = fileUploadDownload.upload(
                FileUploadDTO.of(new File(testResourceDir, "maomi.jpeg"))
                        .setContentType(MediaType.IMAGE_JPEG_VALUE)
                        .setOriginalFilename("xiaomaomi.jpeg")
                        .setFilename("小猫咪.jpeg")
                        .setDirPath("a/b/c/")
                        .setCalculateHashMd5(true)
                        .setThWidtHeight(80, 80)
        );
        Assert.isTrue(StringUtils.isNotBlank(upload.getUrl()), "url 不应为空");
        return upload;
    }
}