package com.ideaaedi.zoo.example.springboot.dynamic.datasource;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.ideaaedi.zoo.example.springboot.dynamic.datasource.entity.po.DemoPO;
import com.ideaaedi.zoo.example.springboot.dynamic.datasource.entity.po.PayRecordPO;
import com.ideaaedi.zoo.example.springboot.dynamic.datasource.entity.po.TransRecordPO;
import com.ideaaedi.zoo.example.springboot.dynamic.datasource.entity.po.UserInfoPO;
import com.ideaaedi.zoo.example.springboot.dynamic.datasource.mapper.DemoMapper;
import com.ideaaedi.zoo.example.springboot.dynamic.datasource.mapper.PayRecordMapper;
import com.ideaaedi.zoo.example.springboot.dynamic.datasource.mapper.TransRecordMapper;
import com.ideaaedi.zoo.example.springboot.dynamic.datasource.mapper.UserInfoMapper;
import jakarta.annotation.Resource;
import org.apache.shardingsphere.sharding.rule.ShardingRule;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

/**
 * 此单元测试可知，在进行联表查询时，会自动笛卡尔积(n×n)进行查询。当然，你可以通过分片字段进行条件过滤，控
 * 制进行笛卡尔积时双方分表的数量, 达到提升性能的效果，甚至达到(1×1)的效果
 * <br />
 * <br />
 * <pre>
 * sharding官方针对这种情况，也提供有解决方案，那就是表绑定：
 * 如：设置spring.shardingsphere.rules.sharding.bindingTables[0]=trans_record, pay_record，那么当关联查询时，就会走绑定关联（而不走笛卡尔关联），即：
 *     trans_record_202405 会自动只关联 pay_record_202405 进行查询,
 *     trans_record_202406 会自动只关联 pay_record_202406 进行查询,
 *     不会出现trans_record_202405关联pay_record_202406、trans_record_202406关联pay_record_202406的情况
 *
 *     当然，sharding对相互绑定的一批表有些要求，如：分库节点要对应都有、分表节点要对应都有、使用相同的分库分表算法，详见{@link ShardingRule#isValidBindingTableConfiguration}
 * </pre>
 *
 *
 * @author <font size = "20" color = "#3CAA3C"><a href="https://gitee.com/JustryDeng">JustryDeng</a></font> <img src="https://gitee.com/JustryDeng/shared-files/raw/master/JustryDeng/avatar.jpg" />
 * @since 2024-05-06 16:30:35
 */
@SpringBootTest
class ShardingTests {
    
    @Resource
    TransRecordMapper transRecordMapper;
    
    @Resource
    PayRecordMapper payRecordMapper;
    
    @Resource
    UserInfoMapper userInfoMapper;
    
    @Resource
    DemoMapper demoMapper;
    
    /**
     * 有分表trans_record  join   没分表user_info
     * <p>
     * 结论：笛卡尔积式所有分表trans_record逐个进行联表user_info查询
     */
    @Test
    void testOne() {
        // 准备数据 (单一变量法, 为了排除可能存在的影响， 可以先将之前测试时产生的表删除)
        prepareData();
        
        List<DemoPO> demoList = demoMapper.listOne();
        demoList.forEach(System.err::println);
    }
    
    /**
     * 有分表trans_record  join   没分表user_info
     * <p>
     * 结论：先根据分表字段，对分表trans_record进行筛选。 然后用筛选后剩下的分表trans_record笛卡尔积式逐个进行联表user_info查询
     */
    @Test
    void testTwo() {
        // 准备数据 (单一变量法, 为了排除可能存在的影响， 可以先将之前测试时产生的表删除)
        prepareData();
        
        List<DemoPO> demoList = demoMapper.listTwo("2024-05-01", "2024-06-30");
        demoList.forEach(System.err::println);
    }
    
    /**
     * 有分表trans_record  join   没分表user_info
     * <p>
     * 结论：对非分片列进行条件过滤， 无法达到减少笛卡尔积联表查询次数的目的
     */
    @Test
    void testTwo2() {
        // 准备数据 (单一变量法, 为了排除可能存在的影响， 可以先将之前测试时产生的表删除)
        prepareData();
        
        List<DemoPO> demoList = demoMapper.listTwo2("备注X");
        demoList.forEach(System.err::println);
    }
    
    /**
     * 有分表trans_record  join   有分表pay_record
     * <ul>
     *     <li>
     *         注释掉配置中的bindingTables绑定，运行测试时（观察控制台SQL执行记录）：<br /><br />
     *         结论：笛卡尔积式所有分表trans_record逐个进行联表user_info查询
     *         <br /><br />
     *     </li>
     *     <li>
     *         启用配置中的bindingTables绑定，运行测试时（观察控制台SQL执行记录）：<br /><br />
     *         结论：因为配置文件中通过bindingTables对这两个表进行了绑定，所以最终实际执行的sql条数是 12 * 1 = 12（而不是12 * 12 = 144）
     *     </li>
     * </ul>
     */
    @Test
    void testThree() {
        // 准备数据 (单一变量法, 为了排除可能存在的影响， 可以先将之前测试时产生的表删除)
        prepareData();
        
        List<DemoPO> demoList = demoMapper.listThree();
        demoList.forEach(System.err::println);
    }
    
