package com.ideaaedi.zoo.example.springboot.file.xfilestorage;

import com.ideaaedi.zoo.commonbase.exception.MinIOOperateException;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dromara.x.file.storage.core.FileStorageProperties;
import org.dromara.x.file.storage.spring.SpringFileStorageProperties;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public final class MinioUtil {
    
    private static final Map<String, MinioClient> instanceMap = new ConcurrentHashMap<>(4);
    
    private static final Set<String> existedBucket = Collections.synchronizedSet(new HashSet<>(8));
    
    /**
     * 确保桶存在
     */
    @SneakyThrows
    public static void createBucketIfNecessary(List<? extends SpringFileStorageProperties.SpringMinioConfig> minioConfigList){
        for (FileStorageProperties.MinioConfig minConf : minioConfigList) {
            // 初始化MinioClient
            String endPoint = minConf.getEndPoint();
            String accessKey = minConf.getAccessKey();
            String secretKey = minConf.getSecretKey();
            String bucket = minConf.getBucketName();
            
            String instanceId = String.join("_", endPoint, accessKey, secretKey);
            MinioClient minioClient = instanceMap.get(instanceId);
            if (minioClient == null) {
                minioClient = MinioClient.builder()
                        .endpoint(endPoint)
                        .credentials(accessKey, secretKey)
                        .build();
                instanceMap.put(instanceId, minioClient);
            }
            
            // 保证桶存在
            if (!existedBucket.contains(instanceId + "_" + bucket)) {
                if (!bucketExists(minioClient, bucket)) {
                    log.warn("minio({}) bucket {} non-exist, auto create it.", endPoint, bucket);
                    // 创建桶，并设置Access Rules为：所有人*都可读
                    makeBucket(minioClient, bucket);
                    String policy = String.format("{\n"
                            + "    \"Version\": \"2012-10-17\",\n"
                            + "    \"Statement\": [\n"
                            + "        {\n"
                            + "            \"Effect\": \"Allow\",\n"
                            + "            \"Principal\": {\n"
                            + "                \"AWS\": [\n"
                            + "                    \"*\"\n"
                            + "                ]\n"
                            + "            },\n"
                            + "            \"Action\": [\n"
                            + "                \"s3:GetBucketLocation\"\n"
                            + "            ],\n"
                            + "            \"Resource\": [\n"
                            + "                \"arn:aws:s3:::%s\"\n"
                            + "            ]\n"
                            + "        },\n"
                            + "        {\n"
                            + "            \"Effect\": \"Allow\",\n"
                            + "            \"Principal\": {\n"
                            + "                \"AWS\": [\n"
                            + "                    \"*\"\n"
                            + "                ]\n"
                            + "            },\n"
                            + "            \"Action\": [\n"
                            + "                \"s3:ListBucket\"\n"
                            + "            ],\n"
                            + "            \"Resource\": [\n"
                            + "                \"arn:aws:s3:::%s\"\n"
                            + "            ],\n"
                            + "            \"Condition\": {\n"
                            + "                \"StringEquals\": {\n"
                            + "                    \"s3:prefix\": [\n"
                            + "                        \"*\"\n"
                            + "                    ]\n"
                            + "                }\n"
                            + "            }\n"
                            + "        },\n"
                            + "        {\n"
                            + "            \"Effect\": \"Allow\",\n"
                            + "            \"Principal\": {\n"
                            + "                \"AWS\": [\n"
                            + "                    \"*\"\n"
                            + "                ]\n"
                            + "            },\n"
                            + "            \"Action\": [\n"
                            + "                \"s3:GetObject\"\n"
                            + "            ],\n"
                            + "            \"Resource\": [\n"
                            + "                \"arn:aws:s3:::%s/**\"\n"
                            + "            ]\n"
                            + "        }\n"
                            + "    ]\n"
                            + "}", bucket, bucket, bucket);
                    // 设置策略
                    minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucket).config(policy).build());
                }
                existedBucket.add(instanceId + "_" + bucket);
            }
        }
    }
    
    
    /**
     * 查看存储bucket是否存在
     */
    private static boolean bucketExists(MinioClient minioClient, String bucketName) {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            log.error("bucketExists occur exception. e.getMessage() -> {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * 创建存储bucket
     */
    private static void makeBucket(MinioClient minioClient, String bucketName) {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            throw new MinIOOperateException(String.format("makeBucket %s fail.", bucketName), e);
        }
    }
}
