package com.yangjiayu.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @Classname RAGController
 * @Description RAGController
 * @Date 2025/9/27 19:04
 * @Created by YangJiaYu
 */
@RestController
public class RAGController {

//    @Resource(name = "qwenChatClient")
//    private ChatClient chatClient;

//    @Resource
//    private VectorStore vectorStore;


    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public RAGController(@Qualifier("qwenChatClient") ChatClient chatClient,VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }


    @GetMapping("/rag4aiops")
    public Flux<String> rag(String msg){
        String systemInfo = """
                你是一个运维工程师，按照给出的编码给出对应故障解释，否则回复找不到信息。
            """;

        RetrievalAugmentationAdvisor advisor = RetrievalAugmentationAdvisor.builder()
            .documentRetriever(VectorStoreDocumentRetriever.builder().vectorStore(vectorStore).build())
            .build();

        return chatClient.prompt().system(systemInfo).user(msg).advisors(advisor).stream().content();

    }

}
