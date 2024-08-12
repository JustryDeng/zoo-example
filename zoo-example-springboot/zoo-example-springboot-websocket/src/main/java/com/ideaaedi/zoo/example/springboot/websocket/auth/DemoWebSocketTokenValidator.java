package com.ideaaedi.zoo.example.springboot.websocket.auth;

import com.ideaaedi.zoo.diy.artifact.websocket.exception.WebSocketAuthException;
import com.ideaaedi.zoo.diy.artifact.websocket.validator.WebSocketTokenValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;


/**
 * 提示：如果你不需要认证，那么不实现此接口即可
 */
@Component
public class DemoWebSocketTokenValidator implements WebSocketTokenValidator {
    @Nonnull
    @Override
    public Map<String, Object> validate(@Nullable String token) throws WebSocketAuthException {
        if (StringUtils.isBlank(token)) {
            throw new WebSocketAuthException("token认证不通过！");
        }
        boolean pass = token.equals("your-token");
        if (!pass) {
            throw new WebSocketAuthException("token非法或过期！");
        }
        return Map.of("userId", 9527);
    }
}
