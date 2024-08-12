package com.ideaaedi.zoo.example.springboot.bpmn.flowable.controller;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.google.common.collect.Lists;
import com.ideaaedi.zoo.diy.feature.bpmn.api.bpmn20.model.Bpmn20UserTask;
import com.ideaaedi.zoo.diy.feature.bpmn.api.entity.dto.BpmnDeployCreate;
import com.ideaaedi.zoo.diy.feature.bpmn.api.entity.dto.BpmnHisProcessInstanceQuery;
import com.ideaaedi.zoo.diy.feature.bpmn.api.entity.dto.BpmnHisTaskQuery;
import com.ideaaedi.zoo.diy.feature.bpmn.api.entity.dto.BpmnProcessDefinitionQuery;
import com.ideaaedi.zoo.diy.feature.bpmn.api.entity.dto.BpmnTaskQuery;
import com.ideaaedi.zoo.diy.feature.bpmn.api.entity.facade.BpmnAttachment;
import com.ideaaedi.zoo.diy.feature.bpmn.api.entity.facade.BpmnComment;
import com.ideaaedi.zoo.diy.feature.bpmn.api.entity.facade.BpmnDeployment;
import com.ideaaedi.zoo.diy.feature.bpmn.api.entity.facade.BpmnHisProcessInstance;
import com.ideaaedi.zoo.diy.feature.bpmn.api.entity.facade.BpmnHisTask;
import com.ideaaedi.zoo.diy.feature.bpmn.api.entity.facade.BpmnProcessDefinition;
import com.ideaaedi.zoo.diy.feature.bpmn.api.entity.facade.BpmnProcessInstance;
import com.ideaaedi.zoo.diy.feature.bpmn.api.entity.facade.BpmnTask;
import com.ideaaedi.zoo.diy.feature.bpmn.api.enums.ORDER_TYPE;
import com.ideaaedi.zoo.diy.feature.bpmn.api.service.BpmnDeploymentService;
import com.ideaaedi.zoo.diy.feature.bpmn.api.service.BpmnHistoryService;
import com.ideaaedi.zoo.diy.feature.bpmn.api.service.BpmnProcessService;
import com.ideaaedi.zoo.diy.feature.bpmn.api.service.BpmnTaskService;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 *
 */
@RestController
@RequestMapping("/demo-查询")
public class Demo3常见查询Controller {
    
    private static final Set<String> deploymentIdSet = new ConcurrentHashSet<>();
    
    private static final String TENANT = "1,";
    
    private static final String LOCAL_TASK_COMPLETED_BY = "LOCAL_TASK_COMPLETED_BY";
    
    private static final String GLOBAL_PROCESS_STATE_KEY = "PROCESS_STATE";
    private static final String yiFaQiState = "已发起";
    private static final String yiShenQing = "已申请";
    private static final String shenPiTongGuo = "审批通过";
    private static final String shenPiBuTongGuo = "审批不通过";
    private static final String fuShenTongGuo = "复审通过";
    private static final String fuShenBuTongGuo = "复审不通过";
    
    @Resource
    private BpmnDeploymentService bpmnDeploymentService;
    
    @Resource
    private BpmnProcessService bpmnProcessService;
    
    @Resource
    private BpmnTaskService bpmnTaskService;
    
    @Resource
    private BpmnHistoryService historyService;
    
    private static final String ZS_USER_ID = "张三";
    private static final String LS_USER_ID = "李四";
    private static final String WW_USER_ID = "王五";
    
    @SneakyThrows
    @RequestMapping("/常见查询/待处理任务")
    public void demo1() {
        List<String> infoList;
        try {
            // 0. 先清一下数据，避免脏数据影响测试
            //clearData(); // 这将删除所有部署及相关数据，请确保你连接的是自己的demo数据库
            
            // 1.准备测试数据（此时都尚未处理完成任务）
            infoList = prepareData(null, null, null, false); // 本demo不关注最后一个字段，true/false均可
            
            /*
             * 2.查询张三的待处理
             * 因为bpmn中指定的flowable:assignee办理人就是ZS_USER_ID，所以这里直接查ZS_USER_ID即可
             */
            List<? extends BpmnTask> list = bpmnTaskService.list(TENANT, BpmnTaskQuery.builder()
                    .assignee(ZS_USER_ID)
                    .build());
            infoList.add(String.format("此时查询%s的待处理：", ZS_USER_ID) + formatStr(list));
            
            // 3.张三完成任务
            if (!ZS_USER_ID.equals(list.get(0).getAssignee())) {
                // 任务完成前，将任务完成人设置为任务办理人，方便后续查询已完成的任务(P.S.当然，如果你不想修改assignee的话，你也可以借助任务本地变量来实现， 见this.prepareData方法中的示例)
                bpmnTaskService.setAssignee(TENANT, list.get(0).getId(), ZS_USER_ID);
            }
            bpmnTaskService.complete(TENANT, list.get(0).getId(), ZS_USER_ID, null);
            
            // 4.再次查询张三的待处理
            list = bpmnTaskService.list(TENANT, BpmnTaskQuery.builder()
                    .assignee(ZS_USER_ID)
                    .build());
            infoList.add(String.format("张三完成任务后，再查询%s的待处理（发现没有了！）：", ZS_USER_ID) + formatStr(list));
            
            /*
             * 5.查询李四1、李四2的待处理
             * 因为bpmn中指定的【flowable:candidateUsers="李四1,李四2"】办理人候选人组是'李四1,李四2'，所以这里他两应该都能查到这个
             * 待处理任务才对
             */
            String lisi1 = "李四1";
            String lisi2 = "李四2";
            List<? extends BpmnTask> lisi1TaskList = bpmnTaskService.list(TENANT, BpmnTaskQuery.builder()
                    .candidateUser(lisi1)
                    .build());
            Assert.isTrue(lisi1TaskList.size() == 1, "存在脏数据！先清除解开步骤0的代码，清除脏数据后再测试");
            BpmnTask lisi1Task = lisi1TaskList.get(0);
            BpmnTask lisi2Task = bpmnTaskService.list(TENANT, BpmnTaskQuery.builder()
                    .candidateUser(lisi2)
                    .build()).get(0);
            Assert.isTrue(lisi1Task.getId().equals(lisi2Task.getId()), "李四1和李四2看到的任务应该是同一个！");
            infoList.add(String.format("张三完成任务后，%s和%s能同时看到同一个待处理任务：", lisi1, lisi2) + formatStr(lisi1Task));
            
            
            // 6.李四2 完成任务
            if (!lisi2.equals(lisi2Task.getAssignee())) {
                // 任务完成前，将任务完成人设置为任务办理人，方便后续查询已完成的任务(P.S.当然，如果你不想修改assignee的话，你也可以借助任务本地变量来实现， 见this.prepareData方法中的示例)
                bpmnTaskService.setAssignee(TENANT, lisi2Task.getId(), lisi2);
            }
            // 先认领任务（认领任务可避免并发冲突），再完成任务
            bpmnTaskService.claim(TENANT, lisi2Task.getId(), lisi2);
            bpmnTaskService.complete(TENANT, lisi2Task.getId(), lisi2, null);
            
            list = bpmnTaskService.list(TENANT, BpmnTaskQuery.builder()
                    .candidateUser(lisi2)
                    .build());
            Assert.isTrue(CollectionUtils.isEmpty(list), "李四2已经完成了该任务，此时他应该查询不到该任务了才对！");
            list = bpmnTaskService.list(TENANT, BpmnTaskQuery.builder()
                    .candidateUser(lisi1)
                    .build());
            Assert.isTrue(CollectionUtils.isEmpty(list), "李四2已经完成了该任务，此时李四1也应该查询不到该任务了才对！");
            infoList.add("李四2完成任务后，李四2和李四1都查询不到该任务了（符合预期）：" + formatStr(list));
            
            /*
             * 7.查询 用户组【DEPT-A】或【ROLE-B】的待处理
             * 因为bpmn中指定的【flowable:candidateGroups="DEPT-A,ROLE-B"】办理人候选组是【DEPT-A】或【ROLE-B】，所以这里在这两个组织下的人员都能看到才对
             */
            // 假设王五在这些组织下（其中有ROLE-B，在候选组里面）
            List<String> wangWuRoles = Lists.newArrayList("ROLE-B", "xyz", "qwer");
            list = bpmnTaskService.list(TENANT, BpmnTaskQuery.builder()
                    .candidateGroups(wangWuRoles)
                    .build());
            Assert.isTrue(!CollectionUtils.isEmpty(list), "王五拥有【ROLE-B】，照理说能查询出来任务才对！");
            infoList.add("李四2完成任务后，任务到王五这里了：" + formatStr(list));
        } finally {
            // 清除测试数据
            clearData();
        }
        int size = infoList.size();
        for (int i = 1; i <= size; i++) {
            System.err.println("信息" + i + ": " + infoList.get(i - 1));
            System.err.println();
        }
    }
    
