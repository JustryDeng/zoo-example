package com.ideaaedi.zoo.example.springboot.expengine.qlexpress;

import com.ideaaedi.commonds.exception.ExceptionUtil;
import com.ideaaedi.zoo.diy.feature.expression.engine.api.exception.ExpressionExecException;
import com.ideaaedi.zoo.diy.feature.expression.engine.api.service.ExpressionExecutor;
import com.ideaaedi.zoo.example.springboot.expengine.qlexpress.DemoXyzBean.Worker;
import com.ql.util.express.exception.QLException;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Sets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 表达式引擎执行测试
 */
@Slf4j
@SpringBootTest(classes = ExampleQLExpressApplication.class)
public class ExpressEngineTest {
    
    @Resource
    private ExpressionExecutor expressionExecutor;
    
    @Resource
    private DemoXyzBean demoXyzBean;
    
    /**
     * 基础运算
     */
    @Test
    public void test1() {
        Integer result = expressionExecutor.execute("1 + 1");
        System.err.println(result);
        Assertions.assertEquals(result, 2);
        System.out.println("更多语法详见 https://github.com/alibaba/QLExpress?tab=readme-ov-file#%E4%B8%89%E8%AF%AD%E6%B3%95%E4%BB%8B%E7%BB%8D");
    }
    
    /**
     * 调用字符串拼接：String#concat、 +
     */
    @Test
    public void test2() {
        Map<String, Object> context = Map.of(
                "name", "张三",
                "gender", "男",
                "hobby", "玩游戏"
        );
        String expressScript = "name.concat(\"（\").concat(gender).concat(\"）的爱好是\").concat(hobby)";
        String result = expressionExecutor.execute(expressScript, context);
        System.err.println(result);
        Assertions.assertEquals(result, "张三（男）的爱好是玩游戏");
        
        
        expressScript = "name + '（' + gender + '）的爱好是' + hobby";
        result = expressionExecutor.execute(expressScript, context);
        System.err.println(result);
        Assertions.assertEquals(result, "张三（男）的爱好是玩游戏");
        System.out.println("更多语法详见 https://github.com/alibaba/QLExpress?tab=readme-ov-file#%E4%B8%89%E8%AF%AD%E6%B3%95%E4%BB%8B%E7%BB%8D");
    }
    
    /**
     * 调用spring bean方法  -  无参
     */
    @Test
    public void test3() {
        String expressScript = String.format("%s.qwer1()", DemoXyzBean.XYZ_NAME);
        String result = expressionExecutor.execute(expressScript);
        System.err.println(result);
        Assertions.assertEquals(result == null ? 0 : result.length(), 3);
        System.out.println("更多语法详见 https://github.com/alibaba/QLExpress?tab=readme-ov-file#%E4%B8%89%E8%AF%AD%E6%B3%95%E4%BB%8B%E7%BB%8D");
    }
    
    /**
     * 调用spring bean方法  -  简单参数
     */
    @Test
    public void test4() {
        String expressScript = String.format(
                """
                a = "张三";
                b = 18;
                return %s.qwer2(a, b);
                """,
                DemoXyzBean.XYZ_NAME
        );
        String result = expressionExecutor.execute(expressScript);
        System.err.println(result);
        Assertions.assertEquals(result == null ? 0 : result.length(), 4);
        System.out.println("更多语法详见 https://github.com/alibaba/QLExpress?tab=readme-ov-file#%E4%B8%89%E8%AF%AD%E6%B3%95%E4%BB%8B%E7%BB%8D");
    }
    
    /**
     * 调用spring bean方法  -  复杂参数
     */
    @Test
    public void test5() {
        String expressScript =
                """
                        import com.ideaaedi.zoo.example.springboot.expengine.qlexpress.entity.User;
                        
                        /** 3.3.3版本，我发现将注释放在第一行，会导致报错：不满足语法规范 **/;
                        /** 系统自动会import java.lang.*,import java.util.*; **/;
                        
                        /**
                         1.支持 +,-,*,/,<,>,<=,>=,==,!=,<>【等同于!=】,%,mod【取模等同于%】,++,--,
                         2.in【类似sql】,like【sql语法】,&&,||,!,等操作符
                         3.支持for，break、continue、if then else 等标准的程序控制逻辑
                         4.不支持try{}catch{}
                         5.注释目前只支持 【斜杠** **斜杠】 （因为不能嵌套，这里就用'斜杠'代替'/'），不支持单行注释 //
                         6.不支持java8的lambda表达式
                         7.不支持for循环集合操作for (Item item : list)
                         8.弱类型语言，请不要定义类型声明,更不要用Template（Map<String, List>之类的）
                         9.array的声明不一样
                         10.min,max,round,print,println,like,in 都是系统默认函数的关键字，请不要作为变量名
                         **/
                        
                        
                        user = new User();
                        user.setName("张三");
                        user.setAge(18);
                        user.setHobby("游泳");
                        return demoXyzBean.qwer3(user);
                        """;
        String result = expressionExecutor.execute(expressScript);
        System.err.println(result);
        Assertions.assertTrue((result == null ? 0 : result.length()) > 4);
        System.out.println("更多语法详见 https://github.com/alibaba/QLExpress?tab=readme-ov-file#%E4%B8%89%E8%AF%AD%E6%B3%95%E4%BB%8B%E7%BB%8D");
    }
    
