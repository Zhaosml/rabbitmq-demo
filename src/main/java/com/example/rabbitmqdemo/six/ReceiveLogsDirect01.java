package com.example.rabbitmqdemo.six;

import com.example.rabbitmqdemo.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author 钊思暮想
 * @date 2023/11/12 16:08
 */
public class ReceiveLogsDirect01 {
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //声明一个队列
        channel.queueDeclare("console",false,false,false,null);
        channel.queueBind("console",EXCHANGE_NAME,"info");
        channel.queueBind("console",EXCHANGE_NAME,"warning");

        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println("ReceiveLogsDirect01控制台打印接收到的消息"+ new String(message.getBody(),"UTF-8"));
        };
        channel.basicConsume("console",true,deliverCallback,consumerTag -> {});
    }
}
