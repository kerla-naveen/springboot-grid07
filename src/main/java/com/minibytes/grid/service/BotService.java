package com.minibytes.grid.service;

import com.minibytes.grid.dto.CreateBotRequest;
import com.minibytes.grid.entity.Bot;
import com.minibytes.grid.repository.BotRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BotService {

    @Autowired
    private BotRepository botRepository;

    public Bot createBot(CreateBotRequest request) {
        log.info("Creating bot with name={}", request.getName());
        Bot bot = Bot.builder()
                .name(request.getName())
                .personaDescription(request.getPersonaDescription())
                .build();
        Bot saved = botRepository.save(bot);
        log.info("Bot created with id={}", saved.getId());
        return saved;
    }
}
