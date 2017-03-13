package com.iqmsoft.boot.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;


@Slf4j
public class RunScheduler {

    @Autowired
    private JobLauncher jobLauncher;

    private Job job;

    @Scheduled(initialDelay = 3000L, fixedRateString = "${restartJobDelay}")
    public void run() {
        try {

            String dateParam = new Date().toString();
            JobParameters param =
                    new JobParametersBuilder().addString("date", dateParam).toJobParameters();

            log.info(dateParam);

            JobExecution execution = jobLauncher.run(job, param);
            log.info("Exit Status : " + execution.getStatus());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }
}
