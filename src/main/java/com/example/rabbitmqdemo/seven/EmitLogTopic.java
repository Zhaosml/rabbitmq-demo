package com.example.rabbitmqdemo.seven;

import com.example.rabbitmqdemo.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 钊思暮想
 * @date 2023/11/12 20:39
 *
 * 生产者
 * 发送主题日志
 */
public class EmitLogTopic {
    //交换机的名称
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        /*
        * 下图绑定关系如下
                Q1-->绑定的是
                    中间带orange 带 3 个单词的字符串(*.orange.*)
                Q2-->绑定的是
                    最后一个单词是 rabbit 的 3 个单词(*.*.rabbit)
                    第一个单词是 lazy 的多个单词(lazy.#)
        * */
        Map<String,String> bindingKeyMap = new HashMap<>();
        bindingKeyMap.put("quick.orange.rabbit","被队列 Q1Q2接收到");
        bindingKeyMap.put("lazy.orange.elephant","被队列 Q1Q2接收到");
        bindingKeyMap.put("quick.orange.fox","被队列 Q1接收到");
        bindingKeyMap.put("lazy.brown.fox","被队列 Q2接收到");
        bindingKeyMap.put("lazy.pink.rabbit","虽然满足两个绑定但只被队列 Q2接收一次");
        bindingKeyMap.put("quick.brown.fox","不匹配任何绑定不会被任何队列接收到会被丢弃");
        bindingKeyMap.put("quick.orange.male.rabbit","是四个单词不匹配任何绑定会被丢弃");

        for (Map.Entry<String, String> bindingKeyEntry : bindingKeyMap.entrySet()) {
            String routingKey = bindingKeyEntry.getKey();
            String message = bindingKeyEntry.getValue();
            channel.basicPublish(EXCHANGE_NAME,routingKey,null,message.getBytes("UTF-8"));
            System.out.println("生产者发出消息："+message);

        }

    }
}
