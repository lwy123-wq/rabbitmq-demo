package com.example.rabbitmqdemo.fanout;

import com.example.rabbitmqdemo.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

//发布订阅模式（共享资源）
public class Send {
    private final static String EXCHANGE_NAME="test_exchange_fanout";

    public static void main(String[] args) {
        try {
            Connection connection= ConnectionUtil.getConnection();
            Channel channel=connection.createChannel();
            //声明交换机
            channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
            for (int i = 0; i <10 ; i++) {
                String message="无语子，无语死了"+i;
                System.out.println("send:"+message);
                channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes(StandardCharsets.UTF_8));
                Thread.sleep(5*i);
            }
            channel.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
