package com.example.rabbitmqdemo.simple;

import com.example.rabbitmqdemo.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Receiver {
    private final static String QUEUE_NAME="simple_queue";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection= ConnectionUtil.getConnection();
        Channel channel=connection.createChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //创建消费者指定建立在哪个链接上
        QueueingConsumer consumer=new QueueingConsumer(channel);
        // 第二个参数 是否自动签收
        // 第三个参数表示消费对象
        channel.basicConsume(QUEUE_NAME,true,consumer);
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            System.out.println("received:" + message + ",");
        }
    }
}
