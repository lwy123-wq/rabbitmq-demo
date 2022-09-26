package com.example.rabbitmqdemo.routing;

import com.example.rabbitmqdemo.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

public class Consumer2 {
    public static void main(String[] args) throws Exception{
        Connection connection= ConnectionUtil.getConnection();
        Channel channel=connection.createChannel();
        channel.exchangeDeclare(Producer.DIRECT_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.queueDeclare(Producer.DIRECT_QUEUE_UPDATE,true,false,false,null);
        channel.queueBind(Producer.DIRECT_QUEUE_UPDATE,Producer.DIRECT_EXCHANGE,"update");
        DefaultConsumer consumer=new DefaultConsumer(channel){
            /**
             * consumerTag 消息者标签，在channel.basicConsume时候可以指定
             * envelope 消息包的内容，可从中获取消息id，消息routingkey，交换机，
             *          消息和重传标志(收到消息失败后是否需要重新发送)
             * properties 属性信息
             * body 消息
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                //路由key
                System.out.println("路由key为：" + envelope.getRoutingKey());
                //交换机
                System.out.println("交换机为：" + envelope.getExchange());
                //消息id
                System.out.println("消息id为：" + envelope.getDeliveryTag());
                //收到的消息
                System.out.println("消费者1-接收到的消息为：" + new String(body, "utf8"));
            }
        };
        channel.basicConsume(Producer.DIRECT_QUEUE_UPDATE,true,consumer);

    }
}
