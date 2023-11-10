package com.example.rabbitmqdemo.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author 钊思暮想
 * @date 2023/11/10 09:15
 * 生产者
 */
public class Producer {
    //队列名称
    private static final String QUEUE_NAME = "hello";

    //发消息
    public static void main(String[] args) throws Exception {
        //创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //工厂ip  连接rabbitmq的队列
        factory.setHost("localhost");
        //用户名
        factory.setUsername("guest");
        //密码
        factory.setPassword("guest");

        //创建链接
        try (Connection connection = factory.newConnection()) {
            //获取信道
            Channel channel = connection.createChannel();

            /*
             * 生成一个队列
             * 1.队列名称
             * 2.队列里面的消息是否持久化(磁盘)默认情况消息存储在内存中
             * 3.该队列是否只提供一个消费者进行消费，是否进行消息共享，true可以多个消费者消费，false：只能消费一个
             * 4.是否自动删除  最后一个消费者端开始连接以后  该队一句是否自动删除  true自动删除 false不自动删除
             * 5.其他参数
             * */
            channel.queueDeclare(QUEUE_NAME,false,false,false,null);

            //发消息
            String message = "hello world";//初次使用

            /*
             * 发送一个消费
             * 1、发送到哪个交换机
             * 2、路由的key值是哪个  本次的队列的名称
             * 3、其他参数信息
             * 4、发送消息的消息体
             */
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println("消息发送完毕");
        }
    }


}
