package com.yangjiayu.controller;

import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingOptions;
import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * @Classname Embed2VectorController
 * @Description TODO
 * @Date 2025/9/25 22:50
 * @Created by YangJiaYu
 */
@RestController
public class Embed2VectorController {

    @Resource
    private EmbeddingModel embeddingModel;



    @Resource
    private VectorStore vectorStore;

    /**
     * 文本向量化
     *  http://localhost:8999/text2embed?msg=射雕英雄传
     * @param msg
     * @return
     */
    @GetMapping("/text2embed")
    public EmbeddingResponse text2Embed(String msg){
        EmbeddingResponse embeddingResponse = embeddingModel.call(new EmbeddingRequest(List.of(msg),
            DashScopeEmbeddingOptions.builder().withModel("text-embedding-v4").build()));

        System.out.println(Arrays.toString(embeddingResponse.getResult().getOutput()));

        return embeddingResponse;
    }

    /**
     * 文本向量化后 存入向量数据库RedisStack
     */
    @GetMapping("/embed2vector/add")
    public void add(){
        List<Document> documents = List.of(
          new Document("i study LLM"),
          new Document("i love java")
        );

        vectorStore.add(documents);
    }

    /**
     * 从向量数据库RedisStack查找，进行相似度查找
     */
    @GetMapping("/embed2vector/get")
    public List getAll(@RequestParam(name = "msg")String msg){

        SearchRequest searchRequest = SearchRequest.builder()
            .query(msg)
            .topK(2)
            .build();
        List<Document> list = vectorStore.similaritySearch(searchRequest);

        System.out.println(list);

        return list;
    }


}
