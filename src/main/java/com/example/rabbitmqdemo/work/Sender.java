package com.example.rabbitmqdemo.work;

import com.example.rabbitmqdemo.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

//资源竞争多个消费者模式
public class Sender {
    private final static String QUEUE_NAME="queue_work";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection= ConnectionUtil.getConnection();
        Channel channel=connection.createChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        for (int i = 0; i < 100; i++) {
            String message="我想暴富，我想要暴富"+i;
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("Send:"+message);
            Thread.sleep(i*10);
        }
        channel.close();
        connection.close();
    }
}
