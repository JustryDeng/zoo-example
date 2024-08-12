package com.ideaaedi.zoo.example.springboot.websocket.endpoint;

import com.ideaaedi.zoo.diy.artifact.websocket.core.AbstractWebSocketEndpoint;
import com.ideaaedi.zoo.diy.artifact.websocket.core.KeepWebSocketHeaders;
import jakarta.websocket.OnMessage;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

/**
 * demo websocket服务端点
 * <br />
 * 注：如果配置有spring-security之类的全局认证拦截器的话，记得放行websocket相关的endpoint
 *
 * @author <font size = "20" color = "#3CAA3C"><a href="https://gitee.com/JustryDeng">JustryDeng</a></font> <img
 * src="https://gitee.com/JustryDeng/shared-files/raw/master/JustryDeng/avatar.jpg" />
 * @since 1.0.0
 */
@Slf4j
@Component
@ServerEndpoint(value = "/demo/{terminalId}", configurator = KeepWebSocketHeaders.class)
public class DemoWsEndpoint extends AbstractWebSocketEndpoint {
    
    @Nonnull
    @Override
    protected String sessionUuid(@Nonnull Session session) {
        Map<String, List<String>> requestParameterMap = session.getRequestParameterMap();
        return requestParameterMap.get("terminalId").get(0);
    }
    
    /**
     * 服务端收到消息
     */
    @OnMessage
    public void onMessage(String message, @Nonnull Session session) {
        log.info("received message. id -> {}, path -> {}, userProperties -> {}, message -> {}",
                session.getId(), session.getRequestURI().getPath(), session.getUserProperties(), message);
        sendMessage(session, "received message success. message is -> " + message);
    }

}
