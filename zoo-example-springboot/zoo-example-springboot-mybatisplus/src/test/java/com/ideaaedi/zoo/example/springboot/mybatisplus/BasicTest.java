package com.ideaaedi.zoo.example.springboot.mybatisplus;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ideaaedi.zoo.example.springboot.mybatisplus.po.UserInfoPO;
import com.ideaaedi.zoo.example.springboot.mybatisplus.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest(classes = ExampleSpringbootMybatisPlusApplication.class)
public class BasicTest {
    
    @Autowired
    private UserInfoService userInfoService;
    
    @BeforeEach
    public void tips() {
        System.err.println("运行单元测试前，请确保已初始化数据库脚本：src/main/resources/sql/user_info.sql");
    }
    
    /*
     * 列表
     */
    @Test
    void list() {
        List<UserInfoPO> list = userInfoService.list();
        System.out.println
                (JSON.toJSONString(list, JSONWriter.Feature.NotWriteDefaultValue, JSONWriter.Feature.PrettyFormat)
                );
        Assertions.assertEquals(2, list.size());
    }
    
    /*
     * 分页
     */
    @Test
    void page() {
        IPage<UserInfoPO> page = new Page<>(1, 1);
        page = userInfoService.page(page, Wrappers.emptyWrapper());
        System.out.println(
                JSON.toJSONString(page, JSONWriter.Feature.NotWriteDefaultValue, JSONWriter.Feature.PrettyFormat)
        );
        Assertions.assertEquals(2, page.getPages());
        Assertions.assertEquals(2, page.getTotal());
        Assertions.assertEquals(1, page.getRecords().size());
    }
    
    /*
     * 硬删除
     * <br /
     * 此方法运行后（因为会硬删除数据），需要重新执行数据库脚本
     */
    @Test
    void forceDelete() {
        List<UserInfoPO> list = userInfoService.forceList();
        System.out.println
                ("物理删除前：" + JSON.toJSONString(list, JSONWriter.Feature.NotWriteDefaultValue, JSONWriter.Feature.PrettyFormat)
                );
        Assertions.assertEquals(3, list.size());
    
        userInfoService.forceDelete(
                Wrappers.lambdaQuery(UserInfoPO.class)
                        .eq(UserInfoPO::getId, 1)
        );
        
        list = userInfoService.forceList();
        System.out.println
                ("物理删除后：" + JSON.toJSONString(list, JSONWriter.Feature.NotWriteDefaultValue, JSONWriter.Feature.PrettyFormat)
                );
        Assertions.assertEquals(2, list.size());
    }
}