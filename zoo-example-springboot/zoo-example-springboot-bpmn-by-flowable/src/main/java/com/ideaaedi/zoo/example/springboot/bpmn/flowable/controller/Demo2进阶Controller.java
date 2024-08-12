package com.ideaaedi.zoo.example.springboot.bpmn.flowable.controller;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.google.common.collect.Lists;
import com.ideaaedi.zoo.diy.feature.bpmn.api.entity.dto.BpmnActivityStateUpdate;
import com.ideaaedi.zoo.diy.feature.bpmn.api.entity.dto.BpmnDeployCreate;
import com.ideaaedi.zoo.diy.feature.bpmn.api.entity.dto.BpmnHisTaskQuery;
import com.ideaaedi.zoo.diy.feature.bpmn.api.entity.dto.BpmnProcessDefinitionQuery;
import com.ideaaedi.zoo.diy.feature.bpmn.api.entity.dto.BpmnTaskQuery;
import com.ideaaedi.zoo.diy.feature.bpmn.api.entity.dto.form.BpmnFormCreate;
import com.ideaaedi.zoo.diy.feature.bpmn.api.entity.facade.BpmnDelegateTask;
import com.ideaaedi.zoo.diy.feature.bpmn.api.entity.facade.BpmnDeployment;
import com.ideaaedi.zoo.diy.feature.bpmn.api.entity.facade.BpmnHisProcessInstance;
import com.ideaaedi.zoo.diy.feature.bpmn.api.entity.facade.BpmnHisTask;
import com.ideaaedi.zoo.diy.feature.bpmn.api.entity.facade.BpmnHisVariable;
import com.ideaaedi.zoo.diy.feature.bpmn.api.entity.facade.BpmnProcessDefinition;
import com.ideaaedi.zoo.diy.feature.bpmn.api.entity.facade.BpmnProcessInstance;
import com.ideaaedi.zoo.diy.feature.bpmn.api.entity.facade.BpmnTask;
import com.ideaaedi.zoo.diy.feature.bpmn.api.entity.facade.BpmnTaskState;
import com.ideaaedi.zoo.diy.feature.bpmn.api.entity.facade.form.BpmnFormData;
import com.ideaaedi.zoo.diy.feature.bpmn.api.entity.facade.form.DefaultBpmnFormProperty;
import com.ideaaedi.zoo.diy.feature.bpmn.api.enums.ORDER_TYPE;
import com.ideaaedi.zoo.diy.feature.bpmn.api.listener.task.BpmnTaskListener;
import com.ideaaedi.zoo.diy.feature.bpmn.api.service.BpmnDeploymentService;
import com.ideaaedi.zoo.diy.feature.bpmn.api.service.BpmnGoBackService;
import com.ideaaedi.zoo.diy.feature.bpmn.api.service.BpmnHistoryService;
import com.ideaaedi.zoo.diy.feature.bpmn.api.service.BpmnProcessService;
import com.ideaaedi.zoo.diy.feature.bpmn.api.service.BpmnTaskService;
import com.ideaaedi.zoo.diy.feature.bpmn.api.service.form.BpmnFormRepositoryService;
import com.ideaaedi.zoo.diy.feature.bpmn.api.service.form.BpmnFormService;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;
import org.flowable.engine.RuntimeService;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <pre>
 * 1. 在bpmn流程文件设计时，在其userTask标签中，
 *       可通过设置flowable:assignee指定任务处理人 ；指定的人直接负责任务
 *       可通过设置flowable:candidateUsers指定任务候选人（多个使用逗号分隔）：当没有指定办理人时，这些候选人可以申领任务成为办理人
 *       可通过设置flowable:candidateGroups指定任务候选组（多个使用逗号分隔）：当没有指定办理人时，这些候选组里面的人可以申领任务成为办理人
 *
 *  2. 在bpmn流程文件设计时，指定任务办理人时
 *     a. 可以指定固定的任务办理人
 *     b. 也可以通过表达式占位指定办理人，然后在代码里动态设置对应"占位符"的值
 *     c. 还可以在流程实例生命周期中，通过任务监听器等，动态指定任务办理人
 *  </pre>
 */
@RestController
@RequestMapping("/demo-进阶")
public class Demo2进阶Controller {
    
    private static final String TENANT = "1,";
    
    @Resource
    private BpmnDeploymentService bpmnDeploymentService;
    
    @Resource
    private BpmnProcessService bpmnProcessService;
    
    @Resource
    private BpmnHistoryService bpmnHistoryService;
    
    @Resource
    private BpmnGoBackService bpmnGoBackService;
    
    @Resource
    private RuntimeService runtimeService;
    
    @Resource
    private BpmnTaskService bpmnTaskService;
    
    @Resource
    private BpmnFormService bpmnFormService;
    
    @Resource
    private BpmnFormRepositoryService bpmnFormRepositoryService;
    