    /**
     * <pre>
     * demo1存在的问题：查询还需要根据用户是（办理人、还是候选人、还是候选组）来分别调用不同的查询方法；
     *                但是很多时候，用户本身是不知道自己是什么身份的，所以这里就需要一个全查的方法了。
     *
     *          需要这样一个方法：只要用户是 办理人 or 候选人 or 在候选组里面， 那么就能查出来。
     *          这个方法就是：{@link BpmnTaskQuery#setUserIdAndUserGroupsPair}
     * </pre>
     */
    @SneakyThrows
    @RequestMapping("/常见查询/待处理任务-全场景查询")
    public void demo2() {
        List<String> infoList;
        try {
            // 0. 先清一下数据，避免脏数据影响测试
            //clearData(); // 这将删除所有部署及相关数据，请确保你连接的是自己的demo数据库
            
            // 1.准备测试数据（此时都尚未处理完成任务）
            infoList = prepareData(null, null, null, true);// 本demo不关注最后一个字段，true/false均可
            
            /*
             * 2.查询张三的待处理
             * 因为bpmn中指定的flowable:assignee办理人就是ZS_USER_ID，所以这里直接查ZS_USER_ID即可
             */
            List<? extends BpmnTask> list = bpmnTaskService.list(TENANT, BpmnTaskQuery.builder()
                    .userIdAndUserGroupsPair(Pair.of(ZS_USER_ID, null))
                    .build());
            infoList.add(String.format("此时查询%s的待处理：", ZS_USER_ID) + formatStr(list));
            
            // 3.张三完成任务
            if (!ZS_USER_ID.equals(list.get(0).getAssignee())) {
                // 任务完成前，将任务完成人设置为任务办理人，方便后续查询已完成的任务(P.S.当然，如果你不想修改assignee的话，你也可以借助任务本地变量来实现， 见this.prepareData方法中的示例)
                bpmnTaskService.setAssignee(TENANT, list.get(0).getId(), ZS_USER_ID);
            }
            bpmnTaskService.complete(TENANT, list.get(0).getId(), ZS_USER_ID, null);
            
            // 4.再次查询张三的待处理
            list = bpmnTaskService.list(TENANT, BpmnTaskQuery.builder()
                    .userIdAndUserGroupsPair(Pair.of(ZS_USER_ID, null))
                    .build());
            infoList.add(String.format("张三完成任务后，再查询%s的待处理（发现没有了！）：", ZS_USER_ID) + formatStr(list));
            
            /*
             * 5.查询李四1、李四2的待处理
             * 因为bpmn中指定的【flowable:candidateUsers="李四1,李四2"】办理人候选人组是'李四1,李四2'，所以这里他两应该都能查到这个
             * 待处理任务才对
             */
            String lisi1 = "李四1";
            String lisi2 = "李四2";
            List<? extends BpmnTask> lisi1TaskList = bpmnTaskService.list(TENANT, BpmnTaskQuery.builder()
                    .userIdAndUserGroupsPair(Pair.of(lisi1, null))
                    .build());
            Assert.isTrue(lisi1TaskList.size() == 1, "存在脏数据！先清除解开步骤0的代码，清除脏数据后再测试");
            BpmnTask lisi1Task = lisi1TaskList.get(0);
            BpmnTask lisi2Task = bpmnTaskService.list(TENANT, BpmnTaskQuery.builder()
                    .userIdAndUserGroupsPair(Pair.of(lisi2, null))
                    .build()).get(0);
            Assert.isTrue(lisi1Task.getId().equals(lisi2Task.getId()), "李四1和李四2看到的任务应该是同一个！");
            infoList.add(String.format("张三完成任务后，%s和%s能同时看到同一个待处理任务：", lisi1, lisi2) + formatStr(lisi1Task));
            
            
            // 6.李四2 完成任务
            if (!lisi2.equals(lisi2Task.getAssignee())) {
                // 任务完成前，将任务完成人设置为任务办理人，方便后续查询已完成的任务(P.S.当然，如果你不想修改assignee的话，你也可以借助任务本地变量来实现， 见this.prepareData方法中的示例)
                bpmnTaskService.setAssignee(TENANT, lisi2Task.getId(), lisi2);
            }
            
            // 先认领任务（认领任务可避免并发冲突），再完成任务
            bpmnTaskService.claim(TENANT, lisi2Task.getId(), lisi2);
            bpmnTaskService.complete(TENANT, lisi2Task.getId(), lisi2, null);
            list = bpmnTaskService.list(TENANT, BpmnTaskQuery.builder()
                    .userIdAndUserGroupsPair(Pair.of(lisi2, null))
                    .build());
            Assert.isTrue(CollectionUtils.isEmpty(list), "李四2已经完成了该任务，此时他应该查询不到该任务了才对！");
            list = bpmnTaskService.list(TENANT, BpmnTaskQuery.builder()
                    .userIdAndUserGroupsPair(Pair.of(lisi1, null))
                    .build());
            Assert.isTrue(CollectionUtils.isEmpty(list), "李四2已经完成了该任务，此时李四1也应该查询不到该任务了才对！");
            infoList.add("李四2完成任务后，李四2和李四1都查询不到该任务了（符合预期）：" + formatStr(list));
            
            /*
             * 7.查询 用户组【DEPT-A】或【ROLE-B】的待处理
             * 因为bpmn中指定的【flowable:candidateGroups="DEPT-A,ROLE-B"】办理人候选组是【DEPT-A】或【ROLE-B】，所以这里在这两个组织下的人员都能看到才对
             */
            // 假设王五在这些组织下（其中有ROLE-B，在候选组里面）
            List<String> wangWuRoles = Lists.newArrayList("ROLE-B", "xyz", "qwer");
            list = bpmnTaskService.list(TENANT, BpmnTaskQuery.builder()
                    .userIdAndUserGroupsPair(Pair.of(null, wangWuRoles))
                    .build());
            Assert.isTrue(!CollectionUtils.isEmpty(list), "王五拥有【ROLE-B】，照理说能查询出来任务才对！");
            infoList.add("李四2完成任务后，任务到王五这里了：" + formatStr(list));
        } finally {
            // 清除测试数据
            clearData();
        }
        int size = infoList.size();
        for (int i = 1; i <= size; i++) {
            System.err.println("信息" + i + ": " + infoList.get(i - 1));
            System.err.println();
        }
    }
    
