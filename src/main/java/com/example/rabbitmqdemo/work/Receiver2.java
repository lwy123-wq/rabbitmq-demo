package com.example.rabbitmqdemo.work;

import com.example.rabbitmqdemo.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Receiver2 {
    private final static String QUEUE_NAME="queue_work";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection= ConnectionUtil.getConnection();
        Channel channel=connection.createChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //同一时刻服务器只发送一条消息给消费者
        channel.basicQos(1);
        QueueingConsumer consumer=new QueueingConsumer(channel);
        channel.basicConsume(QUEUE_NAME,false,consumer);
        while (true){
            QueueingConsumer.Delivery delivery=consumer.nextDelivery();
            String message=new String(delivery.getBody());
            System.out.println("[消费者2] Received2 '"+message+"'");
            Thread.sleep(10);
            //手动回ack值，是否批量操作
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
        }
    }
}