    /**
     * 示例 任务固定分配。 即：任务处理人，在bpmn流程文件中就写死了
     *
     * @param money 出差经费。 在本示例中，按照[出差申请-固定分配.bpmn20.xml]中的定义，如果金额>=10000了，那么总经理还需要复审
     */
    @SneakyThrows
    @RequestMapping("/任务分配/固定分配")
    public void demo1(@RequestParam int money) {
        Assert.isTrue(money >= 0, "出差经费必须大于等于0");
        List<String> infoList = new ArrayList<>();
        String deploymentId = null;
        try {
            // ---------- step1. 部署生成 流程定义 ----------
            BpmnDeployment deploy = bpmnDeploymentService.deploy(
                    TENANT, BpmnDeployCreate.builder()
                            .name("出差申请-固定分配")
                            .resourceName("出差申请-固定分配.bpmn20.xml")
                            .resourceBytes(ResourceUtil.readBytes("出差申请-固定分配.bpmn20.xml"))
                            .build()
            );
            deploymentId = deploy.getId();
            infoList.add("部署id：" + deploymentId);
            List<? extends BpmnProcessDefinition> bpmnProcessDefinitions = bpmnProcessService.listDefinition(TENANT,
                    BpmnProcessDefinitionQuery.builder().deploymentId(deploymentId).build());
            infoList.add("流程定义key：" + bpmnProcessDefinitions.get(0).getKey() + "，流程定义id：" + bpmnProcessDefinitions.get(0).getId()); // 因为本bpmn文件中只定义了一个流程，所以这里取第一个即可
            
            // ---------- step2. 根据流程定义发起流程 ----------
            // 流程定义key,即为xxx.bpmn20.xml中的<bpmn:process id="xxx" />中的id。值等价于 bpmnProcessDefinitions.get(0).getKey()
            String processDefinitionKey = "ccsq_gdfp";
            BpmnProcessInstance bpmnProcessInstance = bpmnProcessService.startInstanceByKey(TENANT, processDefinitionKey,
                    IdUtil.simpleUUID(), null
            );
            String bpmnProcessInstanceId = bpmnProcessInstance.getId();
            infoList.add("流程实例id：" + bpmnProcessInstanceId);
            
            // ---------- step3. （流程自动流转，）查看任务 ----------
            List<? extends BpmnTask> list = bpmnTaskService.list(TENANT,
                    BpmnTaskQuery.builder().processInstanceId(bpmnProcessInstanceId).build());
            infoList.add("发起流程后， 当前任务流转到了：" + formatStr(list));
            
            Map<String, Object> variables = Map.of("money", money);
            bpmnTaskService.complete(TENANT, list.get(0).getId(), "员工张", variables); // 因为按bpmn
            // 文件流程，最多只会同时有一个任务，所以这里取第一个即可
            list = bpmnTaskService.list(TENANT, BpmnTaskQuery.builder().processInstanceId(bpmnProcessInstanceId).build());
            infoList.add(String.format("张三完成了【需要%s经费的出差】申请后， 当前任务流转到了：", money) + formatStr(list));
            
            
            bpmnTaskService.complete(TENANT, list.get(0).getId(), "经理胡", variables); // 因为按bpmn
            // 文件流程，最多只会同时有一个任务，所以这里取第一个即可
            list = bpmnTaskService.list(TENANT, BpmnTaskQuery.builder().processInstanceId(bpmnProcessInstanceId).build());
            infoList.add(String.format("经理胡完成了【需要%s经费的出差】审批后， 当前任务流转到了：", money) + formatStr(list));
            
            // 按照[出差申请-固定分配.bpmn20.xml]中的定义，如果金额>=10000了，那么总经理还需要复审
            if (money >= 10000) {
                bpmnTaskService.complete(TENANT, list.get(0).getId(), "总经理邓", variables); // 因为按bpmn
                // 文件流程，最多只会同时有一个任务，所以这里取第一个即可
                list = bpmnTaskService.list(TENANT,
                        BpmnTaskQuery.builder().processInstanceId(bpmnProcessInstanceId).build());
                infoList.add(String.format("总经理邓完成了【需要%s经费的出差】复审后， 当前任务流转到了：", money) + formatStr(list));
            }
            
            bpmnTaskService.complete(TENANT, list.get(0).getId(), "财务何", variables); // 因为按bpmn
            // 文件流程，最多只会同时有一个任务，所以这里取第一个即可
            list = bpmnTaskService.list(TENANT, BpmnTaskQuery.builder().processInstanceId(bpmnProcessInstanceId).build());
            if (CollectionUtils.isEmpty(list)) {
                bpmnProcessInstance = bpmnProcessService.detailInstance(TENANT, bpmnProcessInstanceId);
                Assert.isTrue(bpmnProcessInstance == null, "流程实例执行完毕后，bpmnProcessService"
                        + ".detailInstance接口应该就查询不到流程实例信息才对！");
                infoList.add(String.format("财务何完成了【需要%s经费的出差】财务拨款后， 流程实例执行完毕啦！", money));
            } else {
                throw new IllegalStateException("按照[出差申请-固定分配.bpmn20.xml]中的定义，流程应该执行完了才对，任务列表因为为空才对！");
            }
        } finally {
            // 清除测试数据
            if (deploymentId != null) {
                bpmnDeploymentService.undeploy(TENANT, deploymentId, true);
            }
        }
        int size = infoList.size();
        for (int i = 1; i <= size; i++) {
            System.err.println("信息" + i + ": " + infoList.get(i - 1));
            System.err.println();
        }
    }
    
    /**
     * 示例 表达式动态分配。 <br /> 即：任务处理人，在bpmn流程文件中通过表达式占位（详见：出差申请-表达式动态分配.bpmn20.xml中的${...}部分），在运行时，根据表达式计算出。
     *
     * @param money 出差经费。 在本示例中，按照[出差申请-表达式动态分配.bpmn20.xml]中的定义，如果金额>=10000了，那么总经理还需要复审
     */
    @SneakyThrows
    @RequestMapping("/任务分配/动态分配-通过表达式占位")
    public void demo2(@RequestParam int money) {
        Assert.isTrue(money >= 0, "出差经费必须大于等于0");
        List<String> infoList = new ArrayList<>();
        String deploymentId = null;
        
        String yuanGong = "员工_" + RandomUtil.randomChinese();
        String jingLi = "部门经理_" + RandomUtil.randomChinese();
        String zongJingLi = "总经理_" + RandomUtil.randomChinese();
        String chaiWu = "财务_" + RandomUtil.randomChinese();
        Map<String, Object> variables = Map.of(
                "money", money, // 将金额放到流程变量中
                
                "YuanGong", yuanGong, // 指定真正的员工
                "JingLi", jingLi, // 指定真正的经理
                "ZongJingLi", zongJingLi, // 指定真正的总经理
                "ChaiWu", chaiWu // 指定真正的财务
        );
        try {
            // ---------- step1. 部署生成 流程定义 ----------
            BpmnDeployment deploy = bpmnDeploymentService.deploy(
                    TENANT, BpmnDeployCreate.builder()
                            .name("出差申请-表达式动态分配")
                            .resourceName("出差申请-表达式动态分配.bpmn20.xml")
                            .resourceBytes(ResourceUtil.readBytes("出差申请-表达式动态分配.bpmn20.xml"))
                            .build()
            );
            deploymentId = deploy.getId();
            infoList.add("部署id：" + deploymentId);
            List<? extends BpmnProcessDefinition> bpmnProcessDefinitions = bpmnProcessService.listDefinition(TENANT,
                    BpmnProcessDefinitionQuery.builder().deploymentId(deploymentId).build());
            infoList.add("流程定义key：" + bpmnProcessDefinitions.get(0).getKey() + "，流程定义id：" + bpmnProcessDefinitions.get(0).getId()); // 因为本bpmn文件中只定义了一个流程，所以这里取第一个即可
            
            // ---------- step2. 根据流程定义发起流程 ----------
            // 流程定义key,即为xxx.bpmn20.xml中的<bpmn:process id="xxx" />中的id。值等价于 bpmnProcessDefinitions.get(0).getKey()
            String processDefinitionKey = "ccsq_bdsdtfp";
            BpmnProcessInstance bpmnProcessInstance = bpmnProcessService.startInstanceByKey(TENANT, processDefinitionKey,
                    IdUtil.simpleUUID(), variables
            );
            String bpmnProcessInstanceId = bpmnProcessInstance.getId();
            infoList.add("流程实例id：" + bpmnProcessInstanceId);
            
            // ---------- step3. （流程自动流转，）查看任务 ----------
            List<? extends BpmnTask> list = bpmnTaskService.list(TENANT,
                    BpmnTaskQuery.builder().processInstanceId(bpmnProcessInstanceId).build());
            infoList.add("发起流程后， 当前任务流转到了：" + formatStr(list));
            
            bpmnTaskService.complete(TENANT, list.get(0).getId(), "1", null); // 因为按bpmn文件流程，最多只会同时有一个任务，所以这里取第一个即可
            list = bpmnTaskService.list(TENANT, BpmnTaskQuery.builder().processInstanceId(bpmnProcessInstanceId).build());
            infoList.add(String.format("%s完成了【需要%s经费的出差】申请后， 当前任务流转到了：", yuanGong, money) + formatStr(list));
            
            
            bpmnTaskService.complete(TENANT, list.get(0).getId(), "2", variables); // 因为按bpmn文件流程，最多只会同时有一个任务，所以这里取第一个即可
            list = bpmnTaskService.list(TENANT, BpmnTaskQuery.builder().processInstanceId(bpmnProcessInstanceId).build());
            infoList.add(String.format("%s完成了【需要%s经费的出差】审批后， 当前任务流转到了：", jingLi, money) + formatStr(list));
            
            // 按照[出差申请-表达式动态分配.bpmn20.xml]中的定义，如果金额>=10000了，那么总经理还需要复审
            if (money >= 10000) {
                bpmnTaskService.complete(TENANT, list.get(0).getId(), "3", variables); // 因为按bpmn
                // 文件流程，最多只会同时有一个任务，所以这里取第一个即可
                list = bpmnTaskService.list(TENANT,
                        BpmnTaskQuery.builder().processInstanceId(bpmnProcessInstanceId).build());
                infoList.add(String.format("%s完成了【需要%s经费的出差】复审后， 当前任务流转到了：", zongJingLi, money) + formatStr(list));
            }
            
            bpmnTaskService.complete(TENANT, list.get(0).getId(), "财务何", variables); // 因为按bpmn
            // 文件流程，最多只会同时有一个任务，所以这里取第一个即可
            list = bpmnTaskService.list(TENANT, BpmnTaskQuery.builder().processInstanceId(bpmnProcessInstanceId).build());
            if (CollectionUtils.isEmpty(list)) {
                bpmnProcessInstance = bpmnProcessService.detailInstance(TENANT, bpmnProcessInstanceId);
                Assert.isTrue(bpmnProcessInstance == null, "流程实例执行完毕后，bpmnProcessService"
                        + ".detailInstance接口应该就查询不到流程实例信息才对！");
                infoList.add(String.format("%s完成了【需要%s经费的出差】财务拨款后， 流程实例执行完毕啦！", chaiWu, money));
            } else {
                throw new IllegalStateException("按照[出差申请-表达式动态分配.bpmn20.xml]中的定义，流程应该执行完了才对，任务列表因为为空才对！");
            }
        } finally {
            // 清除测试数据
            if (deploymentId != null) {
                bpmnDeploymentService.undeploy(TENANT, deploymentId, true);
            }
        }
        int size = infoList.size();
        for (int i = 1; i <= size; i++) {
            System.err.println("信息" + i + ": " + infoList.get(i - 1));
            System.err.println();
        }
    }
    
