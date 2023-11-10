package com.example.rabbitmqdemo.three;

import com.example.rabbitmqdemo.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * @author 钊思暮想
 * @date 2023/11/10 18:18
 *
 * 消息在手动应答时不丢失、放回队列中重新消费
 */
public class Task2 {
    //队列名称
    public static final String task_queue_name = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明队列
        channel.queueDeclare(task_queue_name,false,false,false,null);
        //从控制台中输入信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish("",task_queue_name,null,message.getBytes("UTF-8"));
            System.out.println("消息发送完毕:"+message);
        }
    }
}