    /**
     * 有分表trans_record  join   有分表pay_record
     * <ul>
     *     <li>
     *         注释掉配置中的bindingTables绑定，运行测试时（观察控制台SQL执行记录）：<br /><br />
     *         结论：先根据各自的分表字段，对分表trans_record、分表pay_record进行筛选。 然后用筛选后剩下的分表trans_record笛卡尔积式逐个联表剩下的分表pay_record查询
     *         <br /><br />
     *     </li>
     *     <li>
     *         启用配置中的bindingTables绑定，运行测试时（观察控制台SQL执行记录）：<br /><br />
     *         结论：先根据各自的分表字段，对分表trans_record、分表pay_record进行筛选。 同时因为配置文件中通过bindingTables对这两个表进行了绑定，
     *              所以过滤后只会执行 trans_record_202406 join pay_record_202406 或 trans_record_202407 join pay_record_202407的查询，
     *              因为SQL中trans_record在前面，即先解析到trans_record的真实表，所以相关绑定表都以trans_record的时间为准，所以最终执行的是
     *              trans_record_202406 join pay_record_202406
     *     </li>
     * </ul>
     */
    @Test
    void testFour() {
        // 准备数据 (单一变量法, 为了排除可能存在的影响， 可以先将之前测试时产生的表删除)
        prepareData();
        
        List<DemoPO> demoList = demoMapper.listFour("2024-06-01", "2024-06-30",
                "2024-07-01", "2024-07-31");
        demoList.forEach(System.err::println);
    }
    
    /**
     * 有分表 join 有分表 join 没分表
     * <ul>
     *     <li>
     *         注释掉配置中的bindingTables绑定，运行测试时（观察控制台SQL执行记录）：<br /><br />
     *         结论：与前面的规律保持一致， 笛卡尔积式联表查询
     *         <br /><br />
     *     </li>
     *     <li>
     *         启用配置中的bindingTables绑定，运行测试时（观察控制台SQL执行记录）：<br /><br />
     *         结论：因为配置文件中通过bindingTables对这两个表进行了绑定，所以最终实际执行的sql条数是 12 * 1 * 1 = 12（而不是12 * 12 * 1 = 144）
     *     </li>
     * </ul>
     */
    @Test
    void testFive() {
        // 准备数据 (单一变量法, 为了排除可能存在的影响， 可以先将之前测试时产生的表删除)
        prepareData();
        
        List<DemoPO> demoList = demoMapper.listFive();
        demoList.forEach(System.err::println);
    }
    
    /**
     * 有分表 join 有分表 join 没分表
     * <ul>
     *     <li>
     *         注释掉配置中的bindingTables绑定，运行测试时（观察控制台SQL执行记录）：<br /><br />
     *         结论：与前面的规律保持一致， 先各自过滤，然后剩下的进行笛卡尔积式联表查询
     *         <br /><br />
     *     </li>
     *     <li>
     *         启用配置中的bindingTables绑定，运行测试时（观察控制台SQL执行记录）：<br /><br />
     *         结论：先根据各自的分表字段，对分表trans_record、分表pay_record进行筛选。 同时因为配置文件中通过bindingTables对这两个表进行了绑定，
     *               所以过滤后只会执行 trans_record_202406 join pay_record_202406 join user_info 或 trans_record_202407 join pay_record_202407 join user_info的查询，
     *               因为SQL中trans_record在前面，即先解析到trans_record的真实表，所以相关绑定表都以trans_record的时间为准，所以最终执行的是
     *               trans_record_202406 join pay_record_202406 join user_info
     *     </li>
     * </ul>
     */
    @Test
    void testSix() {
        // 准备数据 (单一变量法, 为了排除可能存在的影响， 可以先将之前测试时产生的表删除)
        prepareData();
        
        List<DemoPO> demoList = demoMapper.listSix("2024-06-01", "2024-06-30",
                "2024-07-01", "2024-07-31");
        demoList.forEach(System.err::println);
    }
    
    void prepareData() {
        // 清空数据
        transRecordMapper.delete(null);
        payRecordMapper.delete(null);
        userInfoMapper.delete(null);
        
        long userId = IdWorker.getId();
        
        // 插入数据
        transRecordMapper.insert(TransRecordPO.builder()
                .id(IdWorker.getId())
                .transDate("2024-03-06")
                .remark("备注X")
                .userId(100L)
                .build());
        transRecordMapper.insert(TransRecordPO.builder()
                .id(IdWorker.getId())
                .transDate("2024-05-06")
                .remark("备注A")
                .userId(userId)
                .build());
        payRecordMapper.insert(PayRecordPO.builder()
                .id(IdWorker.getId())
                .payDate("2024-05-06")
                .amount(new BigDecimal("2.5"))
                .transId(IdWorker.getId())
                .build());
        userInfoMapper.insert(UserInfoPO.builder()
                .id(userId)
                .name("张三")
                .build());
    
        long transId = IdWorker.getId();
        userId = IdWorker.getId();
        transRecordMapper.insert(TransRecordPO.builder()
                .id(transId)
                .transDate("2024-06-06")
                .remark("备注B")
                .userId(userId)
                .build());
        payRecordMapper.insert(PayRecordPO.builder()
                .id(IdWorker.getId())
                .payDate("2024-06-06")
                .amount(new BigDecimal("8.3"))
                .transId(transId)
                .build());
        payRecordMapper.insert(PayRecordPO.builder()
                .id(IdWorker.getId())
                .payDate("2024-07-06")
                .amount(new BigDecimal("8.4"))
                .transId(transId)
                .build());
        payRecordMapper.insert(PayRecordPO.builder()
                .id(IdWorker.getId())
                .payDate("2024-10-06")
                .amount(new BigDecimal("8.5"))
                .transId(transId)
                .build());
        userInfoMapper.insert(UserInfoPO.builder()
                .id(userId)
                .name("李四")
                .build());
    }
}