    /**
     * 示例 任务监听器动态分配办理人。 <br /> 即：任务处理人，在bpmn流程文件中不指定，而是在代码中通过任务监听器动态指定
     */
    @SneakyThrows
    @RequestMapping("/任务分配/动态分配-通过任务监听器")
    public void demo3() {
        String xiaoXueSheng = "谢" + RandomUtil.randomChinese();
        
        List<String> infoList = new ArrayList<>();
        String deploymentId = null;
        try {
            // ---------- step1. 部署生成 流程定义 ----------
            BpmnDeployment deploy = bpmnDeploymentService.deploy(
                    TENANT, BpmnDeployCreate.builder()
                            .name("买游戏机申请-任务监听器动态分配")
                            .resourceName("买游戏机申请.bpmn20.xml")
                            .resourceBytes(ResourceUtil.readBytes("买游戏机申请.bpmn20.xml"))
                            .build()
            );
            deploymentId = deploy.getId();
            infoList.add("部署id：" + deploymentId);
            List<? extends BpmnProcessDefinition> bpmnProcessDefinitions = bpmnProcessService.listDefinition(TENANT,
                    BpmnProcessDefinitionQuery.builder().deploymentId(deploymentId).build());
            infoList.add("流程定义key：" + bpmnProcessDefinitions.get(0).getKey() + "，流程定义id：" + bpmnProcessDefinitions.get(0).getId()); // 因为本bpmn文件中只定义了一个流程，所以这里取第一个即可
            
            // ---------- step2. 根据流程定义发起流程 ----------
            // 流程定义key,即为xxx.bpmn20.xml中的<bpmn:process id="xxx" />中的id。值等价于 bpmnProcessDefinitions.get(0).getKey()
            String processDefinitionKey = "myxjsq";
            
            /*
             * 两个位置的xiaoXueSheng参数说明：
             *
             * Map.of("ShengQingRen", xiaoXueSheng) 中的 xiaoXueSheng： 作为任务【买游戏机申请】的办理人
             * 最后一个参数xiaoXueSheng： 作为流程实例的发起人
             */
            BpmnProcessInstance bpmnProcessInstance = bpmnProcessService.startInstanceByKey(TENANT, processDefinitionKey,
                    IdUtil.simpleUUID(), Map.of("ShengQingRen", xiaoXueSheng), xiaoXueSheng);
            String bpmnProcessInstanceId = bpmnProcessInstance.getId();
            infoList.add("流程实例id：" + bpmnProcessInstanceId);
            
            // ---------- step3. （流程自动流转，）查看任务 ----------
            List<? extends BpmnTask> list = bpmnTaskService.list(TENANT,
                    BpmnTaskQuery.builder().processInstanceId(bpmnProcessInstanceId).build());
            infoList.add(String.format("%s发起流程后， 当前任务流转到了：", xiaoXueSheng) + formatStr(list));
            
            BpmnTask bpmnTask = list.get(0);
            String currTaskAssignee = bpmnTask.getAssignee();
            bpmnTaskService.complete(TENANT, bpmnTask.getId(), "1", null); // 因为按bpmn文件流程，最多只会同时有一个任务，所以这里取第一个即可
            list = bpmnTaskService.list(TENANT, BpmnTaskQuery.builder().processInstanceId(bpmnProcessInstanceId).build());
            infoList.add(String.format("%s完成了【买游戏机】申请后， 当前任务流转到了：", currTaskAssignee) + formatStr(list));
            
            bpmnTask = list.get(0);
            currTaskAssignee = bpmnTask.getAssignee();
            bpmnTaskService.complete(TENANT, bpmnTask.getId(), "2", null); // 因为按bpmn文件流程，最多只会同时有一个任务，所以这里取第一个即可
            list = bpmnTaskService.list(TENANT, BpmnTaskQuery.builder().processInstanceId(bpmnProcessInstanceId).build());
            infoList.add(String.format("%s完成了【买游戏机申请】审批后， 当前任务流转到了：", currTaskAssignee) + formatStr(list));
            
            bpmnTask = list.get(0);
            currTaskAssignee = bpmnTask.getAssignee();
            bpmnTaskService.complete(TENANT, bpmnTask.getId(), "3", null); // 因为按bpmn文件流程，最多只会同时有一个任务，所以这里取第一个即可
            list = bpmnTaskService.list(TENANT, BpmnTaskQuery.builder().processInstanceId(bpmnProcessInstanceId).build());
            if (CollectionUtils.isEmpty(list)) {
                bpmnProcessInstance = bpmnProcessService.detailInstance(TENANT, bpmnProcessInstanceId);
                Assert.isTrue(bpmnProcessInstance == null, "流程实例执行完毕后，bpmnProcessService"
                        + ".detailInstance接口应该就查询不到流程实例信息才对！");
                infoList.add(String.format("%s完成了【买游戏机】购买后， 流程实例执行完毕啦！", currTaskAssignee));
            } else {
                throw new IllegalStateException("按照[买游戏机申请.bpmn20.xml]中的定义，流程应该执行完了才对，任务列表因为为空才对！");
            }
        } finally {
            // 清除测试数据
            if (deploymentId != null) {
                bpmnDeploymentService.undeploy(TENANT, deploymentId, true);
            }
        }
        int size = infoList.size();
        for (int i = 1; i <= size; i++) {
            System.err.println("信息" + i + ": " + infoList.get(i - 1));
            System.err.println();
        }
    }
    
