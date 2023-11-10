package com.example.rabbitmqdemo.two;

import com.example.rabbitmqdemo.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * @author 钊思暮想
 * @date 2023/11/10 16:20
 *
 * 生产者   发送大量的消息
 */
public class Task1 {
    //队列名称
    public static final String QUEUE_NAME = "hello";

    //发送大量的消息
    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();
        //队列的声明
        /*
         * 生成一个队列
         * 1.队列名称
         * 2.队列里面的消息是否持久化(磁盘)默认情况消息存储在内存中
         * 3.该队列是否只提供一个消费者进行消费，是否进行消息共享，true可以多个消费者消费，false：只能消费一个
         * 4.是否自动删除  最后一个消费者端开始连接以后  该队一句是否自动删除  true自动删除 false不自动删除
         * 5.其他参数
         * */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //从控制台中接受信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            //是否有下一个消息
            String message = scanner.next();
            /*
             * 发送一个消费
             * 1、发送到哪个交换机
             * 2、路由的key值是哪个  本次的队列的名称
             * 3、其他参数信息
             * 4、发送消息的消息体
             */
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println("消息发送完毕"+message);
        }

    }
}
