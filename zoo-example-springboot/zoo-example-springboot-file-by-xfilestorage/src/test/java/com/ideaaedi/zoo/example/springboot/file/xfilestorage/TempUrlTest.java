package com.ideaaedi.zoo.example.springboot.file.xfilestorage;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
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
import java.util.concurrent.TimeUnit;

/**
 * 测试生成临时url
 *
 * @author <font size = "20" color = "#3CAA3C"><a href="https://gitee.com/JustryDeng">JustryDeng</a></font> <img
 * src="https://gitee.com/JustryDeng/shared-files/raw/master/JustryDeng/avatar.jpg" />
 * @since 1.0.0
 */
@Slf4j
@SpringBootTest(classes = ExampleSpringbootXFileStorageApplication.class)
public class TempUrlTest {
    
    @Resource
    private FileUploadDownload fileUploadDownload;
    
    @Resource
    private SpringFileStorageProperties fileStorageProperties;
    
    String testResourceDir = PathUtil.getProjectRootDir(TempUrlTest.class)
            .replace("/target/classes/",
                    "/src/test/resources/");
    
    @BeforeEach
    public void init() {
        // 这里通过代码保证对应的minio桶存在（注：一般来讲，生产环境最好手动创建桶）
        MinioUtil.createBucketIfNecessary(fileStorageProperties.getMinio());
    }
    
    /**
     * 测试{@link FileUploadDownload#generateTempUrl(String, int, TimeUnit)}
     */
    @Test
    @SneakyThrows
    void testTempUrl1() {
        FileInfoDTO fileInfoDTO = demoFile();
        System.err.println(JSON.toJSONString(fileInfoDTO, JSONWriter.Feature.PrettyFormat));
        
        String tempUrl = fileUploadDownload.generateTempUrl(fileInfoDTO.getFilepath(), 1, TimeUnit.MINUTES);
        //noinspection RedundantStringFormatCall
        System.err.println(String.format("生成的临时路径是：%s", tempUrl));
        
        TimeUnit.MILLISECONDS.sleep(2000);
    }
    
    /**
     * 测试{@link FileUploadDownload#generateTempUrl(String, String, int, TimeUnit)}
     */
    @Test
    @SneakyThrows
    void testTempUrl2() {
        FileInfoDTO fileInfoDTO = demoFile();
        System.err.println(JSON.toJSONString(fileInfoDTO, JSONWriter.Feature.PrettyFormat));
        
        String tempUrl = fileUploadDownload.generateTempUrl(fileInfoDTO.getPlatform(), fileInfoDTO.getFilepath(), 3, TimeUnit.MINUTES);
        //noinspection RedundantStringFormatCall
        System.err.println(String.format("生成的临时路径是：%s", tempUrl));
        
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