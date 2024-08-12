package com.ideaaedi.zoo.example.springboot.bpmn.flowable.config;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.google.common.collect.Lists;
import com.ideaaedi.commonds.path.PathUtil;
import com.ideaaedi.zoo.commonbase.entity.PageInfo;
import com.ideaaedi.zoo.diy.feature.bpmn.api.entity.dto.form.BpmnFormCreate;
import com.ideaaedi.zoo.diy.feature.bpmn.api.entity.dto.form.BpmnFormQuery;
import com.ideaaedi.zoo.diy.feature.bpmn.api.entity.facade.form.BpmnFormData;
import com.ideaaedi.zoo.diy.feature.bpmn.api.entity.facade.form.DefaultBpmnFormData;
import com.ideaaedi.zoo.diy.feature.bpmn.api.service.form.BpmnFormRepositoryService;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * (non-javadoc)
 *
 * @author <font size = "20" color = "#3CAA3C"><a href="https://gitee.com/JustryDeng">JustryDeng</a></font> <img
 * src="https://gitee.com/JustryDeng/shared-files/raw/master/JustryDeng/avatar.jpg" />
 * @since 1.0.0
 */
@Service
public class DemoFormRepositoryServiceImpl implements BpmnFormRepositoryService {
    
    /**
     * 表单定义表的创建，示例见{@link BpmnFormRepositoryService}上的注释
     */
    private static final File file_db = new File(PathUtil.getProjectRootDir(DemoFormRepositoryServiceImpl.class),
            "flw_form_definition.table");
    
     static {
         ///file_db.delete();
         if (!file_db.exists()) {
             try {
                 file_db.createNewFile();
             } catch (IOException e) {
                 throw new RuntimeException(e);
             }
         }
     }
    
    @Nonnull
    @Override
    @SneakyThrows
    public BpmnFormData create(@Nonnull String tenant, @Nonnull BpmnFormCreate bpmnFormCreate) {
        List<String> list = FileUtils.readLines(file_db, StandardCharsets.UTF_8);
        Set<String> existFormKeySet = list.stream().map(line -> JSON.parseObject(line,
                        DefaultBpmnFormData.class))
                .map(BpmnFormData::getFormKey)
                .collect(Collectors.toSet());
        if (existFormKeySet.contains(bpmnFormCreate.getFormKey())) {
            /*
             * 如果在本demo中，出现脏数据导致进入这里，你可以在准备file_db文件前，先删除一下，避免之前存的脏表单数据的影响  file_db.delete();
             */
            throw new IllegalStateException("表单key已存在");
        }
        DefaultBpmnFormData defaultBpmnFormData = DefaultBpmnFormData.builder()
                .id(IdUtil.simpleUUID())
                .formName(bpmnFormCreate.getFormName())
                .formDescription(bpmnFormCreate.getFormDescription())
                .tenant(tenant)
                .formKey(bpmnFormCreate.getFormKey())
                .formProperties(bpmnFormCreate.getFormProperties())
                .category(bpmnFormCreate.getCategory())
                .build();
        
        String formJson = JSON.toJSONString(defaultBpmnFormData, JSONWriter.Feature.NotWriteDefaultValue);
        FileUtils.writeLines(file_db, Lists.newArrayList(formJson), true);
        return defaultBpmnFormData;
    }
    
    @Nonnull
    @Override
    public BpmnFormData delete(@Nonnull String tenant, @Nonnull String formId) {
        throw new UnsupportedOperationException("请自己实现");
    }
    
    @Nonnull
    @Override
    public List<? extends BpmnFormData> list(@Nonnull String tenant, @Nullable BpmnFormQuery bpmnFormQuery) {
        throw new UnsupportedOperationException("请自己实现");
    }
    
    @Nonnull
    @Override
    public PageInfo<? extends BpmnFormData> page(@Nonnull String tenant, @Nullable BpmnFormQuery bpmnFormQuery,
                                                 int pageNum, int pageSize) {
        throw new UnsupportedOperationException("请自己实现");
    }
    
    @Nullable
    @Override
    @SneakyThrows
    public BpmnFormData detailById(@Nonnull String tenant, @Nonnull String formId) {
        List<String> list = FileUtils.readLines(file_db, StandardCharsets.UTF_8);
        Map<String, DefaultBpmnFormData> map = list.stream().map(line -> JSON.parseObject(line,
                        DefaultBpmnFormData.class))
                .collect(Collectors.toMap(BpmnFormData::getId, Function.identity()));
        DefaultBpmnFormData defaultBpmnFormData = map.get(formId);
        if (defaultBpmnFormData == null) {
            return null;
        }
        String dbTenant = defaultBpmnFormData.getTenant();
        // 假设租户架构是前缀树架构（即：右模糊匹配租户）
        if (dbTenant == null || !dbTenant.startsWith(tenant)) {
            return null;
        }
        return defaultBpmnFormData;
    }
    
    @Nullable
    @Override
    @SneakyThrows
    public BpmnFormData detailByKey(@Nonnull String tenant, @Nonnull String formKey) {
        List<String> list = FileUtils.readLines(file_db, StandardCharsets.UTF_8);
        Map<String, DefaultBpmnFormData> map = list.stream().map(line -> JSON.parseObject(line,
                        DefaultBpmnFormData.class))
                .collect(Collectors.toMap(BpmnFormData::getFormKey, Function.identity()));
        DefaultBpmnFormData defaultBpmnFormData = map.get(formKey);
        if (defaultBpmnFormData == null) {
            return null;
        }
        String dbTenant = defaultBpmnFormData.getTenant();
        // 假设租户架构是前缀树架构（即：右模糊匹配租户）
        if (dbTenant == null || !dbTenant.startsWith(tenant)) {
            return null;
        }
        return defaultBpmnFormData;
    }
}