    /**
     * 调用静态字段的方法（可理解为：伪静态方法）
     */
    @Test
    @SneakyThrows
    public void test7() {
        String expressScript =
                        """
                        System.out.println("海公牛");
                        """;
        String result = expressionExecutor.execute(expressScript);
        System.err.println(result);
        
        TimeUnit.MILLISECONDS.sleep(100);
        System.out.println("更多语法详见 https://github.com/alibaba/QLExpress?tab=readme-ov-file#%E4%B8%89%E8%AF%AD%E6%B3%95%E4%BB%8B%E7%BB%8D");
    }
    
    /**
     * 调用静态方法
     */
    @Test
    @SneakyThrows
    public void test8() {
        String expressScript =
                """
                import cn.hutool.core.util.RandomUtil;
                radStr = RandomUtil.randomString(10);
                return radStr;
                """;
        String result = expressionExecutor.execute(expressScript);
        System.err.println(result);
        Assertions.assertEquals(10, result == null ? 0 : result.length());
        
        TimeUnit.MILLISECONDS.sleep(100);
        System.out.println("更多语法详见 https://github.com/alibaba/QLExpress?tab=readme-ov-file#%E4%B8%89%E8%AF%AD%E6%B3%95%E4%BB%8B%E7%BB%8D");
    }
    
    /**
     * 调用静态方法 - 无法调用内部类的静态方法
     * <pre>
     * 因为【InnerClass.fa()】这样的写法，没法获取到InnerClass，会报InnerClass不能为null）;
     * 而【OutClass.InnerClass.fa()】这样的写法，在QLExpress解析时，内部类InnerClass会被当成外部类OutClass的字段）。
     * 此时，可以考虑使用{@link ExpressionExecutor#addGlobalFunction(String, Class, String, Class[])}来变相解决此问题
     * </pre>
     */
    @Test
    @SneakyThrows
    public void test9() {
        String expressScript =
                        """
                        import com.ideaaedi.zoo.example.springboot.expengine.qlexpress.DemoXyzBean.Worker;
                        
                        worker = Worker.of("张三", 18);
                        return demoXyzBean.qwer4(worker);
                        """;
        String result = null;
        QLException qlException = null;
        try {
            result = expressionExecutor.execute(expressScript);
        } catch (ExpressionExecException e) {
            qlException = ExceptionUtil.extractThrowable(e, QLException.class, 5);
        }
        Assertions.assertNull(result);
        Assertions.assertNotNull(qlException);
        System.err.println(ExceptionUtil.getStackTraceMessage(qlException));
        
        
        expressScript =
                        """
                        import com.ideaaedi.zoo.example.springboot.expengine.qlexpress.DemoXyzBean;
                        
                        worker = DemoXyzBean.Worker.of("张三", 18);
                        return demoXyzBean.qwer4(worker);
                        """;
        NoSuchFieldException noSuchFieldException = null;
        try {
            result = expressionExecutor.execute(expressScript);
        } catch (ExpressionExecException e) {
            noSuchFieldException = ExceptionUtil.extractThrowable(e, NoSuchFieldException.class, 5);
        }
        Assertions.assertNull(result);
        Assertions.assertNotNull(noSuchFieldException);
        System.err.println(ExceptionUtil.getStackTraceMessage(noSuchFieldException));
        
        
        TimeUnit.MILLISECONDS.sleep(100);
        System.out.println("更多语法详见 https://github.com/alibaba/QLExpress?tab=readme-ov-file#%E4%B8%89%E8%AF%AD%E6%B3%95%E4%BB%8B%E7%BB%8D");
    }
    
