package com.iqmsoft.boot.batch.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

import com.iqmsoft.boot.batch.JmsSender;


import javax.jms.ConnectionFactory;


@Configuration
@EnableJms
public class JmsConfiguration {

    @Value("${queue}")
    private String queue;

    @Value("${brokerUrl}")
    private String brokerUrl;
    
    @Value("${receiveJmsDelay}")
    private long receiveDelay;
    
    @Value("${batchPack}")
    private int pack;

    @Bean
    public ActiveMQQueue inputQueue() {
        return new ActiveMQQueue(queue);
    }

   
    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory());
        jmsTemplate.setDefaultDestination(inputQueue());
        jmsTemplate.setReceiveTimeout(receiveDelay);
        return jmsTemplate;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(brokerUrl);

        CachingConnectionFactory bean = new CachingConnectionFactory(activeMQConnectionFactory);
        bean.setSessionCacheSize(5);
        return bean;
    }


    @Bean
    public JmsSender sender() {
        JmsSender sender = new JmsSender();
        sender.setCount(this.pack);
        return sender;
    }


   
}
