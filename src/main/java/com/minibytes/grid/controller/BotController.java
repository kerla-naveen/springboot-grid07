package com.minibytes.grid.controller;

import com.minibytes.grid.dto.CreateBotRequest;
import com.minibytes.grid.entity.Bot;
import com.minibytes.grid.service.BotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bots")
public class BotController {

    @Autowired
    private BotService botService;

    @PostMapping
    public ResponseEntity<Bot> createBot(@RequestBody CreateBotRequest request) {
        Bot createdBot = botService.createBot(request);
        return ResponseEntity.ok(createdBot);
    }
}