    @SneakyThrows
    @RequestMapping("/全局变量与本地变量/运行时")
    public void demo4() {
        demo4AndDemo5Common(true, true);
    }
    
    /*
     * 任务完成之后，再查询运行时任务的相关参数，就无法查询到了；这时候就需要用历史服务相关接口查询
     */
    @SneakyThrows
    @RequestMapping("/全局变量与本地变量/历史")
    public void demo5() {
        List<String> infoList = new ArrayList<>();
        String bpmnProcessInstanceId = null;
        try {
            bpmnProcessInstanceId = demo4AndDemo5Common(false, false);
            List<? extends BpmnHisVariable> hisVariablesList = bpmnHistoryService.listHisVariable(bpmnProcessInstanceId,
                    null);
            for (BpmnHisVariable bpmnHisVariable : hisVariablesList) {
                String taskId = bpmnHisVariable.getId();
                boolean ifExpected = false;
                try {
                    bpmnTaskService.getVariables(taskId);
                } catch (Exception e) {
                    infoList.add("从运行时查询任务的变量出错了（这是符合预期的）：" + e.getMessage() + "；   从历史里面查，能查出来：" + formatStr(bpmnHisVariable));
                    ifExpected = true;
                }
                Assert.isTrue(ifExpected, "任务已经执行完了，这时候再从运行时查询任务的变量，应该查不到任务导致报错才对。");
            }
        } finally {
            // 清除测试数据
            if (bpmnProcessInstanceId != null) {
                BpmnHisProcessInstance bpmnHisProcessInstance =
                        bpmnHistoryService.detailHisProcessInstance(TENANT, bpmnProcessInstanceId);
                Objects.requireNonNull(bpmnHisProcessInstance);
                bpmnDeploymentService.undeploy(TENANT, bpmnHisProcessInstance.getDeploymentId(), true);
            }
        }
        int size = infoList.size();
        for (int i = 1; i <= size; i++) {
            System.err.println("信息" + i + ": " + infoList.get(i - 1));
            System.err.println();
        }
    }
    
    /*
     * 个人理解：表单是流程变量的子集，是对那部分需要和用户交互的变量的描述定义。
     *          严格的讲，表单属于业务该实现的逻辑，而不是流程引擎该实现的逻辑，
     *          可能这就是为什么flowable后来移除自带的表单实现的原因吧
     */
    @SneakyThrows
    @RequestMapping("/表单的使用")
    public void demo6() {
        List<String> infoList = new ArrayList<>();
        String deploymentId = null;
        try {
            // 0. 先清一下数据，避免脏数据影响测试
            clearData(); // 这将删除所有部署及相关数据，请确保你连接的是自己的demo数据库
            
            // ---------- 前置：在编写流程图之前，先把流程中需要用到的相关表单录入好。然后再编写流程图时，通过表单key引用对应的表单参数 ----------
            // ---------- 这里加上我们在发起流程前，已经创建了这两个表单。P.S. 在流程文件bpmn20.xml中通过flowable:formKey="xxx"指定要引用的表单 ----------
            bpmnFormRepositoryService.create(TENANT,
                    BpmnFormCreate.builder()
                            .formKey("startFormKey")
                            .formName("发起流程需要填写的表单")
                            .formProperties(
                                    Lists.newArrayList(
                                            DefaultBpmnFormProperty.builder()
                                                    .key("why")
                                                    .name("发起原因")
                                                    .type("string")
                                                    .description("发起此流程的原因")
                                                    .build(),
                                            DefaultBpmnFormProperty.builder()
                                                    .key("jieZhiRiQi")
                                                    .name("截止日期")
                                                    .type("date-time")
                                                    .description("希望此流程在多久内能处理完")
                                                    .build()
                                    )
                            ).build()
            );
    
            bpmnFormRepositoryService.create(TENANT,
                    BpmnFormCreate.builder()
                            .formKey("zsFormKey")
                            .formName("处理任务需要填写的表单")
                            .formProperties(
                                    Lists.newArrayList(
                                        DefaultBpmnFormProperty.builder()
                                                .key("yiJian")
                                                .name("意见")
                                                .type("string")
                                                .description("描述处理此任务时的意见")
                                                .build(),
                                        DefaultBpmnFormProperty.builder()
                                                .key("deFen")
                                                .name("得分")
                                                .type("integer")
                                                .description("描述处理此任务后的得分，满分100分")
                                                .build()
                                    )
                            ).build()
            );
            
            
            // ---------- step1. 部署生成 流程定义 ----------
            BpmnDeployment deploy = bpmnDeploymentService.deploy(
                    TENANT, BpmnDeployCreate.builder()
                            .resourceName("表单测试.bpmn20.xml")
                            .resourceBytes(ResourceUtil.readBytes("表单测试.bpmn20.xml"))
                            .build()
            );
            deploymentId = deploy.getId();
            List<? extends BpmnProcessDefinition> bpmnProcessDefinitions = bpmnProcessService.listDefinition(TENANT,
                    BpmnProcessDefinitionQuery.builder().deploymentId(deploymentId).build());
            String processDefinitionId = bpmnProcessDefinitions.get(0).getId();
            infoList.add("流程定义id：" + processDefinitionId);
    
            BpmnFormData bpmnFormData = bpmnFormService.getStartFormData(TENANT, processDefinitionId);
            infoList.add("该流程需要的初始表单是：" + formatStr(bpmnFormData));
            
            // ---------- step2. 根据流程定义发起流程 ----------
            BpmnProcessInstance bpmnProcessInstance = bpmnProcessService.startInstanceByKey(TENANT, bpmnProcessDefinitions.get(0).getKey(),
                    IdUtil.simpleUUID(), null);
            String bpmnProcessInstanceId = bpmnProcessInstance.getId();
        
            // ---------- step3. （流程自动流转，）查看任务 ----------
            List<? extends BpmnTask> list = bpmnTaskService.list(TENANT,
                    BpmnTaskQuery.builder().processInstanceId(bpmnProcessInstanceId).build());
            BpmnTask bpmnTask = list.get(0);
            bpmnTaskService.complete(TENANT, bpmnTask.getId(), "1", null);
    
            // 任务完成后也可以查看流程关联的表单
            bpmnFormData = bpmnFormService.getTaskFormData(TENANT, bpmnTask.getId());
            infoList.add(String.format("和任务【%s】关联的表单定义是：，", bpmnTask.getName()) + formatStr(bpmnFormData));
        } finally {
            // 清除测试数据
            if (deploymentId != null) {
                bpmnDeploymentService.undeploy(TENANT, deploymentId, true);
            }
        }
        int size = infoList.size();
        for (int i = 1; i <= size; i++) {
            System.err.println("信息" + i + ": " + infoList.get(i - 1));
            System.err.println();
        }
    }
    
