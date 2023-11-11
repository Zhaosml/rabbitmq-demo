package com.example.rabbitmqdemo.three;

import com.example.rabbitmqdemo.utils.RabbitMqUtils;
import com.example.rabbitmqdemo.utils.SleepUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author 钊思暮想
 * @date 2023/11/10 19:10
 */
public class Work03 {
    //队列名称
    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("C1等待接受消息处理时间比较短");

        DeliverCallback deliverCallback = (consumerTag,message) -> {
            //沉睡IS
            SleepUtils.sleep(1);
            System.out.println("接受的消息："+new String(message.getBody(),"UTF-8"));
            //手动应答
            /*
            * 1.消息的标记  tag
            * 2.是否批量应答 true：批量  false：不批量
            * */
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };
        //int prefetchCount = 1;
        //预取值
        int prefetchCount = 2;
        //设置成不公平分发
        channel.basicQos(prefetchCount);

        //采用手动应答
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME,autoAck,deliverCallback,(consumerTag -> {
            System.out.println(consumerTag + "消费者取消消费接口回调逻辑");
        }));
    }
}
