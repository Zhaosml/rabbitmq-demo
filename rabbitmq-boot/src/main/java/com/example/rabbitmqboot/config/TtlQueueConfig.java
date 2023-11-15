package com.example.rabbitmqboot.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 钊思暮想
 * @date 2023/11/13 23:24
 * <p>
 * Ttl队列    配置文件类代码
 */
@Configuration
public class TtlQueueConfig {
        public static final String X_EXCHANGE = "X";
        public static final String QUEUE_A = "QA";
        public static final String QUEUE_B = "QB";
        public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
        public static final String DEAD_LETTER_QUEUE = "QD";
        public static final String QUEUE_C = "QC";

        @Bean("queueC")
        public Queue queueC(){
            Map<String,Object> args = new HashMap<>(3);
            //设置死信交换机
            args.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
            //设置死信RoutingKey
            args.put("x-dead-letter-routing-key","YD");
            //设置TTL 单位是ms  这次不写了
            return QueueBuilder.durable(QUEUE_C).withArguments(args).build();
        }
        @Bean
        public Binding queueCBindingX(@Qualifier("queueC")Queue queue,
                                      @Qualifier("xExchange")DirectExchange xExchange){
            return BindingBuilder.bind(queueC()).to(xExchange).with("XC");
        }

        // 声明 xExchange
        @Bean("xExchange")
        public DirectExchange xExchange() {
            return new DirectExchange(X_EXCHANGE);
        }

        // 声明 xExchange
        @Bean("yExchange")
        public DirectExchange yExchange() {
            return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
        }

        @Bean("queueA")
        public Queue queueA() {
            Map<String, Object> args = new HashMap<>(3);
            //声明当前队列绑定的死信交换机
            args.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
            //声明当前队列的死信路由 key
            args.put("x-dead-letter-routing-key", "YD");
            //声明队列的 TTL
            args.put("x-message-ttl", 10000);
            return QueueBuilder.durable(QUEUE_A).withArguments(args).build();
        }


        //声明队列 A绑X交换机
        @Bean
        public Binding queueaBindingX(@Qualifier("queueA") Queue queueA,
                                      @Qualifier("xExchange") DirectExchange xExchange)
        {

            return BindingBuilder.bind(queueA).to(xExchange).with("XA");
        }

        //声明队列 Bttl为 40s并绑定到对应的死信交换机
        @Bean("queueB")
        public Queue queueB() {
            Map<String, Object> args = new HashMap<>(3);

        //声明当前队列绑定的死信交换机
            args.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        //声明当前队列的死信路由 key
            args.put("x-dead-letter-routing-key", "YD");
        //声明队列的 TTL
            args.put("x-message-ttl", 40000);
            return QueueBuilder.durable(QUEUE_B).withArguments(args).build();
        }


        //声明队列 B绑定X交换机
        @Bean
        public Binding queuebBindingX(@Qualifier("queueB") Queue queue1B,
                                      @Qualifier("xExchange") DirectExchange xExchange) {
            return BindingBuilder.bind(queue1B).to(xExchange).with("XB");
        }

        //声明死信队列 QD
        @Bean("queueD")
        public Queue queueD() {
            return new Queue(DEAD_LETTER_QUEUE);
        }

        //声明死信队列 QD绑定关系
        @Bean
        public Binding deadLetterBindingQAD(@Qualifier("queueD") Queue queueD,
                                            @Qualifier("yExchange") DirectExchange yExchange)

        {
            return BindingBuilder.bind(queueD).to(yExchange).with("YD");
        }
    }



//声明队列 A ttl为 10s并绑定到对应的死信交换机
            /*
             * 1.设置普通和死信交换机和队列名称
             * 2.声明交换机
             * */
            //
            ////普通交换机的名称
            //public static final String X_EXCHANGE = "X";
            ////死信交换机的名称
            //public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
            ////普通队列的名称
            //public static final String QUEUE_A = "QA";
            //public static final String QUEUE_B = "QB";
            ////死信队列的名称
            //public static final String DEAD_LETTER_QUEUE = "QD";
            //
            ////声明xExchange  别名
            //@Bean("xExchange")
            //public DirectExchange xExchange(){
            //    return new DirectExchange(X_EXCHANGE);
            //}
            ////声明yExchange  别名
            //@Bean("yExchange")
            //public DirectExchange yExchange(){
            //    return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
            //}
            //
            ////声明普通队列 Ttl为 10S
            //@Bean("queueA")
            //public Queue queueA(){
            //    Map<String,Object> arguments = new HashMap<>(3);
            //    //设置死信交换机
            //    arguments.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
            //    //设置死信RoutingKey
            //    arguments.put("x-dead-letter-routing-Key","YD");
            //    //设置TTL
            //    arguments.put("x-message-ttl",5000);
            //    return QueueBuilder.durable(QUEUE_A).withArguments(arguments).build();
            //}
            //
            ////声明普通队列 Ttl为 40S
            //@Bean("queueB")
            //public Queue queueB(){
            //    Map<String,Object> arguments = new HashMap<>(3);
            //    //设置死信交换机
            //    arguments.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
            //    //设置死信RoutingKey
            //    arguments.put("x-dead-letter-routing-Key","YD");
            //    //设置TTL
            //    arguments.put("x-message-ttl",10000);
            //    return QueueBuilder.durable(QUEUE_B).withArguments(arguments).build();
            //}
            //
            ////死信队列
            //@Bean("queueD")
            //public Queue queueD(){
            //    return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
            //}
            //
            ////绑定
            //@Bean
            //public Binding queueABinDingX(@Qualifier("queueA")Queue queueA,
            //                              @Qualifier("xExchange")DirectExchange xExchange){
            //    return BindingBuilder.bind(queueA).to(xExchange).with("XA");
            //}
            //@Bean
            //public Binding queueBBinDingX(@Qualifier("queueB")Queue queueB,
            //                              @Qualifier("xExchange")DirectExchange xExchange){
            //    return BindingBuilder.bind(queueB).to(xExchange).with("XB");
            //}
            //@Bean
            //public Binding queueDBinDingY(@Qualifier("queueD")Queue queueD,
            //                              @Qualifier("yExchange")DirectExchange yExchange){
            //    return BindingBuilder.bind(queueD).to(yExchange).with("YD");
            //}


