package com.bookstore.ai.controllers;

import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/bookstore")
public class BookstoreAssistantController {

    private final OpenAiChatClient openAiChatClient;

    public BookstoreAssistantController(OpenAiChatClient openAiChatClient) {
        this.openAiChatClient = openAiChatClient;
    }

    @GetMapping("/top/informations")
    public String bookStoreChatReturnString(@RequestParam(value = "message",
            defaultValue = "Quais são os livros best sellers dos ultimos anos?") String message) {
        return openAiChatClient.call(message);
    }

    @GetMapping("/informations")
    public ChatResponse bookStoreChat(@RequestParam(value = "message",
            defaultValue = "Quais são os livros best sellers dos ultimos anos?") String message) {
        return openAiChatClient.call(new Prompt(message));
    }

    @GetMapping("/reviews")
    public String bookstoreReview(@RequestParam(value = "book", defaultValue = "Dom Quixote") String book) {
        PromptTemplate promptTemplate = new PromptTemplate("""
                  Por favor, me forneça
                  um breve resumo do livro {book}
                  e também a biografia de seu autor.
                """);
        promptTemplate.add("book", book);
        return this.openAiChatClient.call(promptTemplate.create()).getResult().getOutput().getContent();
    }

    @GetMapping("/top/stream/informations")
    public Flux<String> bookstoreChatStream(@RequestParam(value = "message",
            defaultValue = "Quais são os livros best sellers dos ultimos anos?") String message){
        return openAiChatClient.stream(message);
    }

    @GetMapping("/stream/informations")
    public Flux<ChatResponse> bookstoreChatStreamEx2(@RequestParam(value = "message",
            defaultValue = "Quais são os livros best sellers dos ultimos anos?") String message){
        return openAiChatClient.stream(new Prompt(message));
    }
}
