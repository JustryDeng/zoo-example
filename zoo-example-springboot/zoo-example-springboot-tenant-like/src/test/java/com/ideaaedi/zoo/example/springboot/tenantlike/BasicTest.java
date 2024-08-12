package com.ideaaedi.zoo.example.springboot.tenantlike;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.ideaaedi.zoo.commonbase.zoo_component.tenantlike.DefaultTenantScope;
import com.ideaaedi.zoo.commonbase.zoo_util.ZooContext;
import com.ideaaedi.zoo.example.springboot.tenantlike.po.UserInfoLikePO;
import com.ideaaedi.zoo.example.springboot.tenantlike.service.UserInfoLikeService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest(classes = ExampleSpringbootTenantLikeApplication.class)
public class BasicTest {
    
    @Autowired
    private UserInfoLikeService userInfoService;
    
    @BeforeEach
    public void tips() {
        System.err.println("运行单元测试前，请确保已初始化数据库脚本：src/main/resources/sql/user_info_like.sql");
    }
    
    /*
     * 对比观察租户数据域
     */
    @Test
    @SneakyThrows
    void tenantDataScope() {
        // 四个参数分别是： 用户id，用户所属租户，用户可读数据域，用户可写数据域， 这里因为是查询数据，我们只需关注第三个参数即可
        ZooContext.setUserIdAndTenantScope(
                9527L,
                DefaultTenantScope.of(
                        "1,",
                        new LinkedHashSet<>(Lists.newArrayList("1,")),
                        new LinkedHashSet<>(Lists.newArrayList("1,"))
                )
        );
        try {
            List<UserInfoLikePO> list = userInfoService.list();
            System.out.println("租户读域【1,】能看到的数据：");
            list.forEach(System.out::println);
            Assertions.assertEquals(5, list.size());
            TimeUnit.MILLISECONDS.sleep(100);
        } finally {
            ZooContext.clear();
        }
    
        ZooContext.setUserIdAndTenantScope(
                9527L,
                DefaultTenantScope.of(
                        "1,",
                        new LinkedHashSet<>(Lists.newArrayList("1,2,")),
                        new LinkedHashSet<>(Lists.newArrayList("1,"))
                )
        );
        try {
            List<UserInfoLikePO> list = userInfoService.list();
            System.out.println("租户读域【1,2,】能看到的数据：");
            list.forEach(System.out::println);
            Assertions.assertEquals(2, list.size());
            TimeUnit.MILLISECONDS.sleep(100);
        } finally {
            ZooContext.clear();
        }
    
        ZooContext.setUserIdAndTenantScope(
                9527L,
                DefaultTenantScope.of(
                        "1,",
                        new LinkedHashSet<>(Lists.newArrayList("1,3,31,")),
                        new LinkedHashSet<>(Lists.newArrayList("1,"))
                )
        );
        try {
            List<UserInfoLikePO> list = userInfoService.list();
            System.out.println("租户读域【1,3,31,】能看到的数据：");
            list.forEach(System.out::println);
            Assertions.assertEquals(1, list.size());
        } finally {
            ZooContext.clear();
        }
    }
    
