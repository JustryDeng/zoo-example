package com.ideaaedi.zoo.example.springboot.liteflow.basic;

import cn.hutool.core.util.RandomUtil;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.annotation.LiteflowMethod;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.enums.LiteFlowMethodEnum;
import com.yomahub.liteflow.enums.NodeTypeEnum;
import lombok.Data;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * 定义node
 *
 * @author <font size = "20" color = "#3CAA3C"><a href="https://gitee.com/JustryDeng">JustryDeng</a></font> <img
 * src="https://gitee.com/JustryDeng/shared-files/raw/master/JustryDeng/avatar.jpg" />
 * @since 1.0.0
 */
@LiteflowComponent
public class XyzDemo {
    
    @Data
    public static class XyzContext {
        private List<Integer> stepList = new CopyOnWriteArrayList<>();
        private List<String> stepInfoList = new CopyOnWriteArrayList<>();
        private String aData;
        private String bData;
        private String cData;
        private String dData;
        
        /** 触发异常试试 */
        private boolean testException;
    }
    
    @LiteflowMethod(value = LiteFlowMethodEnum.PROCESS, nodeId = "A", nodeName = "业务A", nodeType = NodeTypeEnum.COMMON)
    public void processA(NodeComponent bindCmp) {
        XyzContext context = bindCmp.getContextBean(XyzContext.class);
    
        List<Integer> stepList = context.getStepList();
        Integer preStep = stepList.stream().max(Integer::compare).orElse(0);
        int currStep = preStep + 1;
        stepList.add(currStep);
        context.setAData(String.format("步骤%s：我是A的数据：A_%s,", currStep, RandomUtil.randomString(3)));
        context.getStepInfoList().add(context.getAData());
    }
    
    @LiteflowMethod(value = LiteFlowMethodEnum.PROCESS, nodeId = "B", nodeName = "业务B", nodeType = NodeTypeEnum.COMMON)
    public void processB(NodeComponent bindCmp) {
        XyzContext context = bindCmp.getContextBean(XyzContext.class);
        List<Integer> stepList = context.getStepList();
        Integer preStep = stepList.stream().max(Integer::compare).orElse(0);
        int currStep = preStep + 1;
        // 睡眠几秒，使并发执行B和C时，拿到的currStep值都一样，方便更直观的通过日志看出来是并发执行的
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        stepList.add(currStep);
        context.setBData(String.format("步骤%s：我是B的数据：B_%s,", currStep, RandomUtil.randomString(3)));
        context.getStepInfoList().add(context.getBData());
    }
    
    @LiteflowMethod(value = LiteFlowMethodEnum.PROCESS, nodeId = "C", nodeName = "业务C", nodeType = NodeTypeEnum.COMMON)
    public void processC(NodeComponent bindCmp) {
        XyzContext context = bindCmp.getContextBean(XyzContext.class);
        List<Integer> stepList = context.getStepList();
        Integer preStep = stepList.stream().max(Integer::compare).orElse(0);
        int currStep = preStep + 1;
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // 触发异常看看
        if (context.isTestException()) {
            System.err.println(1/0);
        }
        stepList.add(currStep);
        context.setCData(String.format("步骤%s：我是C的数据：C_%s,", currStep, RandomUtil.randomString(3)));
        context.getStepInfoList().add(context.getCData());
    }
    
    @LiteflowMethod(value = LiteFlowMethodEnum.PROCESS, nodeId = "D", nodeName = "业务D", nodeType = NodeTypeEnum.COMMON)
    public void processD(NodeComponent bindCmp) {
        XyzContext context = bindCmp.getContextBean(XyzContext.class);
        List<Integer> stepList = context.getStepList();
        Integer preStep = stepList.stream().max(Integer::compare).orElse(0);
        int currStep = preStep + 1;
        stepList.add(currStep);
        context.setDData(String.format("步骤%s：我是D的数据：D_%s,", currStep, RandomUtil.randomString(3)));
        context.getStepInfoList().add(context.getDData());
    }
}
