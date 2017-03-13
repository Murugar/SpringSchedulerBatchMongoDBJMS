package com.iqmsoft.boot.batch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import com.iqmsoft.boot.batch.repos.MsgRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JmsSender {

    @Autowired
    private JmsTemplate jmsTemplate;
    
    @Autowired
    private MsgRepository msgrepos;

    private static int count = 1;


    @Scheduled(initialDelay = 3000L, fixedRateString = "${sendJmsDelay}")
    public void main() {

        for (int i = 1; i <= count; i++) {
            final int j = i;
            
            MyMessage m = new MyMessage("test", "This is a test " + j);
            
            
            
            getJmsTemplate().
            send(getJmsTemplate().getDefaultDestination(), 
            		session -> session.createObjectMessage(m));
            
            msgrepos.save(m);
            		
        }
        log.info("send  " + count + " jms");
    }

    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        JmsSender.count = count;
    }
}