    /**
     * 简单校验表达式语法
     */
    @Test
    @SneakyThrows
    public void test10() {
        String expressScript =
                """
                import cn.hutool.core.util.RandomUtil;
                radStr = RandomUtil.randomString(10);
                return radStr;
                """;
        boolean pass = expressionExecutor.checkSyntax(expressScript);
        Assertions.assertTrue(pass);
        
        expressScript =
                """
                import cn.hutool.core.util.RandomUtil;
                radStr = RandomUtil.randomString(10);bu_xiao_xin_en_zhe_le
                return radStr;
                """;
        pass = expressionExecutor.checkSyntax(expressScript);
        Assertions.assertFalse(pass);
        
        TimeUnit.MILLISECONDS.sleep(100);
        System.out.println("更多语法详见 https://github.com/alibaba/QLExpress?tab=readme-ov-file#%E4%B8%89%E8%AF%AD%E6%B3%95%E4%BB%8B%E7%BB%8D");
    }
    
    /**
     * 解析表达式，获取需要的外部变量
     */
    @Test
    @SneakyThrows
    public void test11() {
        String expressScript =
                """
                return (a + b) * c / d;
                """;
        Set<String> outVarNames = expressionExecutor.getOutVarNames(expressScript);
        System.err.println(outVarNames);
        Assertions.assertEquals(outVarNames, Sets.newHashSet("a", "b", "c", "d"));
        
        TimeUnit.MILLISECONDS.sleep(100);
        System.out.println("更多语法详见 https://github.com/alibaba/QLExpress?tab=readme-ov-file#%E4%B8%89%E8%AF%AD%E6%B3%95%E4%BB%8B%E7%BB%8D");
    }
    
    /**
     * 解析表达式，获取需要的外部函数
     */
    @Test
    @SneakyThrows
    public void test12() {
        String expressScript =
                """
                /** 脚本中定义function **/
                function jia(int a, int b){
                    return a + b;
                };
                
                function jian(int a, int b){
                    return a - b;
                };
                
                return jia(1 + 2) + jian(1, 2) + cheng(1, 2) + chu(1, 2);
                """;
        Set<String> outFunctionNames = expressionExecutor.getOutFunctionNames(expressScript);
        System.err.println(outFunctionNames);
        Assertions.assertEquals(outFunctionNames, Sets.newHashSet("cheng", "chu"));
        
        TimeUnit.MILLISECONDS.sleep(100);
        System.out.println("更多语法详见 https://github.com/alibaba/QLExpress?tab=readme-ov-file#%E4%B8%89%E8%AF%AD%E6%B3%95%E4%BB%8B%E7%BB%8D");
    }
    
    /**
     * 测试添加函数并使用 - 改函数的实现逻辑是指定的静态方法
     */
    @Test
    public void test13() {
        String expressScript =
                """
                worker = createWorkerByOf("张三", 18); /** 这里使用我们添加的函数 **/
                return demoXyzBean.qwer4(worker);
                """;
        
        expressionExecutor.addGlobalFunction("createWorkerByOf", Worker.class, "of", new Class[]{String.class, int.class});
        String result = expressionExecutor.execute(expressScript);
        System.err.println(result);
        Assertions.assertTrue((result == null ? 0 : result.length()) > 4);
        System.out.println("更多语法详见 https://github.com/alibaba/QLExpress?tab=readme-ov-file#%E4%B8%89%E8%AF%AD%E6%B3%95%E4%BB%8B%E7%BB%8D");
    }
    
    /**
     * 测试添加函数并使用 - 改函数的实现逻辑是对象实例的方法
     */
    @Test
    public void test14() {
        String expressScript =
                """
                return invokeQwer("张三", 18);
                """;
        
        expressionExecutor.addGlobalFunction("invokeQwer", demoXyzBean, "qwer2", new Class[]{String.class, int.class});
        String result = expressionExecutor.execute(expressScript);
        System.err.println(result);
        Assertions.assertEquals(4, result == null ? 0 : result.length());
    
        expressScript =
                """
                return invokeQwer();
                """;
        
        // 覆盖同名函数
        expressionExecutor.addGlobalFunction("invokeQwer", new DemoXyzBean(), "qwer1", new Class[]{}, true);
        result = expressionExecutor.execute(expressScript);
        System.err.println(result);
        Assertions.assertEquals(3, result == null ? 0 : result.length());
    
        expressScript =
                """
                return invokeQwerJd("张三", 18);
                """;
        expressionExecutor.addGlobalFunction("invokeQwerJd", new DemoXyzBean(), "qwer2", new Class[]{String.class, int.class});
        result = expressionExecutor.execute(expressScript);
        System.err.println(result);
        Assertions.assertEquals(4, result == null ? 0 : result.length());
        
        System.out.println("更多语法详见 https://github.com/alibaba/QLExpress?tab=readme-ov-file#%E4%B8%89%E8%AF%AD%E6%B3%95%E4%BB%8B%E7%BB%8D");
    }
    
}