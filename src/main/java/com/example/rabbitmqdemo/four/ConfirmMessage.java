package com.example.rabbitmqdemo.four;

import com.example.rabbitmqdemo.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.ConnectionFactory;

import java.security.PublicKey;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author 钊思暮想
 * @date 2023/11/11 10:38
 *
 * 发布确认模式
 *  使用的时间 比较哪种确认方式最好的
 *  1.单个确认
 *  2.批量确认
 *  3.异步批量确认
 */
public class ConfirmMessage {
    //批量时间
    public static final int MESSAGE_COUNT = 1000;
    public static void main(String[] args) throws Exception {
        //1.单个确认
        //ConfirmMessage.publishMessageIndividually();
        //2.批量确认
        //ConfirmMessage.publishMessageBatch();
        //3.异步批量确认
        ConfirmMessage.publishMessageAsync();


    }
    public static void  publishMessageIndividually() throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        //队列的声明
        String queueName = UUID.randomUUID().toString();
        /*
         * 生成一个队列
         * 1.队列名称
         * 2.队列里面的消息是否持久化(磁盘)默认情况消息存储在内存中
         * 3.该队列是否只提供一个消费者进行消费，是否进行消息共享，true可以多个消费者消费，false：只能消费一个
         * 4.是否自动删除  最后一个消费者端开始连接以后  该队一句是否自动删除  true自动删除 false不自动删除
         * 5.其他参数
         * */
        channel.queueDeclare(queueName,false,false,false,null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();
        /*
         * 发送一个消费
         * 1、发送到哪个交换机
         * 2、路由的key值是哪个  本次的队列的名称
         * 3、其他参数信息
         * 4、发送消息的消息体
         */
        for (int i =0;i<MESSAGE_COUNT;i++){
            String message = i + "";
            channel.basicPublish("",queueName,null,message.getBytes());
            //单个消息就马上进行发布确认
            boolean flag = channel.waitForConfirms();
            if(flag){
                System.out.println("发布消息成功");
            }
        }
        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布"+MESSAGE_COUNT+"个单独确认消息,耗时"+(end-begin)+"ms");
    }
    public static void  publishMessageBatch() throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        //队列的声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,false,false,false,null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();
        //批量确认消息大小
        int batchSize = 100;
        for (int i =0;i<MESSAGE_COUNT;i++){
            String message = i + "";
            channel.basicPublish("",queueName,null,message.getBytes());
            //100条消息才进行发布确认
            if(i % batchSize == 00){
                //发布确认
                channel.waitForConfirms();
            }
        }
        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布"+MESSAGE_COUNT+"个批量确认消息,耗时"+(end-begin)+"ms");
    }

    //异步发布确认
    public static void publishMessageAsync() throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        //队列的声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,false,false,false,null);
        //开启发布确认
        channel.confirmSelect();

        /*  37p如何处理异步未确认消息  四
        * 线程安全有序的一个哈希表  适用于高并发的情况下
        * 1.轻松的将序号与消息关联
        * 2.可以轻松的批量删除条目 只要给到序号
        * 3.支持高并发（多线程）
        * */
        ConcurrentSkipListMap<Long,String> outstandingConfirms = new ConcurrentSkipListMap<>();  //四

        //消息确认成功 回调函数
        ConfirmCallback ackCallback=(deliveryTag,multiple)->{
            if(multiple){ //七
                //2.删除已经确认的消息 剩下的就是未确认的消息  37p如何处理异步未确认消息 二、
                ConcurrentNavigableMap<Long, String> longStringConcurrentNavigableMap =
                        outstandingConfirms.headMap(deliveryTag);//六
                System.out.println("确认的消息"+deliveryTag);
            }
            else {
                outstandingConfirms.remove(deliveryTag);
            }

        };
        //消息确认失败 回调函数
        /*
        * 1.deliveryTag 消息的表示
        * 2.multiple 是否为批量确认
        * */
        ConfirmCallback nackCallback=(deliveryTag,multiple)->{
            //3.打印一下未确认的消息有哪些   37p如何处理异步未确认消息 三、
            String message = outstandingConfirms.get(deliveryTag);  //八
            System.out.println("未确认消息是："+message+"：：：：未确认消息"+deliveryTag);
        };
        /*
        * 1.监听哪些消息成功了
        * 2.监听哪些消息失败了
        * */
        //准备消息的监听器 监听哪些消息成功了 哪些消息失败了
        channel.addConfirmListener(ackCallback,nackCallback);//异步通知

        //开始时间  104行移到此处  37p如何处理异步未确认消息 一、
        long begin = System.currentTimeMillis();
        //批量发送消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "消息" + i;
            channel.basicPublish("",queueName,null,message.getBytes());
            //1.此处记录下所有要发送的消息 消息的综合  37p如何处理异步未确认消息
            outstandingConfirms.put(channel.getNextPublishSeqNo(),message); // 五
            System.out.println();

        }
        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布"+MESSAGE_COUNT+"个异步确认消息,耗时"+(end-begin)+"ms");
    }

}