    /*
     * 任务撤回：当前任务的处理人撤销自己对当前任务的操作，将任务恢复到自己尚未处理的状态
     */
    @SneakyThrows
    @RequestMapping("/撤回")
    public void demo7() {
        List<String> infoList = new ArrayList<>();
        String deploymentId = null;
        String currUserId = "邓沙利文";
        try {
            // ---------- step1. 部署生成 流程定义 ----------
            BpmnDeployment deploy = bpmnDeploymentService.deploy(
                    TENANT, BpmnDeployCreate.builder()
                            .name("撤回与回退测试")
                            .resourceName("撤回与回退测试.bpmn20.xml")
                            .resourceBytes(ResourceUtil.readBytes("撤回与回退测试.bpmn20.xml"))
                            .build()
            );
            deploymentId = deploy.getId();
            List<? extends BpmnProcessDefinition> bpmnProcessDefinitions = bpmnProcessService.listDefinition(TENANT,
                    BpmnProcessDefinitionQuery.builder().deploymentId(deploymentId).build());
            BpmnProcessDefinition bpmnProcessDefinition = bpmnProcessDefinitions.get(0);
        
            // ---------- step2. 根据流程定义发起流程 ----------
            BpmnProcessInstance bpmnProcessInstance = bpmnProcessService.startInstanceByKey(TENANT, bpmnProcessDefinition.getKey(),
                    IdUtil.simpleUUID(), null, currUserId);
            String bpmnProcessInstanceId = bpmnProcessInstance.getId();
            infoList.add(String.format("【%s】发起了【%s】流程。", currUserId, bpmnProcessDefinition.getName()));
        
            // ---------- step3. 查看当前任务 ----------
            List<? extends BpmnTask> list = bpmnTaskService.list(TENANT,
                    BpmnTaskQuery.builder().processInstanceId(bpmnProcessInstanceId).build());
            BpmnTask bpmnTask = list.get(0);
            currUserId = "张三";
            
            // ---------- step4. 张三完成任务 ----------
            bpmnTaskService.complete(TENANT, bpmnTask.getId(), currUserId, null);
            String info = String.format("【%s】完成了【%s】。", currUserId, bpmnTask.getName());
            
            // ---------- step5. 再次查看当前任务 ----------
            list = bpmnTaskService.list(TENANT,
                    BpmnTaskQuery.builder().processInstanceId(bpmnProcessInstanceId).build());
            bpmnTask = list.get(0);
            infoList.add(String.format(info + "当前待处理任务：【%s】，任务状态：【%s】。", bpmnTask.getName(),  bpmnTask.getState()));
            
            boolean keCheHui = BpmnTaskState.CREATED.equals(bpmnTask.getState()) || BpmnTaskState.CLAIMED.equals(bpmnTask.getState());
            if (keCheHui) {
                
                // 查询当前任务之前的任务，并按任务完成时间倒序排列
                List<? extends BpmnHisTask> beforeTaskList = bpmnHistoryService.listHisTask(TENANT,
                        BpmnHisTaskQuery.builder()
                        .ifFinished(true)
                        .orderByTaskEndTimePair(Pair.of(true, ORDER_TYPE.DESC))
                        .build());
    
                if (CollectionUtils.isEmpty(beforeTaskList)) {
                    throw new RuntimeException("没有已完成的任务可撤回");
                }
                BpmnHisTask beforeTask1 = beforeTaskList.get(0);
    
                if (!currUserId.equals(beforeTask1.getCompletedBy())) {
                    throw new RuntimeException("前一任务的完成人是" + beforeTask1.getCompletedBy() + "，不是" + currUserId + "，无权撤回");
                }
    
                // ---------- step6. 回退 ----------
                // 因为回退会结束当前活动节点，所以结束前，这里给撤回起点任务任务设置一个业务标识，让我们知道此任务之所以会结束，是因为撤回导致的
                bpmnTaskService.setVariableLocal(bpmnTask.getId(), "CustomTaskSate", "无效（上游节点被撤回）");
    
                // (视情况而定)， 用一个流程变量记录下，此撤回操作下 受影响的任务id
                Set<String> affectedTaskIds = (Set<String>)bpmnTaskService.getVariable(bpmnTask.getId(), "affectedTaskIds");
                if (affectedTaskIds == null) {
                    affectedTaskIds = new HashSet<>();
                }
                affectedTaskIds.add(beforeTask1.getId());
                affectedTaskIds.add(bpmnTask.getId());
                
                // 回退至上一个任务
                bpmnGoBackService.moveActivityIdTo(bpmnTask.getTaskDefinitionKey(), beforeTask1.getTaskDefinitionKey(),
                        BpmnActivityStateUpdate.builder()
                                .processInstanceId(bpmnProcessInstanceId)
                                .processVariables( Map.of("撤回操作受影响的任务", affectedTaskIds))
                                .build()
                        );
                
                infoList.add(String.format("%s撤销前一任务【%s】的操作", currUserId,  beforeTask1.getName()));
                // ---------- step7. 再次查看当前任务 ----------
                list = bpmnTaskService.list(TENANT,
                        BpmnTaskQuery.builder().processInstanceId(bpmnProcessInstanceId).build());
                bpmnTask = list.get(0);
                infoList.add(String.format("再查查看进度信息。当前待处理任务：【%s】，任务状态：【%s】。", bpmnTask.getName(),  bpmnTask.getState()));
    
                // ---------- step8. 张三再次完成任务，再次查看当前任务 ----------
                bpmnTaskService.complete(TENANT, bpmnTask.getId(), currUserId, null);
                info = String.format("【%s】完成了【%s】。", currUserId, bpmnTask.getName());
                list = bpmnTaskService.list(TENANT,
                        BpmnTaskQuery.builder().processInstanceId(bpmnProcessInstanceId).build());
                bpmnTask = list.get(0);
                infoList.add(String.format(info + "当前待处理任务：【%s】，任务状态：【%s】。", bpmnTask.getName(),  bpmnTask.getState()));
    
                // ---------- step9. 李四完成任务，再次查看当前任务 ----------
                currUserId = "李四";
                bpmnTaskService.complete(TENANT, bpmnTask.getId(), currUserId, null);
                info = String.format("【%s】完成了【%s】。", currUserId, bpmnTask.getName());
                list = bpmnTaskService.list(TENANT,
                        BpmnTaskQuery.builder().processInstanceId(bpmnProcessInstanceId).build());
                bpmnTask = list.get(0);
                infoList.add(String.format(info + "当前待处理任务：【%s】，任务状态：【%s】。", bpmnTask.getName(),  bpmnTask.getState()));
    
                // ---------- step10. 王五完成任务 ----------
                currUserId = "王五";
                bpmnTaskService.complete(TENANT, bpmnTask.getId(), currUserId, null);
                info = String.format("【%s】完成了【%s】。", currUserId, bpmnTask.getName());
                list = bpmnTaskService.list(TENANT,
                        BpmnTaskQuery.builder().processInstanceId(bpmnProcessInstanceId).build());
                if (CollectionUtils.isEmpty(list)) {
                    infoList.add(info + "流程结束。");
                    
                    // ---------- step11. 流程结束后，查查查流程节点信息 ----------
                    List<? extends BpmnHisTask> hisTaskList = bpmnHistoryService.listHisTask(TENANT,
                            BpmnHisTaskQuery.builder()
                                    .processInstanceId(bpmnProcessInstanceId)
                                    .queryTaskLocalVariables(true)
                                    .queryProcessVariables(true)
                                    .build());
                    infoList.add(String.format("本流程涉及到的任务节点有：%s", formatStr(hisTaskList)));
                } else {
                    throw new RuntimeException("按照流程图，此时流程已经进行完了才对。");
                }
            } else {
                throw new RuntimeException("不可撤回！后续流程已经被推进了。");
            }
        } finally {
            // 清除测试数据
            if (deploymentId != null) {
                bpmnDeploymentService.undeploy(TENANT, deploymentId, true);
            }
        }
        int size = infoList.size();
        for (int i = 1; i <= size; i++) {
            System.err.println("信息" + i + ": " + infoList.get(i - 1));
            System.err.println();
        }
    }
    
