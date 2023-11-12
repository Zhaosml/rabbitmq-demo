package com.example.rabbitmqdemo.five;

import com.example.rabbitmqdemo.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

/**
 * @author 钊思暮想
 * @date 2023/11/11 21:14
 *
 * 消息接受
 */
public class ReceiveLogs01 {
    //交换机名称
    public static final String EXHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXHANGE_NAME,"fanout");
        //声明一个临时队列
        /*
        * 生成一个临时的队列，队列的名称是随机的
        * 当消费者断开与队列的连接的时候  队列自动删除
        * */
        String queueName = channel.queueDeclare().getQueue();
        /*
        * 绑定交换机与队列
        * */
        channel.queueBind(queueName,EXHANGE_NAME,"");
        System.out.println("等待接受消息，把接受到的消息打印到屏幕上.....");

        //接受消息
        DeliverCallback deliverCallback = ( consumerTag,  message)->{
            System.out.println("ReceiveLogs01控制台打印接收到的消息"+ new String(message.getBody(),"UTF-8"));
        };
        //消费者取消消息时回调接口
        channel.basicConsume(queueName,true,deliverCallback,consumerTag -> {});
    }
}
