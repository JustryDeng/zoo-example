package com.ideaaedi.zoo.example.springboot.liteflow;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ideaaedi.commonds.exception.ExceptionUtil;
import com.ideaaedi.zoo.diy.artifact.liteflow.parser.ScriptExtValidator;
import com.ideaaedi.zoo.example.springboot.liteflow.basic.XyzDemo;
import com.ideaaedi.zoo.example.springboot.liteflow.dynamic.po.LiteflowChainPO;
import com.ideaaedi.zoo.example.springboot.liteflow.dynamic.po.LiteflowScriptPO;
import com.ideaaedi.zoo.example.springboot.liteflow.dynamic.service.LiteflowChainService;
import com.ideaaedi.zoo.example.springboot.liteflow.dynamic.service.LiteflowScriptService;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.enums.ScriptTypeEnum;
import com.yomahub.liteflow.flow.FlowBus;
import com.yomahub.liteflow.flow.LiteflowResponse;
import com.yomahub.liteflow.flow.element.Chain;
import com.yomahub.liteflow.flow.entity.CmpStep;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest(classes = ExampleSpringbootLiteflowApplication.class)
public class XyzDemoTest {
    
    @Value("${spring.application.name}")
    private String applicationName;
    
    @Value("${liteflow.rule-source-ext-data-map.pollingIntervalSeconds}")
    private Integer pollingIntervalSeconds;
    
    @Value("${liteflow.print-execution-log}")
    private Boolean aaa;
    
    @Resource
    private LiteflowChainService liteflowChainService;
    
    @Resource
    private LiteflowScriptService liteflowScriptService;
    
    @Resource
    private FlowExecutor flowExecutor;
    