    @SneakyThrows
    @RequestMapping("/常见查询/已处理任务")
    public void demo3() {
        List<String> infoList;
        try {
            // 0. 先清一下数据，避免脏数据影响测试
            //clearData(); // 这将删除所有部署及相关数据，请确保你连接的是自己的demo数据库
            
            boolean useAssigneeRecordCompletedBy = ThreadLocalRandom.current().nextBoolean();
            
            // 1.准备测试数据（此时都已完成任务）
            infoList = prepareData(ZS_USER_ID, LS_USER_ID, WW_USER_ID, useAssigneeRecordCompletedBy);
            
            if (useAssigneeRecordCompletedBy) {
                infoList.add("上面的步骤中，当用户完成任务时， 使用的是【将任务完成人赋值给Assignee】的方式，来记录任务完成人，那么下面查询的时候就要用对应的查询条件");
            } else {
                infoList.add("上面的步骤中，当用户完成任务时， 使用的是【将任务完成人记录到任务本地变量】的方式，来记录任务完成人，那么下面查询的时候就要用对应的查询条件");
            }
            
            /*
             * 2.查询张三的已处理
             */
            BpmnHisTaskQuery.BpmnHisTaskQueryBuilder builder = BpmnHisTaskQuery.builder()
                    .ifFinished(true)
                    .queryTaskLocalVariables(true)
                    .orderByTaskEndTimePair(Pair.of(true, ORDER_TYPE.DESC));
            if (useAssigneeRecordCompletedBy) {
                builder.assignee(ZS_USER_ID);
            } else {
                builder.taskVariableValueEqualsColl(
                        Lists.newArrayList(
                                Pair.of(LOCAL_TASK_COMPLETED_BY, ZS_USER_ID)
                        )
                );
            }
            List<? extends BpmnHisTask> list = historyService.listHisTask(
                    TENANT,
                    builder.build()
            );
            infoList.add(String.format("此时查询%s的已处理：", ZS_USER_ID) + formatStr(list));
            
            /*
             * 3.查询李四的已处理
             */
            builder = BpmnHisTaskQuery.builder()
                    .ifFinished(true)
                    .queryTaskLocalVariables(true)
                    .orderByTaskEndTimePair(Pair.of(true, ORDER_TYPE.DESC));
            if (useAssigneeRecordCompletedBy) {
                builder.assignee(LS_USER_ID);
            } else {
                builder.taskVariableValueEqualsColl(
                        Lists.newArrayList(
                                Pair.of(LOCAL_TASK_COMPLETED_BY, LS_USER_ID)
                        )
                );
            }
            list = historyService.listHisTask(
                    TENANT,
                    builder.build()
            );
            infoList.add(String.format("此时查询%s的已处理：", LS_USER_ID) + formatStr(list));
            
            /*
             * 4.查询王五的已处理
             */
            builder = BpmnHisTaskQuery.builder()
                    .ifFinished(true)
                    .queryTaskLocalVariables(true)
                    .orderByTaskEndTimePair(Pair.of(true, ORDER_TYPE.DESC));
            if (useAssigneeRecordCompletedBy) {
                builder.assignee(WW_USER_ID);
            } else {
                builder.taskVariableValueEqualsColl(
                        Lists.newArrayList(
                                Pair.of(LOCAL_TASK_COMPLETED_BY, WW_USER_ID)
                        )
                );
            }
            list = historyService.listHisTask(
                    TENANT,
                    builder.build()
            );
            infoList.add(String.format("此时查询%s的已处理：", WW_USER_ID) + formatStr(list));
        } finally {
            // 清除测试数据
            clearData();
        }
        int size = infoList.size();
        for (int i = 1; i <= size; i++) {
            System.err.println("信息" + i + ": " + infoList.get(i - 1));
            System.err.println();
        }
    }
    
