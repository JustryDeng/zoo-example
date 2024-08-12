package com.ideaaedi.zoo.example.springboot.file.xfilestorage;

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
 * 其它测试
 *
 * @author <font size = "20" color = "#3CAA3C"><a href="https://gitee.com/JustryDeng">JustryDeng</a></font> <img
 * src="https://gitee.com/JustryDeng/shared-files/raw/master/JustryDeng/avatar.jpg" />
 * @since 1.0.0
 */
@Slf4j
@SpringBootTest(classes = ExampleSpringbootXFileStorageApplication.class)
public class OtherTest {
    
    @Resource
    private FileUploadDownload fileUploadDownload;
    
    @Resource
    private SpringFileStorageProperties fileStorageProperties;
    
    String testResourceDir = PathUtil.getProjectRootDir(OtherTest.class)
            .replace("/target/classes/",
                    "/src/test/resources/");
    
    private volatile boolean isInit = false;
    
    @BeforeEach
    public void init() {
        if (isInit) {
            return;
        }
        // 这里通过代码保证对应的minio桶存在（注：一般来讲，生产环境最好手动创建桶）
        MinioUtil.createBucketIfNecessary(fileStorageProperties.getMinio());
        isInit = true;
    }
    
    /**
     * <pre>
     * 若出现类似
     * {@code
     * code = SlowDown, message = A timeout exceeded while waiting to proceed with the request, please reduce your request rate
     * }
     * 这样的报错，可以从这几个方面入手检查：
     * 1.使用的minio sdk与部署的minio版本是否匹配，如果差太多可能也会高频偶发上述的问题
     * 2.是否关闭了相关流
     *
     * 提示：本人使用的minio版本为：RELEASE.2024-07-04T14-25-45Z， 对应的sdk版本为：io.minio:minio:8.4.3
     * </pre>
     *
     * @see <a href="https://stackoverflow.com/questions/58433594/aws-s3-slowdown-please-reduce-your-request-rate">stackoverflow</a>
     */
    @Test
    @SneakyThrows
    void testIo() {
        FileInfoDTO randomFileInfo = demoFile();
        for (int i = 0; i < 1000; i++) {
            byte[] download = fileUploadDownload.download(randomFileInfo.getFilepath());
            System.err.printf("第%s次获取：%s", i + 1, download.hashCode());
            System.err.println();
        }
        TimeUnit.MILLISECONDS.sleep(2000);
    }
    
    @Test
    @SneakyThrows
    private FileInfoDTO demoFile() {
        FileInfoDTO upload = fileUploadDownload.upload(
                FileUploadDTO.of(new File(testResourceDir, "maomi.jpeg"))
                        .setContentType(MediaType.IMAGE_JPEG_VALUE)
                        .setOriginalFilename("xiaomaomi.jpeg")
                        .setDirPath("a/b/" + System.currentTimeMillis() + "/")
                        .setCalculateHashMd5(true)
                        .setThWidtHeight(80, 80)
        );
        Assert.isTrue(StringUtils.isNotBlank(upload.getUrl()), "url 不应为空");
        return upload;
    }
}