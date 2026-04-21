package com.minibytes.grid.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ViralityService {

    private static final int BOT_REPLY_POINTS = 1;
    private static final int HUMAN_LIKE_POINTS = 20;
    private static final int HUMAN_COMMENT_POINTS = 50;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void onBotReply(Long postId) {
        increment(postId, BOT_REPLY_POINTS);
    }

    public void onHumanLike(Long postId) {
        increment(postId, HUMAN_LIKE_POINTS);
    }

    public void onHumanComment(Long postId) {
        increment(postId, HUMAN_COMMENT_POINTS);
    }

    private void increment(Long postId, long points) {
        String key = "post:" + postId + ":virality_score";
        Long score = redisTemplate.opsForValue().increment(key, points);
        log.info("Virality score updated for postId={} +{} -> total={}", postId, points, score);
    }
}
