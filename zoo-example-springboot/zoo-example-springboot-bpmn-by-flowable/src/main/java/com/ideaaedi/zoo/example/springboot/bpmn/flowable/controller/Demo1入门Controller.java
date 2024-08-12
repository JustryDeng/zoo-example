package com.ideaaedi.zoo.example.springboot.bpmn.flowable.controller;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.IdUtil;
import com.ideaaedi.zoo.diy.feature.bpmn.api.entity.dto.BpmnDeployCreate;
import com.ideaaedi.zoo.diy.feature.bpmn.api.entity.facade.BpmnDeployment;
import com.ideaaedi.zoo.diy.feature.bpmn.api.service.BpmnDeploymentService;
import com.ideaaedi.zoo.diy.feature.bpmn.api.service.BpmnProcessService;
import com.ideaaedi.zoo.diy.feature.bpmn.api.service.BpmnTaskService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * (non-javadoc)
 *
 * @author <font size = "20" color = "#3CAA3C"><a href="https://gitee.com/JustryDeng">JustryDeng</a></font> <img
 * src="https://gitee.com/JustryDeng/shared-files/raw/master/JustryDeng/avatar.jpg" />
 * @since 1.0.0
 */
@RestController
@RequestMapping("/demo-入门")
public class Demo1入门Controller {
    
    private static final String TENANT = "1,";
    
    @Resource
    private BpmnDeploymentService bpmnDeploymentService;
    
    @Resource
    private BpmnProcessService bpmnProcessService;
    
    @Resource
    private BpmnTaskService bpmnTaskService;
    
    /*
     * 第一步：部署流程图资源
     * <p>
     *  部署xxx.bpmn20.xml文件资源，会根据bpmn文件中流程定义信息：
     *  1. 记录对应的流程定义信息（ACT_RE_PROCDEF表）
     *  2. 记录本地的部署信息（ACT_RE_DEPLOYMENT表）
     *  3. 记录本次部署的资源文件信息（ACT_GE_BYTEARRAY表）
     * </p>
     */
    @RequestMapping("/step1")
    public Object deploy() {
        return bpmnDeploymentService.deploy(
                TENANT,
                BpmnDeployCreate.builder()
                        .name("员工请假流程")
                        .category("内部行政管理")
                        .resourceName("员工请假流程.bpmn20.xml")
                        .resourceBytes(ResourceUtil.readBytes("员工请假流程.bpmn20.xml"))
                        .build()
        );
    }
    
    /*
     * 第二步(可选)：查询已部署的流程定义信息，获取到流程定义的唯一key
     * <p>
     *  流程定义的key即对应，xxx.bpmn20.xml中间中process元素的id属性值，如果知道具体的值的话，可以跳过此步骤
     * </p>
     */
    @RequestMapping("/step2")
    public Object listProcessDefinition() {
        return bpmnProcessService.listDefinition(TENANT, null);
    }
    
    /**
     * 第三步：启动流程实例（以指定的流程定义为模板）
     *
     * @param processDefinitionKey 流程定义的key （从step2查询出来的 key）
     */
    @RequestMapping("/step3")
    public Object startProcessInstance(@RequestParam String processDefinitionKey) {
        ///// 假设我们要用第一个流程定义来启动流程实例
        ///String processDefinitionKey = bpmnProcessService.listDefinition(TENANT, null).get(0).getKey();
        
        // 自定义业务标识
        String businessKey = "业务标识_" + IdUtil.fastSimpleUUID();
        
        // 流程相关参数，以初始化流程数据（具体需要哪些，要看xxx.bpmn20.xml中怎么定义的了）
        Map<String, Object> variables = Map.of("day", 1);
        /*
         * 1. 流程实例还没执行完时，流程实例可以通过（ACT_RU_EXECUTION + ACT_RE_PROCDEF）查询出来
         * 2. 流程实例一旦执行完，运行时数据表act_ru中的相关数据就会被删除（，此时如果想查询数据，就需要去相关历史表act_hi中查询了）
         */
        return bpmnProcessService.startInstanceByKey(TENANT, processDefinitionKey, businessKey, variables);
    }
    
