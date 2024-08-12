package com.ideaaedi.zoo.example.springboot.liteflow.basic;

import com.yomahub.liteflow.script.ScriptExecuteWrap;
import com.yomahub.liteflow.script.body.JaninoBooleanScriptBody;
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
