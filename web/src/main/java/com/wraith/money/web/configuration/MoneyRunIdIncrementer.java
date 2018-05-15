package com.wraith.money.web.configuration;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;

import java.util.Calendar;

/**
 * User: rowan.massey
 * Date: 13/09/2014
 * Time: 23:21
 */
public class MoneyRunIdIncrementer extends RunIdIncrementer {

    private static String RUN_ID_KEY = "run.id";

    private String key = RUN_ID_KEY;

    @Override
    public JobParameters getNext(JobParameters parameters) {

        JobParameters params = (parameters == null) ? new JobParameters() : parameters;

        long id = params.getLong(key, 0L) + 1;
        return new JobParametersBuilder(params).addLong(key, id).addDate("run.date", Calendar.getInstance().getTime()).toJobParameters();
    }
}
