package com.minibytes.grid.service;

import com.minibytes.grid.dto.CreateBotRequest;
import com.minibytes.grid.entity.Bot;
import com.minibytes.grid.repository.BotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BotService {

    @Autowired
    private BotRepository botRepository;

    public Bot createBot(CreateBotRequest request) {
        Bot bot = Bot.builder()
                .name(request.getName())
                .personaDescription(request.getPersonaDescription())
                .build();
        
        return botRepository.save(bot);
    }
}
