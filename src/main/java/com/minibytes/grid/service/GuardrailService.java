package com.minibytes.grid.service;

import com.minibytes.grid.exception.TooManyRequestsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
public class GuardrailService {

    private static final int BOT_REPLY_LIMIT = 100;
    private static final int MAX_DEPTH_LEVEL = 20;
    private static final long COOLDOWN_TTL_SECONDS = 600;

    // Atomically increments the counter and rolls back if the cap is exceeded.
    // Returns the new count on success, 0 if the cap was hit.
    private static final String INCR_WITH_CAP_SCRIPT =
            "local count = redis.call('INCR', KEYS[1]) " +
            "if count > tonumber(ARGV[1]) then " +
            "    redis.call('DECR', KEYS[1]) " +
            "    return 0 " +
            "end " +
            "return count";

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void checkAndIncrementBotCount(Long postId) {
        String key = "post:" + postId + ":bot_count";
        DefaultRedisScript<Long> script = new DefaultRedisScript<>(INCR_WITH_CAP_SCRIPT, Long.class);
        Long result = redisTemplate.execute(script, List.of(key), String.valueOf(BOT_REPLY_LIMIT));
        if (result == null || result == 0) {
            log.warn("Horizontal cap reached for postId={}", postId);
            throw new TooManyRequestsException("Bot reply limit of " + BOT_REPLY_LIMIT + " reached for post: " + postId);
        }
        log.info("Bot reply count for postId={} is now {}", postId, result);
    }

    public void checkDepthLevel(int depthLevel) {
        if (depthLevel > MAX_DEPTH_LEVEL) {
            log.warn("Vertical cap exceeded: depthLevel={} max={}", depthLevel, MAX_DEPTH_LEVEL);
            throw new TooManyRequestsException("Comment depth cannot exceed " + MAX_DEPTH_LEVEL + " levels");
        }
    }

    public void checkAndSetCooldown(Long botId, Long humanId) {
        String key = "cooldown:bot_" + botId + ":human_" + humanId;
        Boolean isNew = redisTemplate.opsForValue().setIfAbsent(key, "1", Duration.ofSeconds(COOLDOWN_TTL_SECONDS));
        if (Boolean.FALSE.equals(isNew)) {
            log.warn("Cooldown active: botId={} cannot interact with humanId={}", botId, humanId);
            throw new TooManyRequestsException("Bot " + botId + " must wait 10 minutes before interacting with user " + humanId + " again");
        }
        log.info("Cooldown set for botId={} -> humanId={} ({}s TTL)", botId, humanId, COOLDOWN_TTL_SECONDS);
    }
}
