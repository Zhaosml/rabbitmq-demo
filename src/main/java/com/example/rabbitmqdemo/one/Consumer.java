package com.example.rabbitmqdemo.one;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.concurrent.TimeoutException;

/**
 * @author 钊思暮想
 * @date 2023/11/10 14:58
 *
 *  消费者 接收消息的
 */

public class Consumer {
    //队列名称
    private static final String QUEUE_NAME= "hello";
    public static void main(String[] args) throws IOException, TimeoutException {
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");
        //创建连接
        Connection connection = factory.newConnection();
        //获取信道
        Channel channel = connection.createChannel();


        //声明 接受消息
        //参数类型点击进入DeliverCallback查看
        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println(new String(message.getBody()));
        };

        //取消消息时的回调
        CancelCallback cancelCallback = (consumerTag) ->{
            System.out.println("消费消息被中断");
        };
        /*
        * 消费者消费消息
        *  1.消费哪个列表
        *  2.消费成功后是否需要自动答应
        *  3.消费未成功消费的回调
        *  4.消费者取录消费的回调
        * */
        System.out.println("C1等待接受消息.....");
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
