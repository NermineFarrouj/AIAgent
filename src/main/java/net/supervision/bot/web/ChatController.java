package net.supervision.bot.web;

import net.supervision.bot.agents.AIAgent;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
//Avec ChatClient (interface) on peut communiquer avec n'importe qu'elle LLM
//un Controlleur qui permet d'interroger le LLM
public class ChatController {
    private AIAgent aiAgent;
    public ChatController(AIAgent aiAgent) {
        this.aiAgent = aiAgent;
    }
    @GetMapping(value="/chat",produces= MediaType.TEXT_PLAIN_VALUE)
    public String chat(String query) {
        return aiAgent.askAgent(query);
    }


}
