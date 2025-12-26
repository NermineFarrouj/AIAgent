package net.supervision.bot.agents;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import reactor.core.publisher.Flux;

import java.util.Arrays;

@Component
public class AIAgent {

    private ChatClient chatClient; //spring ai


    // injecter ChatClient
    public AIAgent(ChatClient.Builder builder, ChatMemory memory, ToolCallbackProvider tools) {
        Arrays.stream(tools.getToolCallbacks()).forEach(toolCallback -> {

            System.out.println("---------------------------------------");
            System.out.println(toolCallback.getToolDefinition());
            System.out.println("---------------------------------------");
        });
        this.chatClient = builder
                .defaultSystem("""
                        Vous etes un assistant qui se charge de répondre aux question de l'utilisateur en fonction du contexte fourni.
                        Si aucun contexte n'est fourni réponds avec je ne sais pas.
                        """)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(memory).build())
                //use the tools u got from Mcp server
                .defaultToolCallbacks(tools)
                .build();
    }

    public String askAgent (String query)
    {
        return chatClient.prompt()
                .user(query)
                .call().content();
    }
}