    /*
     * 任务回退：将流程实例从当前所在的任务节点返回到流程中的前一个或更早的指定节点。
     *         回退可以是因为审批不通过、信息更正需要、或是其他业务逻辑需要回到之前的步骤重新处理
     */
    @SneakyThrows
    @RequestMapping("/回退（打回）")
    public void demo8() {
        List<String> infoList = new ArrayList<>();
        String deploymentId = null;
        String currUserId = "邓沙利文";
        try {
            // ---------- step1. 部署生成 流程定义 ----------
            BpmnDeployment deploy = bpmnDeploymentService.deploy(
                    TENANT, BpmnDeployCreate.builder()
                            .name("撤回与回退测试")
                            .resourceName("撤回与回退测试.bpmn20.xml")
                            .resourceBytes(ResourceUtil.readBytes("撤回与回退测试.bpmn20.xml"))
                            .build()
            );
            deploymentId = deploy.getId();
            List<? extends BpmnProcessDefinition> bpmnProcessDefinitions = bpmnProcessService.listDefinition(TENANT,
                    BpmnProcessDefinitionQuery.builder().deploymentId(deploymentId).build());
            BpmnProcessDefinition bpmnProcessDefinition = bpmnProcessDefinitions.get(0);
        
            // ---------- step2. 根据流程定义发起流程 ----------
            BpmnProcessInstance bpmnProcessInstance = bpmnProcessService.startInstanceByKey(TENANT, bpmnProcessDefinition.getKey(),
                    IdUtil.simpleUUID(), null, currUserId);
            String bpmnProcessInstanceId = bpmnProcessInstance.getId();
            infoList.add(String.format("【%s】发起了【%s】流程。", currUserId, bpmnProcessDefinition.getName()));
        
            // ---------- step3. 查看当前任务 ----------
            List<? extends BpmnTask> list = bpmnTaskService.list(TENANT,
                    BpmnTaskQuery.builder().processInstanceId(bpmnProcessInstanceId).build());
            BpmnTask bpmnTask = list.get(0);
            currUserId = "张三";
        
            // ---------- step4. 张三完成任务 ----------
            bpmnTaskService.complete(TENANT, bpmnTask.getId(), currUserId, null);
            String info = String.format("【%s】完成了【%s】。", currUserId, bpmnTask.getName());
            list = bpmnTaskService.list(TENANT,
                    BpmnTaskQuery.builder().processInstanceId(bpmnProcessInstanceId).build());
            bpmnTask = list.get(0);
            infoList.add(String.format(info + "当前待处理任务：【%s】，任务状态：【%s】。", bpmnTask.getName(),  bpmnTask.getState()));
    
            // ---------- step9. 李四完成任务 ----------
            currUserId = "李四";
            bpmnTaskService.complete(TENANT, bpmnTask.getId(), currUserId, null);
            info = String.format("【%s】完成了【%s】。", currUserId, bpmnTask.getName());
            list = bpmnTaskService.list(TENANT,
                    BpmnTaskQuery.builder().processInstanceId(bpmnProcessInstanceId).build());
            bpmnTask = list.get(0);
            infoList.add(String.format(info + "当前待处理任务：【%s】，任务状态：【%s】。", bpmnTask.getName(),  bpmnTask.getState()));
    
    
            // ---------- step9. 王五退回流程至 最早的任务节点 ----------
            currUserId = "王五";
            // 查询当前任务之前的任务，并按任务完成时间倒序排列
            List<? extends BpmnHisTask> beforeTaskList = bpmnHistoryService.listHisTask(TENANT,
                    BpmnHisTaskQuery.builder()
                            .ifFinished(true)
                            .orderByTaskEndTimePair(Pair.of(true, ORDER_TYPE.DESC))
                            .build());
    
            if (CollectionUtils.isEmpty(beforeTaskList)) {
                throw new RuntimeException("没有已完成的任务可退回");
            }
            // 假设我们想退回到最早的节点
            BpmnHisTask zuiZaoUserTask = beforeTaskList.get(beforeTaskList.size() - 1);
            // 校验一下，从流程定义上来看，从当前节点是否可以往前追溯到该退回点
            boolean reachable = bpmnProcessService.isReachableByBpmnModel(TENANT, bpmnProcessDefinition.getId(),
                    zuiZaoUserTask.getTaskDefinitionKey(), bpmnTask.getTaskDefinitionKey());
            if (!reachable) {
                /*
                 * 假设流程执行A → B → C → D → E， 到E后， E 退回至 C，
                 * 此时在C这里查询历史任务的话，就能查询到A、B、C、D、E，
                 * 如果我们希望，C只能往他之前的A、B，不能忘后面的节点回退的话，还需要进行流程定义上的节点可达验证
                 */
                throw new RuntimeException("从当前节点退回至前一个节点，按理来说，按照流程定义的话，退回到的节点能到达当前节点才对");
            }
    
            // ---------- 执行回退 ----------
            // 因为回退会结束当前活动节点，所以结束前，这里给回退起点任务设置一个业务标识，让我们知道此任务之所以会结束，是因为回退导致的
            bpmnTaskService.setVariableLocal(bpmnTask.getId(), "CustomTaskRemark", "退回至第一个用户任务");
    
            // (视情况而定)， 用一个流程变量记录下，此回退操作下 受影响的任务id
            Set<String> affectedTaskIds = (Set<String>) bpmnTaskService.getVariable(bpmnTask.getId(),
                    "affectedTaskIds");
            if (affectedTaskIds == null) {
                affectedTaskIds = new HashSet<>();
            }
            affectedTaskIds.add(bpmnTask.getId());
            affectedTaskIds.addAll(beforeTaskList.stream().map(BpmnHisTask::getId).collect(Collectors.toList()));
    
            // 回退至第一个任务
            bpmnGoBackService.moveActivityIdTo(bpmnTask.getTaskDefinitionKey(), zuiZaoUserTask.getTaskDefinitionKey(),
                    BpmnActivityStateUpdate.builder()
                            .processInstanceId(bpmnProcessInstanceId)
                            .processVariables(Map.of("退回操作受影响的任务", affectedTaskIds))
                            .build()
            );
    
            infoList.add(String.format("%s退回流程至【%s】。", currUserId, zuiZaoUserTask.getName()));
            
            // ---------- step10. 再次查看当前任务 ----------
            list = bpmnTaskService.list(TENANT,
                    BpmnTaskQuery.builder().processInstanceId(bpmnProcessInstanceId).build());
            bpmnTask = list.get(0);
            infoList.add(String.format("再查查看进度信息。当前待处理任务：【%s】，任务状态：【%s】。", bpmnTask.getName(), bpmnTask.getState()));
    
            // ---------- step11. 张三再次完成任务，再次查看当前任务 ----------
            currUserId = "张三";
            bpmnTaskService.complete(TENANT, bpmnTask.getId(), currUserId, null);
            info = String.format("【%s】完成了【%s】。", currUserId, bpmnTask.getName());
            list = bpmnTaskService.list(TENANT,
                    BpmnTaskQuery.builder().processInstanceId(bpmnProcessInstanceId).build());
            bpmnTask = list.get(0);
            infoList.add(String.format(info + "当前待处理任务：【%s】，任务状态：【%s】。", bpmnTask.getName(), bpmnTask.getState()));
    
    
            // ---------- step12. 李四完成任务 ----------
            currUserId = "李四";
            bpmnTaskService.complete(TENANT, bpmnTask.getId(), currUserId, null);
            info = String.format("【%s】完成了【%s】。", currUserId, bpmnTask.getName());
            list = bpmnTaskService.list(TENANT,
                    BpmnTaskQuery.builder().processInstanceId(bpmnProcessInstanceId).build());
            bpmnTask = list.get(0);
            infoList.add(String.format(info + "当前待处理任务：【%s】，任务状态：【%s】。", bpmnTask.getName(),  bpmnTask.getState()));
            
            // ---------- step13. 王五完成任务 ----------
            currUserId = "王五";
            bpmnTaskService.complete(TENANT, bpmnTask.getId(), currUserId, null);
            info = String.format("【%s】完成了【%s】。", currUserId, bpmnTask.getName());
            list = bpmnTaskService.list(TENANT,
                    BpmnTaskQuery.builder().processInstanceId(bpmnProcessInstanceId).build());
            if (CollectionUtils.isEmpty(list)) {
                infoList.add(info + "流程结束。");
        
                // ---------- step14. 流程结束后，查查查流程节点信息 ----------
                List<? extends BpmnHisTask> hisTaskList = bpmnHistoryService.listHisTask(TENANT,
                        BpmnHisTaskQuery.builder()
                                .processInstanceId(bpmnProcessInstanceId)
                                .queryTaskLocalVariables(true)
                                .queryProcessVariables(true)
                                .build());
                infoList.add(String.format("本流程涉及到的任务节点有：%s", formatStr(hisTaskList)));
            } else {
                throw new RuntimeException("按照流程图，此时流程已经进行完了才对。");
            }
        } finally {
            // 清除测试数据
            if (deploymentId != null) {
                bpmnDeploymentService.undeploy(TENANT, deploymentId, true);
            }
        }
        int size = infoList.size();
        for (int i = 1; i <= size; i++) {
            System.err.println("信息" + i + ": " + infoList.get(i - 1));
            System.err.println();
        }
    }
    