    /*
     * 对比观察租户数据域（同时多个读域）
     */
    @Test
    @SneakyThrows
    void tenantDataScope2() {
        // 四个参数分别是： 用户id，用户所属租户，用户可读数据域，用户可写数据域， 这里因为是查询数据，我们只需关注第三个参数即可
        ZooContext.setUserIdAndTenantScope(
                9527L,
                DefaultTenantScope.of(
                        "1,",
                        new LinkedHashSet<>(Lists.newArrayList("1,3,31,", "1,2,")),
                        new LinkedHashSet<>(Lists.newArrayList("1,"))
                )
        );
        try {
            List<UserInfoLikePO> list = userInfoService.list();
            System.out.println("租户读域【1,3,31,】和【1,2,】能看到的数据：");
            list.forEach(System.out::println);
            Assertions.assertEquals(3, list.size());
            TimeUnit.MILLISECONDS.sleep(100);
        } finally {
            ZooContext.clear();
        }
        
        ZooContext.setUserIdAndTenantScope(
                9527L,
                DefaultTenantScope.of(
                        "1,",
                        new LinkedHashSet<>(Lists.newArrayList("1,3,31,", "1,2,21,")),
                        new LinkedHashSet<>(Lists.newArrayList("1,"))
                )
        );
        try {
            List<UserInfoLikePO> list = userInfoService.list();
            System.out.println("租户读域【1,3,31,】和【1,2,21,】能看到的数据：");
            list.forEach(System.out::println);
            Assertions.assertEquals(2, list.size());
        } finally {
            ZooContext.clear();
        }
    }
    
    /*
     * test：逻辑删除会被租户拦截器拦截
     */
    @Test
    void tenantDelete() {
        // 四个参数分别是： 用户id，用户所属租户，用户可读数据域，用户可写数据域， 这里因为是写数据（增删改都属于写），我们只需关注第四个参数即可
        ZooContext.setUserIdAndTenantScope(
                9527L,
                DefaultTenantScope.of(
                        "1,",
                        new LinkedHashSet<>(Lists.newArrayList("1,")),
                        new LinkedHashSet<>(Lists.newArrayList("1,2,3,"))
                )
        );
        try {
            boolean deleteSuc = userInfoService.remove(
                    Wrappers.lambdaQuery(UserInfoLikePO.class)
                            .eq(UserInfoLikePO::getName, "张三")
            );
            Assertions.assertFalse(deleteSuc, "张三tenant为【1,】，当前写数据域为【1,2,3,】，所以逻辑删除应该是失败才对");
        } finally {
            ZooContext.clear();
        }
    
        ZooContext.setUserIdAndTenantScope(
                9527L,
                DefaultTenantScope.of("1,",
                        new LinkedHashSet<>(Lists.newArrayList("1,")),
                        new LinkedHashSet<>(Lists.newArrayList("1,"))
                )
        );
        try {
            List<UserInfoLikePO> list = userInfoService.list();
            System.out.println("此时的全量数据：");
            list.forEach(System.out::println);
            Assertions.assertEquals(5, list.size());
        } finally {
            ZooContext.clear();
        }
    }
    
    
    /*
     * test：硬删除会被租户拦截器拦截
     */
    @Test
    void tenantForceDelete() {
        // 四个参数分别是： 用户id，用户所属租户，用户可读数据域，用户可写数据域， 这里因为是写数据（增删改都属于写），我们只需关注第四个参数即可
        ZooContext.setUserIdAndTenantScope(
                9527L,
                DefaultTenantScope.of("1,",
                        new LinkedHashSet<>(Lists.newArrayList("1,")),
                        new LinkedHashSet<>(Lists.newArrayList("1,2,3,"))
                )
        );
        try {
            int affectedRowCount = userInfoService.forceDelete(
                    Wrappers.lambdaQuery(UserInfoLikePO.class)
                            .eq(UserInfoLikePO::getName, "张三")
            );
            Assertions.assertEquals(affectedRowCount, 0, "张三tenant为【1,】，当前写数据域为【1,2,3,】，所以硬删除应该是失败才对");
        } finally {
            ZooContext.clear();
        }
    
        ZooContext.setUserIdAndTenantScope(
                9527L,
                DefaultTenantScope.of(
                        "1,",
                        new LinkedHashSet<>(Lists.newArrayList("1,")),
                        new LinkedHashSet<>(Lists.newArrayList("1,"))
                )
        );
        try {
            List<UserInfoLikePO> list = userInfoService.list();
            System.out.println("此时的全量数据：");
            list.forEach(System.out::println);
            Assertions.assertEquals(5, list.size());
        } finally {
            ZooContext.clear();
        }
    }
}