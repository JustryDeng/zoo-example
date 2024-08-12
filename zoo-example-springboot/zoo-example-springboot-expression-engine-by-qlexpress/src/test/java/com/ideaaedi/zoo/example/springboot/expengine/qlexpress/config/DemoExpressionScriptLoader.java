package com.ideaaedi.zoo.example.springboot.expengine.qlexpress.config;

import com.google.common.collect.Lists;
import com.ideaaedi.zoo.diy.feature.expression.engine.api.annotation.ExpressionReplacer;
import com.ideaaedi.zoo.diy.feature.expression.engine.api.entity.ExpressionScriptInfoDTO;
import com.ideaaedi.zoo.diy.feature.expression.engine.api.service.ExpressionExecutor;
import com.ideaaedi.zoo.diy.feature.expression.engine.api.spi.ExpressionScriptLoader;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 如果你不需要用到{@link ExpressionReplacer}注解的话，你可以关闭 zoo.expression-engine.exp-replacer-enable=false 它，
 * 这样就不需要实现{@link ExpressionScriptLoader}了
 */
@SuppressWarnings("all")
@Component
public class DemoExpressionScriptLoader implements ExpressionScriptLoader {
    
    public static final String BizKeyDemo = "qwer5";
    
    public static final String qwer6 = "qwer6";
    
    private static final Map<String, List<ExpressionScriptInfoDTO>> mockDbOrConfig = new HashMap<>(4);
    
    static {
        mockDbOrConfig.put(BizKeyDemo,
                Lists.newArrayList(
                        ExpressionScriptInfoDTO.builder()
                                //  只有当用户名是张三时，才命中此表达式
                                .supportScript(
                                        """
                                        "张三".equals(user.getName())
                                        """
                                )
                                // 命中后，执行的表达式脚本（此脚本会替换）
                                .expressionScript(
                                        """
                                        import com.alibaba.fastjson2.JSON;
                                        
                                        return "\\n\\t" + JSON.toJSONString(user) + "\\n\\t" + JSON.toJSONString(worker);"""
                                )
                                .build()
                )
        );
        
        
        mockDbOrConfig.put(qwer6,
                Lists.newArrayList(
                        ExpressionScriptInfoDTO.builder()
                                //  只有当用户名是王五、工人是田七时，才命中此表达式
                                .supportScript(
                                        """
                                        return "王五".equals(user.getName()) && "田七".equals(worker.getName());
                                        """
                                )
                                // 命中后，执行的表达式脚本（此脚本会替换）
                                .expressionScript(
                                        """
                                        import com.google.common.collect.Lists;
                                        import com.ideaaedi.zoo.example.springboot.expengine.qlexpress.entity.TmpPageInfo;
                                        import com.ideaaedi.zoo.example.springboot.expengine.qlexpress.entity.TmpResult;
                                        import com.ideaaedi.zoo.example.springboot.expengine.qlexpress.entity.User;
                                        
                                        u = new User();
                                        u.setName(worker.getName());
                                        u.setAge(worker.getAge());
                                        return TmpResult.success(TmpPageInfo.of(2, 1, 10, Lists.newArrayList(user, u)));
                                        """
                                )
                                .build()
                )
        );
        
        
    }
    
    
    @Resource
    private ExpressionExecutor  expressionExecutor;
    
    @PostConstruct
    public void check() {
        // 在往存储表达式前，最好先简单检查以下表达式语法是否正确
        mockDbOrConfig.values().forEach(list -> {
            list.forEach(info -> {
                boolean pass = expressionExecutor.checkSyntax(info.getSupportScript());
                if (!pass) {
                    throw new IllegalArgumentException("表达式脚本语法错误：" + info.getSupportScript());
                }
                pass = expressionExecutor.checkSyntax(info.getExpressionScript());
                if (!pass) {
                    throw new IllegalArgumentException("表达式脚本语法错误：" + info.getExpressionScript());
                }
            });
        });
    }
    
    @Nullable
    @Override
    public List<ExpressionScriptInfoDTO> load(@Nonnull String bizKey) {
        return mockDbOrConfig.get(bizKey);
    }
}
