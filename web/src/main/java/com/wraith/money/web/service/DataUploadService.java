package com.wraith.money.web.service;

import com.wraith.money.web.configuration.MoneyRunIdIncrementer;
import com.wraith.money.web.exception.MoneyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

/**
 * This service class deals with all data upload requirements. This is the entry point for all the relevant processing.
 * <p/>
 * User: rowan.massey
 * Date: 26/08/2014
 * Time: 21:11
 */
@Service
public class DataUploadService {

    private static final String TEMP_DIRECTORY = "java.io.tmpdir";
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    private JobLauncher jobLauncher;

//    @Inject
//    private JobOperator operator;

    private JobBuilderFactory jobs;
    private Step step;


    @Inject
    public DataUploadService(JobBuilderFactory jobBuilderFactory, Step step) {
        this.jobs = jobBuilderFactory;
        this.step = step;
    }

    /**
     * This method processes the file that was passed in during the file upload.
     *
     * @param file     The file that was uploaded.
     * @param fileType This is the type of file that will be processed, currently either "MSMoney", or "AIB"
     * @return The job execution object.
     */
    public JobExecution performUploadData(MultipartFile file, String fileType) {
        logger.debug(String.format("Starting process of '%s' for upload, and import.", file.getOriginalFilename()));
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String rootPath = System.getProperty(TEMP_DIRECTORY);
                File uploadDirectory = new File(rootPath.concat(File.separator).concat("uploads"));
                if (!uploadDirectory.exists()) {
                    uploadDirectory.mkdirs();
                }
                File uploadFile = new File(rootPath.concat(File.separator).concat(file.getOriginalFilename()));
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(uploadFile));
                stream.write(bytes);
                stream.close();
                return startImportJob(uploadFile, fileType + Calendar.getInstance().getTimeInMillis());
            } catch (Exception e) {
                logger.error(String.format("Error processing file '%s'.", file.getOriginalFilename()), e);
                throw new MoneyException(e);
            }
        } else {
            throw new MoneyException("There was no file to process.");
        }
    }

    /**
     * This method starts the import of the given job, which will upload the given file.
     *
     * @param file    The file that will be processed.
     * @param jobName The name of the job.
     * @return The JobExecution object.
     */
    private JobExecution startImportJob(File file, String jobName) {
        logger.debug(String.format("Starting job to import file '%s'.", file));
        try {
            Job job = jobs.get(jobName).incrementer(new MoneyRunIdIncrementer()).flow(step).end().build();
            return jobLauncher.run(job, new JobParametersBuilder().addString("targetFile", file.getAbsolutePath()).toJobParameters());
        } catch (JobExecutionAlreadyRunningException e) {
            logger.error(String.format("Job for processing file '%s' is already running.", file), e);
            throw new MoneyException(e);
        } catch (JobParametersInvalidException e) {
            logger.error(String.format("Invalid parameters for processing of file '%s'.", file), e);
            throw new MoneyException(e);
        } catch (JobRestartException e) {
            logger.error(String.format("Error restarting job, for processing file '%s'.", file), e);
            throw new MoneyException(e);
        } catch (JobInstanceAlreadyCompleteException e) {
            logger.error(String.format("Job to process file '%s' has already completed.", file), e);
            throw new MoneyException(e);
        }
    }

    /**
     *
     */
    public void stopJob() {

    }

    /**
     * @param job
     */
    public void stopAllExecutions(String job) {
//        Set<Long> runningJobs;
//        try {
//            runningJobs = operator.getRunningExecutions(job);
//            if (!runningJobs.isEmpty()) {
//                for (Long executionId : runningJobs) {
//                    operator.abandon(executionId);
//                }
//            }
//        } catch (NoSuchJobException | NoSuchJobExecutionException e) {
//            logger.error(String.format("Job '%s' does not exist.", job), e);
//            throw new MoneyException(e);
//        } catch (JobExecutionAlreadyRunningException e) {
//            logger.error(String.format("Job '%s' is already running.", job), e);
//            throw new MoneyException(e);
//        }
    }
}
