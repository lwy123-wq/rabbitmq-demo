package com.example.rabbitmqdemo.topic;

import com.example.rabbitmqdemo.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

public class TopicConsumer2 {
    public static void main(String[] args) throws Exception{
        Connection connection= ConnectionUtil.getConnection();
        Channel channel=connection.createChannel();
        channel.exchangeDeclare(TopicProducer.EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        //临时队列
        String queue=channel.queueDeclare().getQueue();
        channel.queueBind(queue,TopicProducer.EXCHANGE_NAME,"lazy.#");
        System.out.println("消息接收。。。。");
        DefaultConsumer consumer=new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消息消费: "+new String(body,"utf-8")+"  绑定的routingKey:  "+envelope.getRoutingKey());
            }
        };
        channel.basicConsume(queue,true,consumer);
    }
}
