package com.minibytes.grid.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
public class NotificationService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    private final int COOLDOWN_TIME = 15;

    public void notificationsHandling(Long botId, Long userId){

        //check cooldown state
        Boolean canSend=redisTemplate.opsForValue()
                .setIfAbsent("user:"+userId+"cooldown:", "1", Duration.ofMinutes(COOLDOWN_TIME));

        if(Boolean.TRUE.equals(canSend)){
            log.info("Push Notification Sent to User:{}", userId);
        }
        else{
            String key= "pending_notifs:user:"+userId;
            String message="Bot:"+botId+"replied to your post";
            redisTemplate.opsForList().rightPush(key, message);

            key="activeUsers:"+userId;
            redisTemplate.opsForValue().set(key, "1");
        }
    }
}
