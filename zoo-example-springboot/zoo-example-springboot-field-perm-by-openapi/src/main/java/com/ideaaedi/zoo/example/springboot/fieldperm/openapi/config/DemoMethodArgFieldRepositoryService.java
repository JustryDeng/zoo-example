package com.ideaaedi.zoo.example.springboot.fieldperm.openapi.config;

import com.ideaaedi.zoo.commonbase.zoo_util.ZooContext;
import com.ideaaedi.zoo.diy.feature.fieldperm.api.entity.MethodArgFieldInfo;
import com.ideaaedi.zoo.diy.feature.fieldperm.api.spi.MethodArgFieldRepositoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class DemoMethodArgFieldRepositoryService implements MethodArgFieldRepositoryService {
    
    private static final Map<String, List<MethodArgFieldInfo>> SIMPLE_DB = new ConcurrentHashMap<>();
    
    @Override
    public void initSavaOrUpdate(@Nonnull List<MethodArgFieldInfo> methodArgFieldInfoList) {
        SIMPLE_DB.putAll(
                methodArgFieldInfoList.stream().collect(Collectors.groupingBy(MethodArgFieldInfo::getMethodUid))
        );
    }
    
    @Nonnull
    @Override
    public Pair<Boolean, List<MethodArgFieldInfo>> ifPermAndNotAllowedList(@Nonnull String methodUid) {
        // step1. 获取当前用户id
        
        // step2. 关联查询当前用户的字段权限信息
        
        // step3. 如果当前用户没有任何字段权限限制，那么直接返回即可
        // return Pair.of(false, null);
        
        // step3. 如果当前用户有字段权限限制，这里返回对应不允许修改的字段信息
        
        // ----------------- mock如下数据，请运行单元测试观察效果 -----------------
        // 假设用户id从请求头中取
        HttpServletRequest httpServletRequest = ZooContext.Http.httpServletRequest();
        String currUserId = Objects.requireNonNull(httpServletRequest).getHeader("CURR_USER_ID");
        int CURR_USER_ID = StringUtils.isBlank(currUserId) ? 0 : Integer.parseInt(currUserId);
        // 假设不同的用户，有不同的 字段限制
        List<MethodArgFieldInfo> methodArgFieldInfos = SIMPLE_DB.get(methodUid);
        List<MethodArgFieldInfo> notAllowSetValueFieldList = switch (CURR_USER_ID) {
            case 1 -> methodArgFieldInfos.stream()
                    // 用户1访问时，不能设置name的值
                    .filter(x -> StringUtils.equalsAny(x.getFieldName(), "name"))
                    .toList();
            case 2 -> methodArgFieldInfos.stream()
                    // 用户2访问时，不能设置path的值
                    .filter(x -> StringUtils.equalsAny(x.getFieldName(), "path"))
                    .toList();
            case 3 -> methodArgFieldInfos.stream()
                    // 用户3访问时，不能设置deptId的值
                    .filter(x -> StringUtils.equalsAny(x.getFieldName(), "deptId"))
                    .toList();
            case 4 -> methodArgFieldInfos.stream()
                    // 用户4访问时，不能设置deptName的值
                    .filter(x -> StringUtils.equalsAny(x.getFieldName(), "deptName"))
                    .toList();
            default -> throw new IllegalArgumentException();
        };
        ;
        return Pair.of(true, notAllowSetValueFieldList);
    }
}
