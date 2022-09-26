package com.example.rabbitmqdemo.fanout;

import com.example.rabbitmqdemo.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Receive1 {
    private final static String EXCHANGE_NAME="test_exchange_fanout";
    private static final String QUEUE_NAME="test_queue_email";

    public static void main(String[] args) {
        try {
            Connection connection= ConnectionUtil.getConnection();
            final Channel channel=connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
            channel.queueDeclare(QUEUE_NAME,false,false,false,null);
            //将队列绑定到交换机上
            channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"");
            //保证一次只发送一个
            channel.basicQos(1);
            DefaultConsumer consumer=new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message=new String(body,"utf-8");
                    System.out.println("[email] Receive message：" + message);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        System.out.println("[1] done");
                        //手动应答
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    }
                }
            };
            boolean autoAck=false;
            channel.basicConsume(QUEUE_NAME,autoAck,consumer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