    /**
     * 测试【src/test/java/com/ideaaedi/zoo/example/springboot/liteflow/basic/测试说明.md】中的chainX 流程
     */
    @Test
    @SneakyThrows
    void testChainX() {
        String traceId = UUID.randomUUID().toString().replace("-", "");
        MDC.put("traceId", traceId);
        
        // chainX
        String chainName = prepareChain("chainX", "SER(A, B, C, D)");
        // 上下文初始化数据
        ///XyzDemo.XyzContext context = new XyzDemo.XyzContext();
        ///context.setTestException(true);
        ///LiteflowResponse liteflowResponse = flowExecutor.execute2RespWithRid(chainName, null, traceId, context);
        // 如果不需要传初始化数据的话，那可以这样
        LiteflowResponse liteflowResponse = flowExecutor.execute2RespWithRid(chainName, null, traceId, XyzDemo.XyzContext.class);
        XyzDemo.XyzContext contextBean = liteflowResponse.getContextBean(XyzDemo.XyzContext.class);
        String executeStepStrWithTime = liteflowResponse.getExecuteStepStrWithTime();
        Chain chain = FlowBus.getChainMap().get(chainName);
        log.info(String.format("chain -> %s=%s, execute-step-info -> %s", chain.getChainId(), chain.getEl(), executeStepStrWithTime));
        boolean success = liteflowResponse.isSuccess();
        if (success) {
            List<String> stepInfoList = contextBean.getStepInfoList();
            for (String stepInfo : stepInfoList) {
                System.out.println("chainX步骤信息： " + stepInfo);
            }
        } else {
            CmpStep occurExceptionStep = new ArrayDeque<>(liteflowResponse.getExecuteStepQueue()).peekLast();
            String nodeInfo = occurExceptionStep == null ? "" : (occurExceptionStep.getNodeId() + "=" + occurExceptionStep.getNodeName());
            Exception cause = liteflowResponse.getCause();
            System.err.printf("chanX节点（%s）异常啦! 上下文 -> %s。 异常信息 -> %s", nodeInfo, contextBean, ExceptionUtil.getStackTraceMessage(cause));
        }
    
    
        // chainY
        chainName = prepareChain("chainY", "SER(A, PAR(B, C), D)");
        chain = FlowBus.getChainMap().get(chainName);
        System.err.printf("当前执行chain的el是%s=%s\n", chain.getChainId(), chain.getEl());
        
        liteflowResponse = flowExecutor.execute2RespWithRid(chainName, null, traceId, XyzDemo.XyzContext.class);
        executeStepStrWithTime = liteflowResponse.getExecuteStepStrWithTime();
        chain = FlowBus.getChainMap().get(chainName);
        log.info(String.format("chain -> %s=%s, execute-step-info -> %s", chain.getChainId(), chain.getEl(), executeStepStrWithTime));
        contextBean = liteflowResponse.getContextBean(XyzDemo.XyzContext.class);
        success = liteflowResponse.isSuccess();
        if (success) {
            List<String> stepInfoList = contextBean.getStepInfoList();
            for (String stepInfo : stepInfoList) {
                System.out.println("chainY步骤信息： " + stepInfo);
            }
        } else {
            CmpStep occurExceptionStep = new ArrayDeque<>(liteflowResponse.getExecuteStepQueue()).peekLast();
            String nodeInfo = occurExceptionStep == null ? "" : (occurExceptionStep.getNodeId() + "=" + occurExceptionStep.getNodeName());
            Exception cause = liteflowResponse.getCause();
            System.err.printf("chanY节点（%s）异常啦! 上下文 -> %s。 异常信息 -> %s", nodeInfo, contextBean, ExceptionUtil.getStackTraceMessage(cause));
        }
        
        // chainZ
        prepareJavaBooleanScript("ifk", """
                import com.yomahub.liteflow.script.ScriptExecuteWrap;
                import com.yomahub.liteflow.script.body.JaninoBooleanScriptBody;
                import com.ideaaedi.zoo.example.springboot.liteflow.basic.XyzDemo;
                import org.slf4j.Logger;
                import org.slf4j.LoggerFactory;
                
                import java.util.Random;
                
                public class Demo implements JaninoBooleanScriptBody {
                   
                    private static final Logger log = LoggerFactory.getLogger(Demo.class);
                   
                    @Override
                    public Boolean body(ScriptExecuteWrap wrap) {
                        XyzDemo.XyzContext contextBean = (XyzDemo.XyzContext)wrap.getCmp().getContextBean(XyzDemo.XyzContext.class);
                        boolean bl = new Random().nextBoolean();
                        log.warn("bl -> {}, contextBean -> {}", bl, contextBean);
                        return bl;
                    }
                }
                """);
        chainName = prepareChain("chainZ", "SER(A, IF(ifk, SER(B, C)), D)");
        chain = FlowBus.getChainMap().get(chainName);
        System.err.printf("当前执行chain的el是%s=%s\n", chain.getChainId(), chain.getEl());
    
        liteflowResponse = flowExecutor.execute2RespWithRid(chainName, null, traceId, XyzDemo.XyzContext.class);
        executeStepStrWithTime = liteflowResponse.getExecuteStepStrWithTime();
        chain = FlowBus.getChainMap().get(chainName);
        log.info(String.format("chain -> %s=%s, execute-step-info -> %s", chain.getChainId(), chain.getEl(), executeStepStrWithTime));
        contextBean = liteflowResponse.getContextBean(XyzDemo.XyzContext.class);
        success = liteflowResponse.isSuccess();
        if (success) {
            List<String> stepInfoList = contextBean.getStepInfoList();
            for (String stepInfo : stepInfoList) {
                System.out.println("chainZ步骤信息： " + stepInfo);
            }
        } else {
            CmpStep occurExceptionStep = new ArrayDeque<>(liteflowResponse.getExecuteStepQueue()).peekLast();
            String nodeInfo = occurExceptionStep == null ? "" : (occurExceptionStep.getNodeId() + "=" + occurExceptionStep.getNodeName());
            Exception cause = liteflowResponse.getCause();
            System.err.printf("chanZ节点（%s）异常啦! 上下文 -> %s。 异常信息 -> %s", nodeInfo, contextBean, ExceptionUtil.getStackTraceMessage(cause));
        }
        
        // chainM
        prepareJavaSwitchScript("switchJ", """
                import com.yomahub.liteflow.script.ScriptExecuteWrap;
                import com.yomahub.liteflow.script.body.JaninoSwitchScriptBody;
                import com.ideaaedi.zoo.example.springboot.liteflow.basic.XyzDemo;
                import org.slf4j.Logger;
                import org.slf4j.LoggerFactory;
                
                import java.util.Random;
                
                public class Demo implements JaninoSwitchScriptBody {
                   
                    private static final Logger log = LoggerFactory.getLogger(Demo.class);
                   
                    @Override
                    public String body(ScriptExecuteWrap wrap) {
                        XyzDemo.XyzContext contextBean = (XyzDemo.XyzContext)wrap.getCmp().getContextBean(XyzDemo.XyzContext.class);
                        int ir = new Random().nextInt(100);
                        String switchNodeId;
                        if (ir > 50) {
                           switchNodeId = "A";
                        } else if (ir > 20) {
                           switchNodeId = "B";
                        } else {
                           switchNodeId = "C";
                        }
                        log.warn("ir -> {}, switchNodeId -> {}, contextBean -> {}", ir, switchNodeId, contextBean);
                        return switchNodeId;
                    }
                }
                """);
        
        chainName = prepareChain("chainM", "SER(SWITCH(switchJ).to(A, B, C), D)");
        chain = FlowBus.getChainMap().get(chainName);
        System.err.printf("当前执行chain的el是%s=%s\n", chain.getChainId(), chain.getEl());
    
        liteflowResponse = flowExecutor.execute2RespWithRid(chainName, null, traceId, XyzDemo.XyzContext.class);
        executeStepStrWithTime = liteflowResponse.getExecuteStepStrWithTime();
        chain = FlowBus.getChainMap().get(chainName);
        log.info(String.format("chain -> %s=%s, execute-step-info -> %s", chain.getChainId(), chain.getEl(), executeStepStrWithTime));
        contextBean = liteflowResponse.getContextBean(XyzDemo.XyzContext.class);
        success = liteflowResponse.isSuccess();
        if (success) {
            List<String> stepInfoList = contextBean.getStepInfoList();
            for (String stepInfo : stepInfoList) {
                System.out.println("chainM步骤信息： " + stepInfo);
            }
        } else {
            CmpStep occurExceptionStep = new ArrayDeque<>(liteflowResponse.getExecuteStepQueue()).peekLast();
            String nodeInfo = occurExceptionStep == null ? "" : (occurExceptionStep.getNodeId() + "=" + occurExceptionStep.getNodeName());
            Exception cause = liteflowResponse.getCause();
            System.err.printf("chanM节点（%s）异常啦! 上下文 -> %s。 异常信息 -> %s", nodeInfo, contextBean, ExceptionUtil.getStackTraceMessage(cause));
        }
    }
    
