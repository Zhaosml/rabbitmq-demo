package com.example.rabbitmqdemo.seven;

import com.example.rabbitmqdemo.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author 钊思暮想
 * @date 2023/11/12 20:19
 */
@SuppressWarnings("ALL")
public class ReceiveLogsTopic02 {
    //交换机的名称
    public static final String EXCHANGE_NAME = "topic_logs";
    //接受消息
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
         //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        //声明一个队列
        String queueName = "Q2";
        channel.queueDeclare(queueName,false,false,false,null);
        channel.queueBind(queueName,EXCHANGE_NAME,"*.*.rabbit");
        channel.queueBind(queueName,EXCHANGE_NAME,"lazy.#");
        System.out.println("等待接受消息");
        DeliverCallback deliverCallback = ( consumerTag,  message)->{
            System.out.println(new String(message.getBody(),"UTF-8"));
            System.out.println("接受队列："+queueName+"绑定键："+message.getEnvelope().getRoutingKey());
        };
        //接受消息
        channel.basicConsume(queueName,true,deliverCallback,consumerTag -> {});

    }
}