    /**
     * 第四步：流程实例启动后，工作流会自动流转。 如果流转到对应环节需要审批或者啥的，就会创建对应的任务，此时我们观察任 务情况即可。如果任务到张三那里了，那么需要张三上去处理完成以触发后续流转 <br /><br /> <hr
     * /> Flowable流程引擎在启动一个流程实例后，会根据流程定义文件（通常是BPMN 2.0格式的XML文件）中预先设定的流程逻辑进行流转。<b>以下是Flowable流程流转的基本工作原理</b>：
     * <ol>
     *     <li>启动流程实例: <br>
     *          当调用RuntimeService.startProcessInstanceByKey(String processDefinitionKey, Map<String, Object>
     *              variables)或类似方法时，会基于指定的流程定义键启动一个新的流程实例，并可传入变量初始化流程数据。</li>
     *     <li>执行流程节点: <br>
     *          流程从开始事件（Start Event）开始执行，然后按照顺序流（Sequence Flow）、并行网关（Parallel Gateway）、排他网关（Exclusive
     *          Gateway）、任务（Task）、服务任务（Service Task）、用户任务（User Task）等节点依次执行。每个节点的执行可能依赖于特定的条件、表达式或用户交互。</li>
     *     <li>路由决策: <br>
     *          在流程流转过程中，根据条件和分支规则，如排他网关的条件表达式，决定流程走向哪个后续节点。</li>
     *     <li>任务分配与完成: <br>
     *          对于用户任务，Flowable会创建任务实例，并根据配置可能将其分配给特定用户或角色。用户通过任务列表领取任务并完成，完成任务后，流程会继续流向下一个节点。</li>
     *     <li>自动流转: <br>
     *          对于服务任务和服务调用，Flowable可以自动执行绑定的Java Delegate或Expression，无需用户干预，完成相应的业务逻辑或系统集成。</li>
     *     <li>边界事件与异常处理: <br>
     *          如果在任务执行期间触发了边界事件（例如定时器边界事件）或发生异常，Flowable会按照预设的流程处理逻辑响应。</li>
     *     <li>结束流程: <br>
     *          当流程到达结束事件（End Event）时，整个流程实例结束，相关的流程实例和历史数据会被记录在数据库中。</li>
     * </ol>
     * 在整个流转过程中，Flowable引擎通过持久化机制确保流程状态的稳定性和可恢复性，即使在系统重启后也能从断点继续执行。开发者可以通过监听器（Listeners
     * ）和自定义行为（如自定义任务处理器）来扩展流程的行为和数据处理逻辑。此外，通过查询服务（Query Services），可以实时监控和检索流程实例的状态和历史数据。
     * <br /><br /><hr />
     * 在流程引擎中，<b>需要程序员手动处理的主要涉及以下几个方面：</b>
     * <ol>
     *     <li>流程定义的创建与部署：<br>
     *         包括设计工作流图，定义各个步骤（任务）、转换规则及条件，然后将这些定义打包部署到流程引擎中。</li>
     *
     *     <li>变量映射：<br>
     *         配置流程实例运行时数据的传递，确保表单数据、业务对象等能正确绑定到流程变量中，实现数据交互。</li>
     *
     *     <li>任务监听器和委托：<br>
     *         设置监听器来响应特定任务事件（如任务创建、完成），以及配置任务的委托机制，允许用户将任务转交给其他参与者处理。</li>
     *
     *     <li>任务分配策略：<br>
     *         定义如何将任务分派给用户或用户组，可以基于角色、轮询、候选用户列表等多种策略自动或手动分配。</li>
     *
     *     <li>边界事件与异常处理：<br>
     *         为流程定义边界事件来捕获并处理外部信号或超时情况，以及配置异常处理逻辑，保证流程的稳定性和健壮性。</li>
     *
     *     <li>查询与监听配置：<br>
     *         实现对流程实例、任务状态的实时查询功能，并配置事件监听，以便系统能够根据流程进展触发相应操作或通知。</li>
     *
     *     <li>事务管理与错误处理：<br>
     *         确保流程中的操作具有原子性，即便某一步骤失败也能回滚，同时配置错误处理逻辑，妥善管理流程执行中可能出现的各类错误。</li>
     * </ol>
     */
    @RequestMapping("/step4")
    public Object listTasks() {
        return bpmnTaskService.list(TENANT, null);
    }
    
    /**
     * 测试完毕后，清除所有部署（并级联清除相关数据）等等
     */
    @RequestMapping("/clear-all")
    public Object clearAll() {
        List<? extends BpmnDeployment> list = bpmnDeploymentService.list(TENANT, null);
        for (BpmnDeployment bpmnDeployment : list) {
            // true表示级联删除相关的：资源、流程定义、流程实例、任务、历史数据 等等
            bpmnDeploymentService.undeploy(TENANT, bpmnDeployment.getId(), true);
        }
        return list;
    }
}