    void prepareJavaBooleanScript(String nodeId, String scriptData) {
        // 校验脚本语法
        Exception exception = ScriptExtValidator.validateScript(scriptData, ScriptTypeEnum.JAVA);
        if (exception != null) {
            throw new RuntimeException(exception);
        }
        LiteflowScriptPO liteflowScript = new LiteflowScriptPO();
        liteflowScript.setApplicationName(applicationName);
        liteflowScript.setScriptId(nodeId);
        liteflowScript.setScriptName("条件k");
        liteflowScript.setScriptLanguage("java");
        liteflowScript.setScriptType("boolean_script");
        liteflowScript.setScriptData(scriptData);
        liteflowScript.setEnable(1);
        liteflowScript.setDeleted(0);
        // 以下字段对于liteflowChain来说不是必须的，只是我们的表结构设计上不允许为空，不赋值会报错，所以这里也赋值
        LocalDateTime now = LocalDateTime.now();
        liteflowScript.setCreatedBy(1L);
        liteflowScript.setCreatedAt(now);
        liteflowScript.setUpdatedBy(1L);
        liteflowScript.setUpdatedAt(now);
        
        // 先移除旧的同名的Chain
        liteflowScriptService.remove(Wrappers.lambdaUpdate(LiteflowScriptPO.class)
                .eq(LiteflowScriptPO::getScriptId, liteflowScript.getScriptId())
        );
        liteflowScriptService.save(liteflowScript);
    
        // 等一会儿，等 数据库中的规则生效
        try {
            TimeUnit.SECONDS.sleep(pollingIntervalSeconds + 3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    
    void prepareJavaSwitchScript(String nodeId, String scriptData) {
        // 校验脚本语法
        Exception exception = ScriptExtValidator.validateScript(scriptData, ScriptTypeEnum.JAVA);
        if (exception != null) {
            throw new RuntimeException(exception);
        }
        LiteflowScriptPO liteflowScript = new LiteflowScriptPO();
        liteflowScript.setApplicationName(applicationName);
        liteflowScript.setScriptId(nodeId);
        liteflowScript.setScriptName("switchJ");
        liteflowScript.setScriptLanguage("java");
        liteflowScript.setScriptType("switch_script");
        liteflowScript.setScriptData(scriptData);
        liteflowScript.setEnable(1);
        liteflowScript.setDeleted(0);
        // 以下字段对于liteflowChain来说不是必须的，只是我们的表结构设计上不允许为空，不赋值会报错，所以这里也赋值
        LocalDateTime now = LocalDateTime.now();
        liteflowScript.setCreatedBy(1L);
        liteflowScript.setCreatedAt(now);
        liteflowScript.setUpdatedBy(1L);
        liteflowScript.setUpdatedAt(now);
        
        // 先移除旧的同名的Chain
        liteflowScriptService.remove(Wrappers.lambdaUpdate(LiteflowScriptPO.class)
                .eq(LiteflowScriptPO::getScriptId, liteflowScript.getScriptId())
        );
        liteflowScriptService.save(liteflowScript);
    
        // 等一会儿，等 数据库中的规则生效
        try {
            TimeUnit.SECONDS.sleep(pollingIntervalSeconds + 3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    
    String prepareChain(String chainName, String elData) {
        LiteflowChainPO liteflowChain = new LiteflowChainPO();
        liteflowChain.setApplicationName(applicationName);
        liteflowChain.setChainName(chainName);
        liteflowChain.setChainDesc("流程规则X");
        liteflowChain.setElData(elData);
        liteflowChain.setEnable(1);
        liteflowChain.setDeleted(0);
        // 以下字段对于liteflowChain来说不是必须的，只是我们的表结构设计上不允许为空，不赋值会报错，所以这里也赋值
        LocalDateTime now = LocalDateTime.now();
        liteflowChain.setCreatedBy(1L);
        liteflowChain.setCreatedAt(now);
        liteflowChain.setUpdatedBy(1L);
        liteflowChain.setUpdatedAt(now);
        
        // 先移除旧的同名的Chain
        liteflowChainService.remove(Wrappers.lambdaUpdate(LiteflowChainPO.class)
                .eq(LiteflowChainPO::getChainName, liteflowChain.getChainName())
        );
        liteflowChainService.save(liteflowChain);
    
        // 等一会儿，等 数据库中的规则生效
        try {
            TimeUnit.SECONDS.sleep(pollingIntervalSeconds + 3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return liteflowChain.getChainName();
    }
    
}