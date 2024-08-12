package com.ideaaedi.zoo.example.springboot.file.xfilestorage;

import com.ideaaedi.commonds.io.IOUtil;
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
import org.springframework.util.FastByteArrayOutputStream;

import java.io.File;
import java.io.OutputStream;

/**
 * 下载测试
 *
 * @author <font size = "20" color = "#3CAA3C"><a href="https://gitee.com/JustryDeng">JustryDeng</a></font> <img src="https://gitee.com/JustryDeng/shared-files/raw/master/JustryDeng/avatar.jpg" />
 * @since 1.0.0
 */
@Slf4j
@SpringBootTest(classes = ExampleSpringbootXFileStorageApplication.class)
public class DownloadTest {
    
    @Resource
    private FileUploadDownload fileUploadDownload;
    
    @Resource
    private SpringFileStorageProperties fileStorageProperties;
    
    String testResourceDir = PathUtil.getProjectRootDir(DownloadTest.class)
            .replace("/target/classes/",
                    "/src/test/resources/");

    @BeforeEach
    public void init () {
        // 这里通过代码保证对应的minio桶存在（注：一般来讲，生产环境最好手动创建桶）
        MinioUtil.createBucketIfNecessary(fileStorageProperties.getMinio());
    }
    
    /**
     * 测试{@link FileUploadDownload#download(String, String)}
     */
    @Test
    @SneakyThrows
    void testDownload1() {
        FileInfoDTO fileInfoDTO = demoFile("minio-xyz");
        byte[] file = fileUploadDownload.download(fileInfoDTO.getPlatform(), fileInfoDTO.getFilepath());
        File destFile = new File("C:\\Users\\Administrator\\Desktop\\tmp\\" + fileInfoDTO.getOriginalFilename());
        if (destFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            destFile.delete();
        }
        IOUtil.toFile(file, destFile, true);
        System.err.println("请查看文件：" + destFile.getAbsolutePath());
    }
    
    /**
     * 测试{@link FileUploadDownload#downloadToFile(String, String, File)}
     */
    @Test
    @SneakyThrows
    void testDownload2() {
        FileInfoDTO fileInfoDTO = demoFile("minio-xyz");
        File destFile = new File("C:\\Users\\Administrator\\Desktop\\tmp\\" + fileInfoDTO.getOriginalFilename());
        if (destFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            destFile.delete();
        }
        fileUploadDownload.downloadToFile(fileInfoDTO.getPlatform(), fileInfoDTO.getFilepath(), destFile);
        System.err.println("请查看文件：" + destFile.getAbsolutePath());
    }

    
    /**
     * 测试{@link FileUploadDownload#downloadToOutputStream(String, String, OutputStream)}
     */
    @Test
    @SneakyThrows
    void testDownload3() {
        FileInfoDTO fileInfoDTO = demoFile("minio-xyz");
        
        FastByteArrayOutputStream fastByteArrayOutputStream = new FastByteArrayOutputStream();
        fileUploadDownload.downloadToOutputStream(fileInfoDTO.getPlatform(), fileInfoDTO.getFilepath(), fastByteArrayOutputStream);
        byte[] bytes = fastByteArrayOutputStream.toByteArray();
    
        File destFile = new File("C:\\Users\\Administrator\\Desktop\\tmp\\" + fileInfoDTO.getOriginalFilename());
        if (destFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            destFile.delete();
        }
        IOUtil.toFile(bytes, destFile, true);
        System.err.println("请查看文件：" + destFile.getAbsolutePath());
    }
    
    /**
     * 测试{@link FileUploadDownload#download(String)}
     */
    @Test
    @SneakyThrows
    void testDownload4() {
        FileInfoDTO fileInfoDTO = demoFile();
        byte[] file = fileUploadDownload.download(fileInfoDTO.getThFilepath());
        File destFile = new File("C:\\Users\\Administrator\\Desktop\\tmp\\" + fileInfoDTO.getThFilename());
        if (destFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            destFile.delete();
        }
        IOUtil.toFile(file, destFile, true);
        System.err.println("请查看文件：" + destFile.getAbsolutePath());
    }
    
    /**
     * 测试{@link FileUploadDownload#downloadToFile(String, File)}
     */
    @Test
    @SneakyThrows
    void testDownload5() {
        FileInfoDTO fileInfoDTO = demoFile();
        File destFile = new File("C:\\Users\\Administrator\\Desktop\\tmp\\" + fileInfoDTO.getThFilename());
        if (destFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            destFile.delete();
        }
        fileUploadDownload.downloadToFile(fileInfoDTO.getThFilepath(), destFile);
        System.err.println("请查看文件：" + destFile.getAbsolutePath());
    }

    
    /**
     * 测试{@link FileUploadDownload#downloadToOutputStream(String, OutputStream)}
     */
    @Test
    @SneakyThrows
    void testDownload6() {
        FileInfoDTO fileInfoDTO = demoFile();
        
        FastByteArrayOutputStream fastByteArrayOutputStream = new FastByteArrayOutputStream();
        fileUploadDownload.downloadToOutputStream(fileInfoDTO.getThFilepath(), fastByteArrayOutputStream);
        byte[] bytes = fastByteArrayOutputStream.toByteArray();
    
        File destFile = new File("C:\\Users\\Administrator\\Desktop\\tmp\\" + fileInfoDTO.getThFilename());
        if (destFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            destFile.delete();
        }
        IOUtil.toFile(bytes, destFile, true);
        System.err.println("请查看文件：" + destFile.getAbsolutePath());
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

    @Test
    @SneakyThrows
    private FileInfoDTO demoFile(String platform) {
        FileInfoDTO upload = fileUploadDownload.upload(
                FileUploadDTO.of(new File(testResourceDir, "maomi.jpeg"))
                        .setContentType(MediaType.IMAGE_JPEG_VALUE)
                        .setOriginalFilename("xiaomaomi.jpeg")
                        .setFilename("小猫咪.jpeg")
                        .setDirPath("a/b/c/")
                        .setPlatform(platform)
                        .setCalculateHashMd5(true)
                        .setThWidtHeight(80, 80)
        );
        Assert.isTrue(StringUtils.isNotBlank(upload.getUrl()), "url 不应为空");
        return upload;
    }
}