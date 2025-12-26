package net.supervision.bot.telegram;

import jakarta.annotation.PostConstruct;
import net.supervision.bot.agents.AIAgent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    @Value("${telegram.api.key}")
    private String telegramBotToken;
    private AIAgent aiAgent;

    public TelegramBot(AIAgent aiAgent) {
        this.aiAgent = aiAgent;
    }

    @PostConstruct
    //agent subscribe vers lAPI telegram en utilisant le key fourni
    public void registerTelegramBot() {
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(this);
        }catch (TelegramApiException e){
            throw new RuntimeException(e);
        }

    }


    @Override
    //methode qui reçoit le message lorsque envoyer par le user de telegram
    public void onUpdateReceived(Update telegramRequest) {
        try {
            if (!telegramRequest.hasMessage()) return;
            String msgTxt = telegramRequest.getMessage().getText();
            Long chatId = telegramRequest.getMessage().getChatId();
            sendTypingQuestion(chatId);
            String answer = aiAgent.askAgent(msgTxt);
            sendTextMessage(chatId, answer);
        }catch (TelegramApiException e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public String getBotUsername() {
        return "FirstLateBot";
    }

    @Override
    public String getBotToken() {
        return telegramBotToken;
    }

    private void sendTextMessage(Long chatId, String text)  throws TelegramApiException {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), text);
        execute(sendMessage);

    }
    private void sendTypingQuestion(Long chetId) throws TelegramApiException {
        SendChatAction sendChatAction = new SendChatAction();
        sendChatAction.setChatId(String.valueOf(chetId));
        sendChatAction.setAction(ActionType.TYPING);
        execute(sendChatAction);

    }
}
