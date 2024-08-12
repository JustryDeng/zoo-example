package com.ideaaedi.zoo.example.springboot.auth.satoken.config;

import com.google.common.collect.Lists;
import com.ideaaedi.zoo.commonbase.zoo_component.tenantlike.DefaultTenantScope;
import com.ideaaedi.zoo.commonbase.zoo_component.tenantlike.TenantScope;
import com.ideaaedi.zoo.diy.artifact.auth.satoken.permission.AbstractPermissionProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 获取用户权限
 *
 * @author <font size = "20" color = "#3CAA3C"><a href="https://gitee.com/JustryDeng">JustryDeng</a></font> <img
 * src="https://gitee.com/JustryDeng/shared-files/raw/master/JustryDeng/avatar.jpg" />
 * @since 1.0.0
 */
@Component
public class DemoPermissionProvider extends AbstractPermissionProvider {
    
    public static final long ZHANG_SAN_ID = 1L;
    public static final String ZHANG_SAN_NAME = "admin张三";
    public static final long LI_SI_ID = 2L;
    public static final String LI_SI_NAME = "dba李四";
    public static final long WANG_WU_ID = 3L;
    public static final String WANG_WU_NAME = "developer王五";
    
    /*
     * 模拟 用户id - 用户拥有的权限
     */
    public static final Map<Long, List<String>> userIdAndPermissionMap;
    
    public static final String adminPath = "/admin";
    public static final String dbaPath = "/dba";
    public static final String developerPath = "/developer";
    static {
        userIdAndPermissionMap = Map.of(
                ZHANG_SAN_ID, Lists.newArrayList(adminPath, dbaPath, developerPath),
                LI_SI_ID, Lists.newArrayList(dbaPath),
                WANG_WU_ID, Lists.newArrayList(developerPath)
        );
    }
    
    /*
     * 模拟 用户id - 用户名
     */
    public static final Map<Long, String> userIdAndNameMap = Map.of(
            ZHANG_SAN_ID, ZHANG_SAN_NAME,
            LI_SI_ID, LI_SI_NAME,
            WANG_WU_ID, WANG_WU_NAME
    );
    
    
    /**
     * 获取权限
     *
     * @param loginId：账号id，即你在调用 StpUtil.login(id) 时写入的标识值。
     * @param loginType：账号体系标识
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return userIdAndPermissionMap.get(Long.parseLong(String.valueOf(loginId)));
    }
    
    /**
     * 获取角色
     *
     * @param loginId：账号id，即你在调用 StpUtil.login(id) 时写入的标识值。
     * @param loginType：账号体系标识
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return Lists.newArrayList();
    }
    
    /**
     * 获取用户数据域信息
     *
     * @param loginType 账号类别
     * @param loginId 账号id
     * @param tokenValue 本次登录产生的 token 值
     *
     * @return 用户的数据域
     */
    @Override
    protected TenantScope getTenantScope(String loginType, Object loginId, String tokenValue) {
        /*
         * 数据域层级说明，详见TenantScope类注释
         */
        return DefaultTenantScope.of(
                "1,",
                Lists.newArrayList("1,3,", "1,7,"),
                Lists.newArrayList("1,3")
        );
    }
}
