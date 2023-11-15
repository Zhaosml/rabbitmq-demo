package com.example.rabbitmqdemo.six;

import com.example.rabbitmqdemo.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * @author 钊思暮想
 * @date 2023/11/12 16:29
 */
public class EmitLogDirect {
    //交换机名称
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //channel.exchangeDeclare(EXCHANGE_NAME,"fauout");

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME,"info",null,message.getBytes("UTF-8"));
            System.out.println("生产者发出消息："+message);
        }

    }
}
