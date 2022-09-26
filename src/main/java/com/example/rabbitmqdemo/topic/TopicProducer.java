package com.example.rabbitmqdemo.topic;

import com.example.rabbitmqdemo.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

//主题模式（路由模式一种）
public class TopicProducer {
    public static final String EXCHANGE_NAME="topic_log";
    public static void main(String[] args) throws Exception{
        Connection connection= ConnectionUtil.getConnection();
        Channel channel=connection.createChannel();
        HashMap<String,String> hashMap=new HashMap<>(16);
        hashMap.put("a.orange.b","消费a.orange.b");
        hashMap.put("a.b.rabbit","消费a.b.rabbit");
        hashMap.put("lazy.orange.b.c","消费lazy.orange.b.c");
        for(Map.Entry<String,String> item: hashMap.entrySet()){
            channel.basicPublish(EXCHANGE_NAME,item.getKey(),null,item.getValue().getBytes(StandardCharsets.UTF_8));
            System.out.println(item.getValue());
        }
        System.out.println("消息发送");
    }

}
