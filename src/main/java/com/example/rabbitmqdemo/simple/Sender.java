package com.example.rabbitmqdemo.simple;

import com.example.rabbitmqdemo.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Sender {
    private final static String QUEUE_NAME="simple_queue";
    /**
    * 简单模式：一个生产者一个消费者
    * */
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection= ConnectionUtil.getConnection();
        Channel channel=connection.createChannel();
        /**
         * 参数：队列名，是否持久化，是否排外（即只允许该channel
         * 访问该队列   一般等于true的话用于一个队列只能有一个消
         * 费者来消费的场景），是否消费完自动删除，其他属性
         */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        String message="我爱学习，学习爱我，学习是我快乐";
        /**
         * 交换机，队列名，其他属性如路由，消息体
         */
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes(StandardCharsets.UTF_8));
        System.out.println("send:"+message+",");
        channel.close();
        connection.close();
    }
}