    @SneakyThrows
    @RequestMapping("/常见查询/已发起流程")
    public void demo4() {
        List<String> infoList = new ArrayList<>();
        try {
            // 0. 先清一下数据，避免脏数据影响测试
            //clearData(); // 这将删除所有部署及相关数据，请确保你连接的是自己的demo数据库
            
            // 1.准备测试数据（发起多个流程）
            prepareData(ZS_USER_ID, LS_USER_ID, WW_USER_ID, true);  // 本demo不关注最后一个字段，true/false均可
            prepareData(ZS_USER_ID, LS_USER_ID, null, true);
            prepareData(ZS_USER_ID, null, null, true);
            
            // 2. 查询已发起流程
            List<? extends BpmnHisProcessInstance> list = historyService.listHisProcessInstance(
                    TENANT,
                    BpmnHisProcessInstanceQuery.builder()
                            .startedBy(ZS_USER_ID) // 对应发起流程时，需要设置流程发起人（即： BpmnProcessService.startByKey(...)
                            // 时，参数userId）， 要不然这里查询不到
                            .queryProcessVariables(true)
                            .build()
            );
            infoList.add(String.format("此时查询%s的已发起流程：", ZS_USER_ID) + formatStr(list));
        } finally {
            // 清除测试数据
            clearData();
        }
        int size = infoList.size();
        for (int i = 1; i <= size; i++) {
            System.err.println("信息" + i + ": " + infoList.get(i - 1));
            System.err.println();
        }
    }
    
