package com.ideaaedi.zoo.example.springboot.file.xfilestorage;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 上传测试
 *
 * @author <font size = "20" color = "#3CAA3C"><a href="https://gitee.com/JustryDeng">JustryDeng</a></font> <img src="https://gitee.com/JustryDeng/shared-files/raw/master/JustryDeng/avatar.jpg" />
 * @since 1.0.0
 */
@Slf4j
@SpringBootTest(classes = ExampleSpringbootXFileStorageApplication.class)
public class UploadTest {
    
    @Resource
    private FileUploadDownload fileUploadDownload;
    
    @Resource
    private SpringFileStorageProperties fileStorageProperties;
    
    String testResourceDir = PathUtil.getProjectRootDir(UploadTest.class)
            .replace("/target/classes/",
                    "/src/test/resources/");

    @BeforeEach
    public void init () {
        // 这里通过代码保证对应的minio桶存在（注：一般来讲，生产环境最好手动创建桶）
        MinioUtil.createBucketIfNecessary(fileStorageProperties.getMinio());
    }
    
    /**
     * 测试{@link FileUploadDownload#upload(MultipartFile)}
     */
    @Test
    @SneakyThrows
    void testUpload1() {
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "用户数据.json",
                "user.json",
                MediaType.APPLICATION_JSON_VALUE,
                IOUtil.toBytes(new File(testResourceDir, "user.json"))
                );
        FileInfoDTO upload = fileUploadDownload.upload(mockMultipartFile);
        System.err.println(JSON.toJSONString(upload, JSONWriter.Feature.PrettyFormat));
        Assert.isTrue(StringUtils.isNotBlank(upload.getUrl()), "url 不应为空");
    }
    
    /**
     * 测试{@link FileUploadDownload#upload(String, MultipartFile)}
     */
    @Test
    @SneakyThrows
    void testUpload2() {
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "用户数据.json",
                "user.json",
                MediaType.APPLICATION_JSON_VALUE,
                IOUtil.toBytes(new File(testResourceDir, "user.json"))
                );
        // 配置文件中设置的第二个 存储平台是：minio-xyz
        FileInfoDTO upload = fileUploadDownload.upload("minio-xyz", mockMultipartFile);
        System.err.println(JSON.toJSONString(upload, JSONWriter.Feature.PrettyFormat));
        Assert.isTrue(StringUtils.isNotBlank(upload.getUrl()), "url 不应为空");
    }
    
    /**
     * 测试{@link FileUploadDownload#upload(byte[], String)}
     */
    @Test
    @SneakyThrows
    void testUpload3() {
        FileInfoDTO upload = fileUploadDownload.upload(IOUtil.toBytes(new File(testResourceDir, "user.json")), "sss.json");
        System.err.println(JSON.toJSONString(upload, JSONWriter.Feature.PrettyFormat));
        Assert.isTrue(StringUtils.isNotBlank(upload.getUrl()), "url 不应为空");
    }
    
    /**
     * 测试{@link FileUploadDownload#upload(byte[], String, String)}
     */
    @Test
    @SneakyThrows
    void testUpload4() {
        FileInfoDTO upload = fileUploadDownload.upload(IOUtil.toBytes(new File(testResourceDir, "user.json")), "sss.json", MediaType.APPLICATION_JSON_VALUE);
        System.err.println(JSON.toJSONString(upload, JSONWriter.Feature.PrettyFormat));
        Assert.isTrue(StringUtils.isNotBlank(upload.getUrl()), "url 不应为空");
    }
    
    /**
     * 测试{@link FileUploadDownload#upload(String, byte[], String, String)}
     */
    @Test
    @SneakyThrows
    void testUpload5() {
        // 配置文件中设置的第二个 存储平台是：minio-xyz
        FileInfoDTO upload = fileUploadDownload.upload("minio-xyz", IOUtil.toBytes(new File(testResourceDir, "user.json")), "sss.json");
        System.err.println(JSON.toJSONString(upload, JSONWriter.Feature.PrettyFormat));
        Assert.isTrue(StringUtils.isNotBlank(upload.getUrl()), "url 不应为空");
    }
    
    /**
     * 测试{@link FileUploadDownload#upload(String, byte[], String, String)}
     */
    @Test
    @SneakyThrows
    void testUpload6() {
        // 配置文件中设置的第二个 存储平台是：minio-xyz
        FileInfoDTO upload = fileUploadDownload.upload("minio-xyz", IOUtil.toBytes(new File(testResourceDir, "user.json")), "sss.json", MediaType.APPLICATION_JSON_VALUE);
        System.err.println(JSON.toJSONString(upload, JSONWriter.Feature.PrettyFormat));
        Assert.isTrue(StringUtils.isNotBlank(upload.getUrl()), "url 不应为空");
    }
    
    /**
     * 测试{@link FileUploadDownload#upload(File)}
     */
    @Test
    @SneakyThrows
    void testUpload7() {
        FileInfoDTO upload = fileUploadDownload.upload(
                new File(testResourceDir, "user.json")
        );
        System.err.println(JSON.toJSONString(upload, JSONWriter.Feature.PrettyFormat));
        Assert.isTrue(StringUtils.isNotBlank(upload.getUrl()), "url 不应为空");
    }
    
    /**
     * 测试{@link FileUploadDownload#upload(String, MultipartFile)}
     */
    @Test
    @SneakyThrows
    void testUpload8() {
        FileInfoDTO upload = fileUploadDownload.upload(
                "minio-xyz", new File(testResourceDir, "user.json")
        );
        System.err.println(JSON.toJSONString(upload, JSONWriter.Feature.PrettyFormat));
        Assert.isTrue(StringUtils.isNotBlank(upload.getUrl()), "url 不应为空");
    }
    
    /**
     * 测试{@link FileUploadDownload#upload(File, String)}
     */
    @Test
    @SneakyThrows
    void testUpload9() {
        FileInfoDTO upload = fileUploadDownload.upload(
                new File(testResourceDir, "user.json"), MediaType.APPLICATION_JSON_VALUE
        );
        System.err.println(JSON.toJSONString(upload, JSONWriter.Feature.PrettyFormat));
        Assert.isTrue(StringUtils.isNotBlank(upload.getUrl()), "url 不应为空");
    }
    
    /**
     * 测试{@link FileUploadDownload#upload(File, String)}
     */
    @Test
    @SneakyThrows
    void testUpload10() {
        FileInfoDTO upload = fileUploadDownload.upload(
                "minio-xyz", new File(testResourceDir, "user.json"), MediaType.APPLICATION_JSON_VALUE
        );
        System.err.println(JSON.toJSONString(upload, JSONWriter.Feature.PrettyFormat));
        Assert.isTrue(StringUtils.isNotBlank(upload.getUrl()), "url 不应为空");
    }
    
    /**
     * 测试{@link FileUploadDownload#upload(InputStream, String)}
     */
    @Test
    @SneakyThrows
    void testUpload11() {
        FileInfoDTO upload = fileUploadDownload.upload(new FileInputStream(new File(testResourceDir, "user.json")), "sss.json");
        System.err.println(JSON.toJSONString(upload, JSONWriter.Feature.PrettyFormat));
        Assert.isTrue(StringUtils.isNotBlank(upload.getUrl()), "url 不应为空");
    }
    
    /**
     * 测试{@link FileUploadDownload#upload(InputStream, String, String)}
     */
    @Test
    @SneakyThrows
    void testUpload12() {
        FileInfoDTO upload = fileUploadDownload.upload(new FileInputStream(new File(testResourceDir, "user.json")), "sss.json", MediaType.APPLICATION_JSON_VALUE);
        System.err.println(JSON.toJSONString(upload, JSONWriter.Feature.PrettyFormat));
        Assert.isTrue(StringUtils.isNotBlank(upload.getUrl()), "url 不应为空");
    }
    
    /**
     * 测试{@link FileUploadDownload#upload(String, InputStream, String, String)}
     */
    @Test
    @SneakyThrows
    void testUpload13() {
        // 配置文件中设置的第二个 存储平台是：minio-xyz
        FileInfoDTO upload = fileUploadDownload.upload("minio-xyz", new FileInputStream(new File(testResourceDir, "user.json")), "sss.json");
        System.err.println(JSON.toJSONString(upload, JSONWriter.Feature.PrettyFormat));
        Assert.isTrue(StringUtils.isNotBlank(upload.getUrl()), "url 不应为空");
    }
    
    /**
     * 测试{@link FileUploadDownload#upload(String, InputStream, String, String)}
     */
    @Test
    @SneakyThrows
    void testUpload14() {
        // 配置文件中设置的第二个 存储平台是：minio-xyz
        FileInfoDTO upload = fileUploadDownload.upload("minio-xyz", new FileInputStream(new File(testResourceDir, "user.json")), "sss.json", MediaType.APPLICATION_JSON_VALUE);
        System.err.println(JSON.toJSONString(upload, JSONWriter.Feature.PrettyFormat));
        Assert.isTrue(StringUtils.isNotBlank(upload.getUrl()), "url 不应为空");
    }
    
    /**
     * 测试{@link FileUploadDownload#upload(FileUploadDTO)}
     */
    @Test
    @SneakyThrows
    void testUpload15() {
        FileInfoDTO upload = fileUploadDownload.upload(
                FileUploadDTO.of(new File(testResourceDir, "maomi.jpeg"))
                        .setContentType(MediaType.IMAGE_JPEG_VALUE)
                        .setOriginalFilename("umaomi.jpeg")
                        .setFilename("小猫咪.jpeg")
                        .setDirPath("a/b/c/")
                        .setCalculateHashMd5(true)
                        .setThWidtHeight(80, 80)
        );
        System.err.println(JSON.toJSONString(upload, JSONWriter.Feature.PrettyFormat));
        Assert.isTrue(StringUtils.isNotBlank(upload.getUrl()), "url 不应为空");
    }
}