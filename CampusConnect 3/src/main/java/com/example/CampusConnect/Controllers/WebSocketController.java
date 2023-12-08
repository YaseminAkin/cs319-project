package com.example.CampusConnect.Controllers;

import com.example.CampusConnect.Services.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.CampusConnect.Messaging.Message;

@RestController
public class WebSocketController {

    @Autowired
    private WebSocketService service;

    @PostMapping("/send-message")
    public void sendMessage(@RequestBody final Message message){
        service.notifyFrontend(message.getMessageContents());
    }


}