    @SneakyThrows
    @RequestMapping("/常见查询/流程进度详情-不含未知节点")
    public void demo5() {
        List<String> infoList = new ArrayList<>();
        try {
            // 0. 先清一下数据，避免脏数据影响测试
            //clearData(); // 这将删除所有部署及相关数据，请确保你连接的是自己的demo数据库
            
            // 1.准备测试数据（发起多个流程）
            if (ThreadLocalRandom.current().nextBoolean()) {
                prepareData(ZS_USER_ID, LS_USER_ID, WW_USER_ID, true); // 本demo不关注最后一个字段，true/false均可
            } else {
                prepareData(ZS_USER_ID, LS_USER_ID, null, true); // 本demo不关注最后一个字段，true/false均可
            }
            
            // 2. 查询已发起流程
            List<? extends BpmnHisProcessInstance> list = historyService.listHisProcessInstance(
                    TENANT,
                    BpmnHisProcessInstanceQuery.builder()
                            .startedBy(ZS_USER_ID) // 对应发起流程时，需要设置流程发起人（即： BpmnProcessService.startByKey(...)
                            // 时，参数userId）， 要不然这里查询不到
                            .build()
            );
            BpmnHisProcessInstance bpmnHisProcessInstance = list.get(0);
            String processInstanceId = bpmnHisProcessInstance.getId();
            String processName = ZS_USER_ID + "发起的【"+ bpmnHisProcessInstance.getProcessDefinitionName() + "】流程";
            
            infoList.add(String.format("流程：%s， 流程实例id：%s", processName,
                    processInstanceId));
            
            // 2. 查询流程各任务节点
            List<? extends BpmnHisTask> bpmnHisTaskList = historyService.listHisTask(TENANT, BpmnHisTaskQuery.builder()
                    .processInstanceId(processInstanceId)
                    .orderByTaskStartTimePair(Pair.of(true, ORDER_TYPE.ASC))
                    .build());
            
            // 流程评论
            List<? extends BpmnComment> processInstanceCommentList = bpmnTaskService.getProcessInstanceComments
            (processInstanceId);
            // 任务评论
            Map<String, ? extends List<? extends BpmnComment>> taskIdAndCommentsMap =
                    processInstanceCommentList.stream().filter(x -> Objects.nonNull(x.getTaskId()) && "comment".equals(x.getType()))
                    .collect(Collectors.groupingBy(BpmnComment::getTaskId));
            // 流程附件
            List<? extends BpmnAttachment> processInstanceAttachments = bpmnTaskService
            .getProcessInstanceAttachments(processInstanceId);
            // 任务附件
            Map<String, ? extends List<? extends BpmnAttachment>> taskIdAndAttachmentsMap =
                    processInstanceAttachments.stream().filter(x -> Objects.nonNull(x.getTaskId()))
                            .collect(Collectors.groupingBy(BpmnAttachment::getTaskId));
            
            for (int i = 0; i < bpmnHisTaskList.size(); i++) {
                BpmnHisTask bpmnHisTask = bpmnHisTaskList.get(i);
                String taskId = bpmnHisTask.getId();
                infoList.add(String.format("【节点%s】：", (i + 1)) +
                        "任务信息：" + formatStr(bpmnHisTask)
                        + "\n 评论信息：" + formatStr(taskIdAndCommentsMap.get(taskId))
                        + "\n 附件信息：" + formatStr(taskIdAndAttachmentsMap.get(taskId))
                        + "\n 任务状态：" + (bpmnHisTask.getEndTime() == null ? "待处理" : "已完成")
                );
            }
        } finally {
            // 清除测试数据
            clearData();
        }
        int size = infoList.size();
        for (int i = 1; i <= size; i++) {
            System.err.println("信息" + i + ": " + infoList.get(i - 1));
            System.err.println();
        }
    }
    
    
    @SneakyThrows
    @RequestMapping("/常见查询/流程进度详情-含未知节点")
    public void demo6() {
        List<String> infoList = new ArrayList<>();
        try {
            // 0. 先清一下数据，避免脏数据影响测试
            //clearData(); // 这将删除所有部署及相关数据，请确保你连接的是自己的demo数据库
        
            // 1.准备测试数据（发起多个流程）
            prepareData(ZS_USER_ID, null, null, true); // 本demo不关注最后一个字段，true/false均可
        
            // 2. 查询已发起流程
            List<? extends BpmnHisProcessInstance> list = historyService.listHisProcessInstance(
                    TENANT,
                    BpmnHisProcessInstanceQuery.builder()
                            .startedBy(ZS_USER_ID) // 对应发起流程时，需要设置流程发起人（即： BpmnProcessService.startByKey(...)
                            // 时，参数userId）， 要不然这里查询不到
                            .build()
            );
            BpmnHisProcessInstance bpmnHisProcessInstance = list.get(0);
            String processDefinitionId = bpmnHisProcessInstance.getProcessDefinitionId();
            String processInstanceId = bpmnHisProcessInstance.getId();
            String processName = ZS_USER_ID + "发起的【"+ bpmnHisProcessInstance.getProcessDefinitionName() + "】流程";
        
            infoList.add(String.format("流程：%s， 流程实例id：%s", processName,
                    processInstanceId));
        
            // 2. 查询流程各任务节点
            List<? extends BpmnHisTask> bpmnHisTaskList = historyService.listHisTask(TENANT, BpmnHisTaskQuery.builder()
                    .processInstanceId(processInstanceId)
                    .orderByTaskStartTimePair(Pair.of(true, ORDER_TYPE.ASC))
                    .build());
        
            // 流程评论
            List<? extends BpmnComment> processInstanceCommentList = bpmnTaskService.getProcessInstanceComments
                    (processInstanceId);
            // 任务评论
            Map<String, ? extends List<? extends BpmnComment>> taskIdAndCommentsMap =
                    processInstanceCommentList.stream().filter(x -> Objects.nonNull(x.getTaskId()) && "comment".equals(x.getType()))
                            .collect(Collectors.groupingBy(BpmnComment::getTaskId));
            // 流程附件
            List<? extends BpmnAttachment> processInstanceAttachments = bpmnTaskService
                    .getProcessInstanceAttachments(processInstanceId);
            // 任务附件
            Map<String, ? extends List<? extends BpmnAttachment>> taskIdAndAttachmentsMap =
                    processInstanceAttachments.stream().filter(x -> Objects.nonNull(x.getTaskId()))
                            .collect(Collectors.groupingBy(BpmnAttachment::getTaskId));
        
            // -------------------------- 已知的节点 --------------------------
            for (int i = 0; i < bpmnHisTaskList.size(); i++) {
                BpmnHisTask bpmnHisTask = bpmnHisTaskList.get(i);
                String taskId = bpmnHisTask.getId();
                infoList.add(String.format("【已知节点-%s】：", (i + 1)) +
                        "任务信息：" + formatStr(bpmnHisTask)
                        + "\n 评论信息：" + formatStr(taskIdAndCommentsMap.get(taskId))
                        + "\n 附件信息：" + formatStr(taskIdAndAttachmentsMap.get(taskId))
                        + "\n 任务状态：" + (bpmnHisTask.getEndTime() == null ? "待处理" : "已完成")
                );
            }
    
            Set<String> knownTaskDefKeySet = bpmnHisTaskList.stream().map(BpmnTask::getTaskDefinitionKey).collect(Collectors.toSet());
            
            
            // -------------------------- 未知的节点（已执行或执行中的任务为已知节点，后续的为未知节点） --------------------------
            List<Bpmn20UserTask> bpmn20UserTasks = bpmnProcessService.queryUserTaskFromBpmnModel(TENANT,
                    processDefinitionId, null);
    
            int count = 1;
            for (Bpmn20UserTask bpmn20UserTask : bpmn20UserTasks) {
                boolean contains = knownTaskDefKeySet.contains(bpmn20UserTask.getId());
                if (contains) {
                    continue;
                }
                String assignee = bpmn20UserTask.getAssignee();
                List<String> candidateUsers = bpmn20UserTask.getCandidateUsers();
                List<String> candidateGroups = bpmn20UserTask.getCandidateGroups();
                String description = bpmn20UserTask.getDescription();
                String name = bpmn20UserTask.getName();
                infoList.add(
                        String.format("未知节点-%s：\n\t\t任务名：%s\n\t\t任务描述:%s\n\t\t办理人：%s\n\t\t候选人：%s\n\t\t候选组：%s",
                            count,
                            name,
                            StringUtils.defaultIfBlank(description, "（无）"),
                            StringUtils.defaultIfBlank(assignee, "（未指定）"),
                           CollectionUtils.isEmpty(candidateUsers) ? "（无）" : String.join("、", candidateUsers),
                           CollectionUtils.isEmpty(candidateGroups) ? "（无）" : String.join("、", candidateGroups)
                        )
                );
                count++;
            }
        } finally {
            // 清除测试数据
            clearData();
        }
        int size = infoList.size();
        for (int i = 1; i <= size; i++) {
            System.err.println("信息" + i + ": " + infoList.get(i - 1));
            System.err.println();
        }
    }
    
    
    /**
     *
     * @param recordProcessStateByBusinessState 是否已流程business-status字段记录流程状态
     * <br /><br />
     * true-通过流程的business-status记录流程状态；<br />
     * false-通过流程变量记录流程状态
     */
    @SneakyThrows
    @RequestMapping("/常见查询/按流程状态查询流程")
    public void demo7(boolean recordProcessStateByBusinessState) {
        String faQiRen = "流程发起人Deng";
        List<String> infoList = new ArrayList<>();
        try {
            // 0. 先清一下数据，避免脏数据影响测试
            //clearData(); // 这将删除所有部署及相关数据，请确保你连接的是自己的demo数据库
        
            // 1.准备测试数据（发起多个流程）
            // 准备 yiFaQiState 状态的流程
            prepareDataProcessState(faQiRen, null, null, null, null, null, recordProcessStateByBusinessState);
            // 准备 yiShenQing 状态的流程
            prepareDataProcessState(faQiRen, ZS_USER_ID, null, null, null, null, recordProcessStateByBusinessState);
            // 准备 shenPiTongGuo 状态的流程
            prepareDataProcessState(faQiRen, ZS_USER_ID, LS_USER_ID, true, null, null, recordProcessStateByBusinessState);
            // 准备 shenPiBuTongGuo 状态的流程
            prepareDataProcessState(faQiRen, ZS_USER_ID, LS_USER_ID, false, null, null, recordProcessStateByBusinessState);
            // 准备 fuShenTongGuo 状态的流程
            prepareDataProcessState(faQiRen, ZS_USER_ID, LS_USER_ID, true, WW_USER_ID, true, recordProcessStateByBusinessState);
            // 准备 fuShenBuTongGuo 状态的流程
            prepareDataProcessState(faQiRen, ZS_USER_ID, LS_USER_ID, true, WW_USER_ID, false, recordProcessStateByBusinessState);
        
            // 查询 yiFaQiState 状态的流程
            BpmnHisProcessInstanceQuery.BpmnHisProcessInstanceQueryBuilder builder =
                    BpmnHisProcessInstanceQuery.builder()
                    .startedBy(faQiRen)
                    .queryProcessVariables(true);
            if (recordProcessStateByBusinessState) {
                builder.businessStatus(yiFaQiState);
            } else {
                builder.variableValuePairColl(
                        Lists.newArrayList(
                                Pair.of(GLOBAL_PROCESS_STATE_KEY, yiFaQiState)
                        )
                );
            }
            List<? extends BpmnHisProcessInstance> list = historyService.listHisProcessInstance(TENANT, builder.build());
            infoList.add(String.format("查询【%s】状态的流程：", yiFaQiState) + formatStr(list));
        
            // 查询 yiShenQing 状态的流程
            builder =
                    BpmnHisProcessInstanceQuery.builder()
                            .startedBy(faQiRen)
                            .queryProcessVariables(true);
            if (recordProcessStateByBusinessState) {
                builder.businessStatus(yiShenQing);
            } else {
                builder.variableValuePairColl(
                        Lists.newArrayList(
                                Pair.of(GLOBAL_PROCESS_STATE_KEY, yiShenQing)
                        )
                );
            }
            list = historyService.listHisProcessInstance(TENANT, builder.build());
            infoList.add(String.format("查询【%s】状态的流程：", yiShenQing) + formatStr(list));
        
            // 查询 shenPiTongGuo 状态的流程
            builder =
                    BpmnHisProcessInstanceQuery.builder()
                            .startedBy(faQiRen)
                            .queryProcessVariables(true);
            if (recordProcessStateByBusinessState) {
                builder.businessStatus(shenPiTongGuo);
            } else {
                builder.variableValuePairColl(
                        Lists.newArrayList(
                                Pair.of(GLOBAL_PROCESS_STATE_KEY, shenPiTongGuo)
                        )
                );
            }
            list = historyService.listHisProcessInstance(TENANT, builder.build());
            infoList.add(String.format("查询【%s】状态的流程：", shenPiTongGuo) + formatStr(list));
        
            // 查询 shenPiBuTongGuo 状态的流程
            builder = BpmnHisProcessInstanceQuery.builder()
                            .startedBy(faQiRen)
                            .queryProcessVariables(true);
            if (recordProcessStateByBusinessState) {
                builder.businessStatus(shenPiBuTongGuo);
            } else {
                builder.variableValuePairColl(
                        Lists.newArrayList(
                                Pair.of(GLOBAL_PROCESS_STATE_KEY, shenPiBuTongGuo)
                        )
                );
            }
            list = historyService.listHisProcessInstance(TENANT, builder.build());
            infoList.add(String.format("查询【%s】状态的流程：", shenPiBuTongGuo) + formatStr(list));
        
            // 查询 fuShenBuTongGuo 状态的流程
            builder = BpmnHisProcessInstanceQuery.builder()
                    .startedBy(faQiRen)
                    .queryProcessVariables(true);
            if (recordProcessStateByBusinessState) {
                builder.businessStatus(fuShenBuTongGuo);
            } else {
                builder.variableValuePairColl(
                        Lists.newArrayList(
                                Pair.of(GLOBAL_PROCESS_STATE_KEY, fuShenBuTongGuo)
                        )
                );
            }
            list = historyService.listHisProcessInstance(TENANT, builder.build());
            infoList.add(String.format("查询【%s】状态的流程：", fuShenBuTongGuo) + formatStr(list));
        
            // 查询 fuShenTongGuo 状态的流程
            builder = BpmnHisProcessInstanceQuery.builder()
                    .startedBy(faQiRen)
                    .queryProcessVariables(true);
            if (recordProcessStateByBusinessState) {
                builder.businessStatus(fuShenTongGuo);
            } else {
                builder.variableValuePairColl(
                        Lists.newArrayList(
                                Pair.of(GLOBAL_PROCESS_STATE_KEY, fuShenTongGuo)
                        )
                );
            }
            list = historyService.listHisProcessInstance(TENANT, builder.build());
            infoList.add(String.format("查询【%s】状态的流程：", fuShenTongGuo) + formatStr(list));
        } finally {
            // 清除测试数据
            clearData();
        }
        int size = infoList.size();
        for (int i = 1; i <= size; i++) {
            System.err.println("信息" + i + ": " + infoList.get(i - 1));
            System.err.println();
        }
    }
    
