package com.example.rabbitmqdemo.two;

import com.example.rabbitmqdemo.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author 钊思暮想
 * @date 2023/11/10 16:0
 *
 * 这是一个工作线程（相当于之前消费者）
 */
public class Worker01 {
    //队列名称
    public static final String QUEUE_NAME = "hello";

    //接受消息
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        //消息的接收
        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println("消息的接受："+new String(message.getBody()));
        };
        //消息接受被取消时，执行下面的内容
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println(consumerTag + "消费者取消消费接口回调逻辑");
        };


        /*
         * 消费者消费消息
         *  1.消费哪个列表
         *  2.消费成功后是否需要自动答应
         *  3.消费未成功消费的回调
         *  4.消费者取录消费的回调
         * */
        System.out.println("C2等待接受消息......");
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }

}