    /**
     * @param clear 是否清除产生的数据
     * @param print 是否打印流程信息
     *
     * @return 流程实例id`
     */
    public String demo4AndDemo5Common(boolean clear, boolean print) {
        List<String> infoList = new ArrayList<>();
        String deploymentId = null;
        String bpmnProcessInstanceId;
        try {
            // ---------- step1. 部署生成 流程定义 ----------
            BpmnDeployment deploy = bpmnDeploymentService.deploy(
                    TENANT, BpmnDeployCreate.builder()
                            .resourceName("全局变量与本地变量测试.bpmn20.xml")
                            .resourceBytes(ResourceUtil.readBytes("全局变量与本地变量测试.bpmn20.xml"))
                            .build()
            );
            deploymentId = deploy.getId();
            
            // ---------- step2. 发起流程 ----------
            // 流程定义key,即为xxx.bpmn20.xml中的<bpmn:process id="xxx" />中的id
            String processDefinitionKey = "qublbdbl";
            BpmnProcessInstance bpmnProcessInstance = bpmnProcessService.startInstanceByKey(TENANT, processDefinitionKey,
                    IdUtil.simpleUUID(), Map.of("k_by_ProcessStart", "我是启动流程时，设置的流程变量"));
            bpmnProcessInstanceId = bpmnProcessInstance.getId();
            infoList.add("流程已发起，流程实例id：" + bpmnProcessInstanceId);
            
            // ---------- step3. 在第1个任务这里观察 流程变量 & 任务本地变量 ----------
            BpmnTask bpmnTask = bpmnTaskService.list(TENANT, BpmnTaskQuery.builder().build()).get(0);
            String taskId = bpmnTask.getId();
            String firstTaskId = taskId;
            String taskNname = bpmnTask.getName();
            String firstTaskNname = taskNname;
            // 设置流程变量
            bpmnTaskService.setVariableProcess(taskId, "sport_" + taskNname, "台球");
            bpmnTaskService.setVariableProcess(taskId, "hobby_" + taskNname, "打游戏");
            // 给自己设置本地变量
            bpmnTaskService.setVariableLocal(taskId, "hobby_" + taskNname, "吃饭");
            infoList.add(
                    String.format("\n%s处的流程变量：", taskNname) + formatStr(bpmnTaskService.getVariablesProcess(taskId))
                            + "\n" +
                            String.format("%s处的本地变量（可以看到，我不包含其它任务的本地变量）：", taskNname) + formatStr(bpmnTaskService.getVariablesLocal(taskId))
                            + "\n" +
                            String.format("%s处的所有变量：", taskNname) + formatStr(bpmnTaskService.getVariables(taskId))
            );
            bpmnTaskService.complete(TENANT, bpmnTask.getId(), "1",
                    Map.of("k_by_ProcessStart",
                            String.format("我是完成【%s】时，设置的流程变量, 可以看到我覆盖了前面的同名流程变量", taskNname))
            );
            
            // ---------- step4. 在第2个任务这里观察 流程变量 & 任务本地变量 ----------
            bpmnTask = bpmnTaskService.list(TENANT, BpmnTaskQuery.builder().build()).get(0);
            taskId = bpmnTask.getId();
            taskNname = bpmnTask.getName();
            // 给自己设置本地变量
            bpmnTaskService.setVariableLocal(taskId, "hobby_" + taskNname, "睡觉");
            infoList.add(
                    String.format("\n%s处的流程变量：", taskNname) + formatStr(bpmnTaskService.getVariablesProcess(taskId))
                            + "\n" +
                            String.format("%s处的本地变量（可以看到，我不包含其它任务的本地变量）：", taskNname) + formatStr(bpmnTaskService.getVariablesLocal(taskId))
                            + "\n" +
                            String.format("%s处的所有变量：", taskNname) + formatStr(bpmnTaskService.getVariables(taskId))
            );
            bpmnTaskService.complete(TENANT, bpmnTask.getId(), "1",
                    Map.of("k_by_ProcessStart",
                            String.format("我是完成【%s】时，设置的流程变量, 可以看到我覆盖了前面的同名流程变量", taskNname))
            );
            
            // ---------- step5. 在第2个任务这里观察 流程变量 & 任务本地变量 ----------
            bpmnTask = bpmnTaskService.list(TENANT, BpmnTaskQuery.builder().build()).get(0);
            taskId = bpmnTask.getId();
            taskNname = bpmnTask.getName();
            // 给自己设置本地变量
            bpmnTaskService.setVariableLocal(taskId, "hobby_" + taskNname, "葛优躺");
            infoList.add(
                    String.format("\n%s处的流程变量：", taskNname) + formatStr(bpmnTaskService.getVariablesProcess(taskId))
                            + "\n" +
                            String.format("%s处的本地变量（可以看到，我不包含其它任务的本地变量）：", taskNname) + formatStr(bpmnTaskService.getVariablesLocal(taskId))
                            + "\n" +
                            String.format("%s处的所有变量：", taskNname) + formatStr(bpmnTaskService.getVariables(taskId))
            );
            bpmnTaskService.complete(TENANT, bpmnTask.getId(), "1", null);
    
    
            // ---------- step6. 在最后我们观察一下第一个任务的流程变量 ----------
            BpmnHisTask firstHisTask = bpmnHistoryService.detailHisTask(TENANT, firstTaskId);
            infoList.add(
                    String.format("\n%s处的流程变量（可以看到，后续任务对流程变量进行了修改的话，获取已完成任务的流程变量时，也会受到影响）：", firstTaskNname) + formatStr(firstHisTask.getProcessVariables())
            );
        } finally {
            // 清除测试数据
            if (deploymentId != null && clear) {
                bpmnDeploymentService.undeploy(TENANT, deploymentId, true);
            }
        }
        if (!print) {
            return bpmnProcessInstanceId;
        }
        int size = infoList.size();
        for (int i = 1; i <= size; i++) {
            System.err.println("信息" + i + ": " + infoList.get(i - 1));
            System.err.println();
        }
        return bpmnProcessInstanceId;
    }
    