    /**
     * 准备数据
     *
     * @param zs 申请人。当为空时，表示不进行申请
     * @param ls 审批人。当为空时，表示不进行审批
     * @param ww 复审人。当为空时，表示不进行复审
     * @param useAssigneeRecordCompletedBy true-直接将任务完成人记录到任务办理人字段里；false-使用任务本地变量来记录任务完成人
     *
     * @return 相关信息
     */
    private List<String> prepareData(@Nullable String zs, @Nullable String ls,
                                     @Nullable String ww, boolean useAssigneeRecordCompletedBy) {
        List<String> infoList = new ArrayList<>();
        // ---------- step1. 部署生成 流程定义 ----------
        BpmnDeployment deploy = bpmnDeploymentService.deploy(
                TENANT, BpmnDeployCreate.builder()
                        .name("张三李四王五")
                        .resourceName("张三李四王五.bpmn20.xml")
                        .resourceBytes(ResourceUtil.readBytes("张三李四王五.bpmn20.xml"))
                        .build()
        );
        String deploymentId = deploy.getId();
        deploymentIdSet.add(deploymentId);
        infoList.add("部署id：" + deploymentId);
        List<? extends BpmnProcessDefinition> bpmnProcessDefinitions = bpmnProcessService.listDefinition(TENANT,
                BpmnProcessDefinitionQuery.builder().deploymentId(deploymentId).build());
        infoList.add("流程定义key：" + bpmnProcessDefinitions.get(0).getKey() + "，流程定义id：" + bpmnProcessDefinitions.get(0).getId()); // 因为本bpmn文件中只定义了一个流程，所以这里取第一个即可
        
        // ---------- step2. 根据流程定义发起流程 ----------
        // 流程定义key,即为xxx.bpmn20.xml中的<bpmn:process id="xxx" />中的id。值等价于 bpmnProcessDefinitions.get(0).getKey()
        String processDefinitionKey = "zslsww";
        BpmnProcessInstance bpmnProcessInstance = bpmnProcessService.startInstanceByKey(TENANT, processDefinitionKey,
                IdUtil.simpleUUID(), null, zs
        );
        String bpmnProcessInstanceId = bpmnProcessInstance.getId();
        infoList.add("流程实例id：" + bpmnProcessInstanceId);
        
        // ---------- step3. （流程自动流转，）查看任务 ----------
        List<? extends BpmnTask> list = bpmnTaskService.list(TENANT,
                BpmnTaskQuery.builder().processInstanceId(bpmnProcessInstanceId).build());
        infoList.add("发起流程后， 当前任务流转到了：" + formatStr(list));
        
        if (StringUtils.isBlank(zs)) {
            return infoList;
        }
        BpmnTask bpmnTask = list.get(0);
    
        if (!zs.equals(bpmnTask.getAssignee())) {
            // 先申领任务，再完成任务
            bpmnTaskService.claim(TENANT, bpmnTask.getId(), zs);
        }
        /*
         * 任务完成前，将任务完成人设置为任务办理人，方便后续查询已完成的任务
         *
         * P.S.当然，如果你不想修改assignee的话，你也可以借助其它方式来实现，如：使用任务本地变量设置完成人id
         */
        if (useAssigneeRecordCompletedBy) {
            if (!zs.equals(bpmnTask.getAssignee())) {
                bpmnTaskService.setAssignee(TENANT, bpmnTask.getId(), zs);
            }
        } else {
            bpmnTaskService.setVariableLocal(bpmnTask.getId(), LOCAL_TASK_COMPLETED_BY, zs);
        }
        bpmnTaskService.addComment(bpmnTask.getId(), bpmnProcessInstanceId, zs + "的评论：" + "请假拉屎");
        bpmnTaskService.addAttachment("附件名1", "附件类型1", "附件描述1",
                bpmnTask.getId(), bpmnProcessInstanceId, "附件内容1".getBytes());
        bpmnTaskService.complete(TENANT, bpmnTask.getId(), zs, null);
        list = bpmnTaskService.list(TENANT,
                BpmnTaskQuery.builder().processInstanceId(bpmnProcessInstanceId).queryTaskLocalVariables(true).build());
        infoList.add(String.format("%s完成了申请后， 当前任务流转到了：", zs) + formatStr(list));
        if (StringUtils.isBlank(ls)) {
            return infoList;
        }
    
        bpmnTask = list.get(0);
        if (!ls.equals(bpmnTask.getAssignee())) {
            // 先申领任务，再完成任务
            bpmnTaskService.claim(TENANT, bpmnTask.getId(), ls);
        }

        /*
         * 任务完成前，将任务完成人设置为任务办理人，方便后续查询已完成的任务
         *
         * P.S.当然，如果你不想修改assignee的话，你也可以借助其它方式来实现，如：使用任务本地变量设置完成人id
         */
        if (useAssigneeRecordCompletedBy) {
            if (!ls.equals(bpmnTask.getAssignee())) {
                bpmnTaskService.setAssignee(TENANT, bpmnTask.getId(), ls);
            }
        } else {
            bpmnTaskService.setVariableLocal(bpmnTask.getId(), LOCAL_TASK_COMPLETED_BY, ls);
        }
        bpmnTaskService.addComment(bpmnTask.getId(), bpmnProcessInstanceId, ls + "的评论：" + String.format("打小就看%s这小子行，通过！", zs));
        bpmnTaskService.addAttachment("附件名2", "附件类型2", "附件描述2",
                bpmnTask.getId(), bpmnProcessInstanceId, "附件内容2".getBytes());
        bpmnTaskService.complete(TENANT, bpmnTask.getId(), ls, null);
        list = bpmnTaskService.list(TENANT,
                BpmnTaskQuery.builder().processInstanceId(bpmnProcessInstanceId).queryTaskLocalVariables(true).build());
        infoList.add(String.format("%s完成了审核后， 当前任务流转到了：", ls) + formatStr(list));
        if (StringUtils.isBlank(ww)) {
            return infoList;
        }
    
        bpmnTask = list.get(0);
        if (!ww.equals(bpmnTask.getAssignee())) {
            // 先申领任务，再完成任务
            bpmnTaskService.claim(TENANT, bpmnTask.getId(), ww);
        }
        /*
         * 任务完成前，将任务完成人设置为任务办理人，方便后续查询已完成的任务
         *
         * P.S.当然，如果你不想修改assignee的话，你也可以借助其它方式来实现，如：使用任务本地变量设置完成人id
         */
        if (useAssigneeRecordCompletedBy) {
            if (!zs.equals(bpmnTask.getAssignee())) {
                bpmnTaskService.setAssignee(TENANT, bpmnTask.getId(), ww);
            }
        } else {
            bpmnTaskService.setVariableLocal(bpmnTask.getId(), LOCAL_TASK_COMPLETED_BY, ww);
        }
        bpmnTaskService.addComment(bpmnTask.getId(), bpmnProcessInstanceId, ww + "的评论：" + "准~");
        bpmnTaskService.addAttachment("附件名3", "附件类型3", "附件描述3",
                bpmnTask.getId(), bpmnProcessInstanceId, "附件内容3".getBytes());
        bpmnTaskService.complete(TENANT, bpmnTask.getId(), ww, null);
        list = bpmnTaskService.list(TENANT,
                BpmnTaskQuery.builder().processInstanceId(bpmnProcessInstanceId).queryTaskLocalVariables(true).build());
        if (CollectionUtils.isEmpty(list)) {
            bpmnProcessInstance = bpmnProcessService.detailInstance(TENANT, bpmnProcessInstanceId);
            Assert.isTrue(bpmnProcessInstance == null, "流程实例执行完毕后，bpmnProcessService.detailInstance接口应该就查询不到流程实例信息才对！");
            infoList.add(String.format("%s何完成了审核后， 流程实例执行完毕啦！", ww));
        } else {
            throw new IllegalStateException("按照[张三李四王五.bpmn20.xml]中的定义，流程应该执行完了才对，任务列表因为为空才对！");
        }
        return infoList;
    }
    
