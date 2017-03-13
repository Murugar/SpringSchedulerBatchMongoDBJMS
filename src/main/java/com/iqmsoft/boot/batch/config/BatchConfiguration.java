package com.iqmsoft.boot.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.jms.JmsItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

import com.iqmsoft.boot.batch.RunScheduler;

import lombok.extern.slf4j.Slf4j;

import javax.jms.Message;
import javax.jms.ObjectMessage;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Value("${batchSize}")
    private int batchSize;

    @Bean
    public JmsItemReader<Message> reader() {
        JmsItemReader<Message> reader = new JmsItemReader<>();
        reader.setJmsTemplate(jmsTemplate);
        reader.setItemType(Message.class);
        return reader;
    }


    @Bean
    public ItemProcessor<Message, Object> processor() {
        return new JmsProcessor();
    }

    @Bean
    public ObjectItemWriter writer() {
        return new ObjectItemWriter();
    }


    @Bean
    public Job testJob() {
        return jobBuilderFactory.get("testJob")
                .incrementer(new RunIdIncrementer())
                .flow(step1())
                .end()
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<Message, Object> chunk(batchSize)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }


    @Bean
    RunScheduler scheduler() {
        RunScheduler scheduler = new RunScheduler();
        scheduler.setJob(testJob());
        return scheduler;
    }

    private class JmsProcessor implements ItemProcessor<Message, Object> {

        @Override
        public String process(Message message) throws Exception {
        	log.info("Process: " + message);
            return messageToString(message);
        }

        private String messageToString(Message m) {
            try {
                ObjectMessage tm = (ObjectMessage)m;
                return m.getJMSMessageID() + " [" + tm.toString() + "]";
            } catch (Exception e) {
                return e.getMessage();
            }
        }
    }

    private class ObjectItemWriter implements ItemWriter<Object> {

      /*  @Override
        public void write(List<? extends String> list) throws Exception {
        	log.info("Get batch size: " + list.size());
        	log.info("> " + list.stream().collect(Collectors.joining(" | ")));
        }*/

		@Override
		public void write(List<? extends Object> arg0) throws Exception {
		
			log.info("Get batch size: " + arg0.size());
			log.info("Batch Info : " + arg0.stream().map(Object::toString).collect(Collectors.joining(" | ")));
	
		}
    }


}
