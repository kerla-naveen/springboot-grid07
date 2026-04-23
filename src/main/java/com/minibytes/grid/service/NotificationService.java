package com.minibytes.grid.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Set;

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
            String message="Bot:"+botId+" replied to your post";
            redisTemplate.opsForList().rightPush(key, message);

            key="activeUsers:"+userId;
            redisTemplate.opsForValue().set(key, "1");
        }
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void sweepPendingNotifications(){

        Set<String> activeUsers= redisTemplate.keys("activeUsers:*");
        List<Long> userIds= activeUsers.stream()
                .map( key -> key.split(":")[1])
                .map( str -> Long.parseLong(str)).toList();

        for(Long userId : userIds){
            String key= "pending_notifs:user:"+userId;
            List<String> messages = redisTemplate.opsForList().range(key, 0, -1);
            
            if (!messages.isEmpty()) {
                int count = messages.size();
                
                log.info("Summarized Push Notification: Bot interactions for user {}: {} notifications", userId, count);
                
                // Clear the Redis list for that user
                redisTemplate.delete(key);
                redisTemplate.delete("activeUsers:" + userId);
            }
        }
    }
}