    /**
     * 准备流程状态数据
     *
     * @param faQiRen 流程发起人
     * @param zs 申请人。当为空时，表示不进行申请
     * @param ls 审批人。当为空时，表示不进行审批
     * @param lsPass ls是否审核通过
     * @param ww 复审人。当为空时，表示不进行复审
     * @param wwPass ww是否复审通过
     * @param recordProcessStateByBusinessState true-通过流程的business-status记录流程状态；false-通过流程变量记录流程状态
     */
    private void prepareDataProcessState(@Nullable String faQiRen, @Nullable String zs,
                                         @Nullable String ls, @Nullable Boolean lsPass,
                                         @Nullable String ww, @Nullable Boolean wwPass,
                                         boolean recordProcessStateByBusinessState
    ) {
        // ---------- step1. 部署生成 流程定义 ----------
        bpmnDeploymentService.deploy(
                TENANT, BpmnDeployCreate.builder()
                        .name("张三李四王五")
                        .resourceName("张三李四王五.bpmn20.xml")
                        .resourceBytes(ResourceUtil.readBytes("张三李四王五.bpmn20.xml"))
                        .build()
        );
        
        // ---------- step2. 根据流程定义发起流程 ----------
        // 流程定义key,即为xxx.bpmn20.xml中的<bpmn:process id="xxx" />中的id。值等价于 bpmnProcessDefinitions.get(0).getKey()
        String processDefinitionKey = "zslsww";
        String bpmnProcessInstanceId;
        if (recordProcessStateByBusinessState) {
            BpmnProcessInstance bpmnProcessInstance = bpmnProcessService.startInstanceByKey(TENANT, processDefinitionKey,
                    IdUtil.simpleUUID(),null, faQiRen
            );
            bpmnProcessInstanceId = bpmnProcessInstance.getId();
            bpmnProcessService.updateInstanceBusinessStatus(TENANT, bpmnProcessInstanceId, yiFaQiState);
        } else {
            BpmnProcessInstance bpmnProcessInstance = bpmnProcessService.startInstanceByKey(TENANT, processDefinitionKey,
                    IdUtil.simpleUUID(), Map.of(GLOBAL_PROCESS_STATE_KEY, yiFaQiState), faQiRen
            );
            bpmnProcessInstanceId = bpmnProcessInstance.getId();
        }
        // ---------- step3. （流程自动流转，）查看任务 ----------
        List<? extends BpmnTask> list = bpmnTaskService.list(TENANT,
                BpmnTaskQuery.builder().processInstanceId(bpmnProcessInstanceId).build());
        
        if (StringUtils.isBlank(zs)) {
            return;
        }
        BpmnTask bpmnTask = list.get(0);
        if (!zs.equals(bpmnTask.getAssignee())) {
            bpmnTaskService.claim(TENANT, bpmnTask.getId(), zs); // 先申领任务，再完成任务
            bpmnTaskService.setAssignee(TENANT, bpmnTask.getId(), zs);
        }
        
        if (recordProcessStateByBusinessState) {
            bpmnProcessService.updateInstanceBusinessStatus(TENANT, bpmnProcessInstanceId, yiShenQing);
            bpmnTaskService.complete(TENANT, bpmnTask.getId(), zs, null);
        } else {
            bpmnTaskService.complete(TENANT, bpmnTask.getId(), zs, Map.of(GLOBAL_PROCESS_STATE_KEY, yiShenQing));
        }
        
        if (StringUtils.isBlank(ls)) {
            return;
        }
    
        list = bpmnTaskService.list(TENANT,
                BpmnTaskQuery.builder().processInstanceId(bpmnProcessInstanceId).build());
        bpmnTask = list.get(0);
        if (!ls.equals(bpmnTask.getAssignee())) {
            bpmnTaskService.claim(TENANT, bpmnTask.getId(), ls); // 先申领任务，再完成任务
            bpmnTaskService.setAssignee(TENANT, bpmnTask.getId(), ls);
        }
        
        if (BooleanUtils.isTrue(lsPass)) {
            if (recordProcessStateByBusinessState) {
                bpmnProcessService.updateInstanceBusinessStatus(TENANT, bpmnProcessInstanceId, shenPiTongGuo);
                bpmnTaskService.complete(TENANT, bpmnTask.getId(), ls, null);
            } else {
                bpmnTaskService.complete(TENANT, bpmnTask.getId(), ls, Map.of(GLOBAL_PROCESS_STATE_KEY, shenPiTongGuo));
            }
        } else {
            if (recordProcessStateByBusinessState) {
                bpmnProcessService.updateInstanceBusinessStatus(TENANT, bpmnProcessInstanceId, shenPiBuTongGuo);
                bpmnTaskService.complete(TENANT, bpmnTask.getId(), ls, null);
            } else {
                bpmnTaskService.complete(TENANT, bpmnTask.getId(), ls, Map.of(GLOBAL_PROCESS_STATE_KEY, shenPiBuTongGuo));
            }
            // 如果流程图上，设置有 fuShenBuTongGuo 对应的结束事件的话，这里就不用主动删除（推荐此方式）
            bpmnProcessService.deleteInstanceIfExist(TENANT, bpmnProcessInstanceId, "流程实例的删除原因是因为审批不通过。");
        }
        if (StringUtils.isBlank(ww)) {
            return;
        }
    
    
        list = bpmnTaskService.list(TENANT,
                BpmnTaskQuery.builder().processInstanceId(bpmnProcessInstanceId).build());
        bpmnTask = list.get(0);
        
        if (!ww.equals(bpmnTask.getAssignee())) {
            bpmnTaskService.claim(TENANT, bpmnTask.getId(), ww);
            bpmnTaskService.setAssignee(TENANT, bpmnTask.getId(), ww);
        }
        if (BooleanUtils.isTrue(wwPass)) {
            if (recordProcessStateByBusinessState) {
                bpmnProcessService.updateInstanceBusinessStatus(TENANT, bpmnProcessInstanceId, fuShenTongGuo);
                bpmnTaskService.complete(TENANT, bpmnTask.getId(), ww, null);
            } else {
                bpmnTaskService.complete(TENANT, bpmnTask.getId(), ww, Map.of(GLOBAL_PROCESS_STATE_KEY, fuShenTongGuo));
            }
        } else {
            if (recordProcessStateByBusinessState) {
                bpmnProcessService.updateInstanceBusinessStatus(TENANT, bpmnProcessInstanceId, fuShenBuTongGuo);
                bpmnTaskService.complete(TENANT, bpmnTask.getId(), ww, null);
            } else {
                bpmnTaskService.complete(TENANT, bpmnTask.getId(), ww, Map.of(GLOBAL_PROCESS_STATE_KEY, fuShenBuTongGuo));
            }
            // 如果流程图上，设置有 fuShenBuTongGuo 对应的结束事件的话，这里就不用主动删除（推荐此方式）
            bpmnProcessService.deleteInstanceIfExist(TENANT, bpmnProcessInstanceId, "流程实例的删除原因是因为复审不通过。");
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
    
    private static String formatStr(Object obj) {
        return JSON.toJSONString(obj, JSONWriter.Feature.NotWriteDefaultValue, JSONWriter.Feature.PrettyFormat);
    }
}