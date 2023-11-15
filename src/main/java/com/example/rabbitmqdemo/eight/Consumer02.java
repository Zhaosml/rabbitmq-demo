package com.example.rabbitmqdemo.eight;

import com.example.rabbitmqdemo.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 钊思暮想
 * @date 2023/11/13 15:56
 *
 * 死信队列
 *
 * 消费者2
 */
public class Consumer02 {

    //死信队列的名称
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("等待接收.....");
        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println("Consumer02接受的消息是"+new String(message.getBody(),"UTF-8"));
        };
        channel.basicConsume(DEAD_QUEUE,true,deliverCallback, consumerTag -> {});

    }
}
