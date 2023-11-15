package com.example.rabbitmqdemo.three;

import com.example.rabbitmqdemo.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.util.Scanner;

/**
 * @author 钊思暮想
 * @date 2023/11/10 18:18
 *
 * 消息在手动应答时不丢失、放回队列中重新消费
 */
public class Task2 {
    //队列名称
    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明队列
        /*
         * 生成一个队列
         * 1.队列名称
         * 2.队列里面的消息是否持久化(磁盘)默认情况消息存储在内存中
         * 3.该队列是否只提供一个消费者进行消费，是否进行消息共享，true可以多个消费者消费，false：只能消费一个
         * 4.是否自动删除  最后一个消费者端开始连接以后  该队一句是否自动删除  true自动删除 false不自动删除
         * 5.其他参数
         * */
        boolean durable = true;//需要让queue进行持久化
        channel.queueDeclare(task_queue_name,durable,false,false,null);
        //从控制台中输入信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            //设置生产者发送消息为持久化消息(要求保存在磁盘上)保存在内存中
            channel.basicPublish("",TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes("UTF-8"));
            System.out.println("消息发送完毕:"+message);
        }
    }
}
