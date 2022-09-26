package com.example.rabbitmqdemo.routing;

import com.example.rabbitmqdemo.util.ConnectionUtil;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.BuiltinExchangeType;

import java.nio.charset.StandardCharsets;

//路由模式
public class Producer {
    //交换器
    static final String DIRECT_EXCHANGE="direct_exchange";
    //队列
    static final String DIRECT_QUEUE_INSERT="direct_queue_insert";

    static final String DIRECT_QUEUE_UPDATE="direct_queue_update";

    public static void main(String[] args) throws Exception{
        Connection connection= ConnectionUtil.getConnection();
        Channel channel=connection.createChannel();
        channel.exchangeDeclare(DIRECT_EXCHANGE,BuiltinExchangeType.DIRECT);
        /**
         * 参数1：队列名称
         * 参数2：是否定义持久化队列
         * 参数3：是否独占本次连接
         * 参数4：是否在不使用的时候自动删除队列
         * 参数5：队列其它参数
         */
        channel.queueDeclare(DIRECT_QUEUE_INSERT,true,false,false,null);
        channel.queueDeclare(DIRECT_QUEUE_UPDATE,true,false,false,null);
        channel.queueBind(DIRECT_QUEUE_INSERT,DIRECT_EXCHANGE,"insert");
        channel.queueBind(DIRECT_QUEUE_UPDATE,DIRECT_EXCHANGE,"update");
        String message="新增了。。路由模式：routing key 为 insert";

        /**
         * 参数1：交换机名称，如果没有指定则使用默认Default Exchage
         * 参数2：路由key,简单模式可以传递队列名称
         * 参数3：消息其它属性
         * 参数4：消息内容
         */
        channel.basicPublish(DIRECT_EXCHANGE,"insert",null,message.getBytes(StandardCharsets.UTF_8));
        System.out.println("已发送消息："+message);
        message="修改了。。路由模式：routing key 为 update";
        channel.basicPublish(DIRECT_EXCHANGE,"update",null,message.getBytes(StandardCharsets.UTF_8));
        System.out.println("已发送消息："+message);
        channel.close();
        connection.close();
    }
}
