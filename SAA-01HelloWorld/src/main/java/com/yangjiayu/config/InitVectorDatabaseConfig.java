package com.yangjiayu.config;

import cn.hutool.crypto.SecureUtil;
import jakarta.annotation.PostConstruct;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @Classname InitVectorDatabaseConfig
 * @Description 初始化向量数据库
 * @Date 2025/9/27 18:44
 * @Created by YangJiaYu
 */
@Configuration
public class InitVectorDatabaseConfig {

    private final VectorStore vectorStore;

    private final RedisTemplate<String,String> redisTemplate;


    public InitVectorDatabaseConfig(VectorStore vectorStore, RedisTemplate<String, String> redisTemplate) {
        this.vectorStore = vectorStore;
        this.redisTemplate = redisTemplate;
    }

    @Value("classpath:RAG资料/ops.txt")
    private Resource opsFile;

    @PostConstruct //  @PostConstruct 是 Spring 项目中「依赖注入完成后，自动执行一次初始化逻辑」的标准做法。
    public void init() throws IOException {

        //索引（indexing）
        //1.读取文件
        TextReader textReader = new TextReader(opsFile);
        textReader.setCharset(Charset.forName("UTF-8"));//Charset.defaultCharset

        //2.文件转换为向量（开启分词）
        List<Document> list = new TokenTextSplitter().transform(textReader.read());

        //3.写入向量数据库Redis-Stack
//        vectorStore.add(list);
        
        // 解决上面第3步，向量数据重复问题，使用redis setnx 命令处理

//        //4.去重复版
//        String source = (String)textReader.getCustomMetadata().get("source");
//
//        String sourceMetadata = (String)textReader.getCustomMetadata().get("source");
//
//        String textHash = SecureUtil.md5(sourceMetadata);
//        String redisKey = "vector-xxx:"+textHash;
//
//        // setIfAbsent 相当于 Redis 命令 SETNX (SET if Not eXists)
//        // 判断是否存入过 redisKey如果能够成功插入表示以前没有过 可以成功插入
//        Boolean retFlag = redisTemplate.opsForValue().setIfAbsent(redisKey, "1");
//
//        System.out.println("**********  retFlag  **********" +retFlag);
//
//        if(Boolean.TRUE.equals(retFlag)){
//            //键不存在，首次插入，可以保存进向量数据库
//            vectorStore.add(list);
//        }else{
//            // 键已存在，跳过或者报错
////            throw new RuntimeException("-------重复操作-------");
//            System.out.println("请不要重复操作");
//        }

        //如果你希望“当文档内容发生变化时，允许重新加载（更新）”，你就必须对文档内容进行哈希，而不是对文件名进行哈希。
        // =================== 修改开始 ==================
        // 计算【文件内容】的哈希值
        String content;
        try(InputStream inputStream = opsFile.getInputStream()){
            // 使用Spring自带的StreamUtils 将输入流转为字符串
            content = StreamUtils.copyToString(inputStream,Charset.forName("UTF-8"));
        }

        // 对【内容】进行MD5哈希
        String contentHash = SecureUtil.md5(content);
        String redisKey = "vector-yjy:"+contentHash;
        // ============= 修改结束 ===========

        // 判断是否存入过redisKey
        Boolean retFlag = redisTemplate.opsForValue().setIfAbsent(redisKey, "1");

        System.out.println(" ******** retFlag ********"+retFlag + "Key: "+ redisKey);

        if(Boolean.TRUE.equals(retFlag)){
            // 哈希值是新的（内容变了 或者第一次允许），都保存进入向量数据库
            System.out.println("检测到新内容或首次运行，开始写入向量数据库");
            //【可选优化】：如果你想在内容更新后，删除旧版本的向量数据，这里还需要加入额外的逻辑
            // 目前逻辑是更新后两个版本都会存在
        }else{
            // 哈希值已经存在 说明内容没有发送变化，跳过向量化操作。
            System.out.println("内容未发生改变，跳过向量化操作");
        }
    }
}
