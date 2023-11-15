package com.example.rabbitmqdemo.eight;

import com.example.rabbitmqdemo.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author 钊思暮想
 * @date 2023/11/13 15:56
 *
 * 死信队列
 *
 * 消费者1
 */
public class Consumer01 {
    //普通交换机的名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    //死信交换机的名称
    public static final String DEAD_EXCHANGE = "dead_exchange";
    //普通队列的名称
    public static final String NORMAL_QUEUE = "normal_queue";
    //死信队列的名称
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明死信和普通交换机  类型direct
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        //正常队列绑定死信交换机与 routingkey
        Map<String, Object> arguments = new HashMap<>(); //arguments 参数

        //过期时间  10s = 10000ms
        //arguments.put("x-message-ttl",10000);

        //正常队列设置死信交换机
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        //设置私信RoutingKey
        arguments.put("x-dead-letter-routing-key","list");
        //2.设置正常队列的长度的限制
        //arguments.put("x-max-length",6);

        //声明普通队列
        channel.queueDeclare(NORMAL_QUEUE,false,false,false,arguments);

        /****************************************************************************************/
        //声明死信队列
        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);

        //绑定普通的交换机与普通的队列
        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"zhangsan");
        //绑定死信的交换机与普通的队列
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"list");
        System.out.println("等待接收.....");



        DeliverCallback deliverCallback = (consumerTag,message)->{
            //System.out.println("Consumer01接受的消息是"+new String(message.getBody(),"UTF-8"));
            String msg = new String(message.getBody(),"UTF-8");
            if(msg.equals("info5")){
                System.out.println("Consumer01接受的消息是:"+msg+":此消息是被C1拒绝的");
                channel.basicReject(message.getEnvelope().getDeliveryTag(),false);
            }
            else {
                System.out.println("Consumer01接受的消息是:"+msg);
            }
            System.out.println("Consumer01接受的消息是:"+msg);
        };
        //开启手动应答  false  消息被拒
        channel.basicConsume(NORMAL_QUEUE,false,deliverCallback, consumerTag -> {});

    }
}