    private static String formatStr(Object obj) {
        return JSON.toJSONString(obj, JSONWriter.Feature.NotWriteDefaultValue, JSONWriter.Feature.PrettyFormat,
                JSONWriter.Feature.SortMapEntriesByKeys);
    }
    
    /**
     * 此任务监听器，配合{@link Demo2进阶Controller#demo3()}完成示例 <hr />
     * <p>
     * 之所以【买游戏机审批】和【买游戏机购买】这两个任务会触发此监听器，是因为我们在【买游戏机申请.bpmn20.xml】文件中定义流 程时，就设置了触发这个监听器。相关xml内容：
     * </p>
     * <pre>
     * {@code
     *     <userTask id="sid-B5A01ED3-CF6B-4C9C-BFB8-27C592390BF6" name="买游戏机审批" flowable:formFieldValidation="true">
     *       <extensionElements>
     *         <flowable:taskListener event="create" delegateExpression="${customTaskListener}"></flowable:taskListener>
     *       </extensionElements>
     *     </userTask>
     *     <sequenceFlow id="sid-FDA18157-5CFF-4016-B06B-31F210FC47B1" sourceRef="sid-C1A82427-F754-4234-97F9-1990C5B6481A" targetRef="sid-B5A01ED3-CF6B-4C9C-BFB8-27C592390BF6"></sequenceFlow>
     *     <userTask id="sid-54C8FD90-5158-4782-89CD-24920509DC24" name="买游戏机购买" flowable:formFieldValidation="true">
     *       <extensionElements>
     *         <flowable:taskListener event="create" delegateExpression="${customTaskListener}"></flowable:taskListener>
     *       </extensionElements>
     *     </userTask>
     * }
     * </pre>
     */
    @Component("customTaskListener")
    public static class CustomTaskListener implements BpmnTaskListener {
        
        @Resource
        private BpmnProcessService bpmnProcessService;
        
        @Override
        public void notify(BpmnDelegateTask bpmnDelegateTask) {
            /*
             * 事件类型：
             *  assignment：任务被委派给某人后触发（create之前触发）
             *  create：任务创建时，并且所有的任务属性设置完成后 触发
             *  complete：任务完成后，从运行时数据（runtime data）中删除前触发
             *  delete：在任务将要被删除之前发生（当任务通过completeTask完成任务时，它也会被执行）
             */
            String eventName = bpmnDelegateTask.getEventName();
            if (!EVENTNAME_CREATE.equals(eventName)) {
                return;
            }
            String taskName = bpmnDelegateTask.getName();
            // 根据【买游戏机申请.bpmn20.xml】中的定义，如果任务名称不是【审批】和【购买】，则不处理
            if (!"买游戏机审批".equals(taskName) && !"买游戏机购买".equals(taskName)) {
                return;
            }
            String processInstanceId = bpmnDelegateTask.getProcessInstanceId();
            BpmnProcessInstance bpmnProcessInstance = bpmnProcessService.detailInstance(TENANT, processInstanceId);
            Objects.requireNonNull(bpmnProcessInstance, "bpmnProcessInstance，流程尚未结束，不应该为空呀");
            String startUserId = bpmnProcessInstance.getStartUserId();
            Objects.requireNonNull(startUserId, "请设置流程发起人");
            // 指定任务发起人
            if ("买游戏机审批".equals(taskName)) {
                // 买游戏机要爸爸审批
                bpmnDelegateTask.setAssignee(startUserId + "的爸爸");
            } else {
                // 买游戏机要妈妈购买
                bpmnDelegateTask.setAssignee(startUserId + "的妈妈");
            }
        }
    }
    
    /*
     * 清除数据
     */
    private void clearData() {
        List<? extends BpmnDeployment> list = bpmnDeploymentService.list(TENANT, null);
        for (BpmnDeployment bpmnDeployment : list) {
            bpmnDeploymentService.undeploy(TENANT, bpmnDeployment.getId(), true);
        }
    }
}