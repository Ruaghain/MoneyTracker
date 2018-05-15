package com.wraith.money.web.configuration;

import com.wraith.money.data.entity.Transaction;
import com.wraith.money.repository.TransactionRepository;
import com.wraith.money.web.processor.MoneyTransaction;
import com.wraith.money.web.processor.TransactionProcessor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import javax.inject.Inject;

/**
 * User: rowan.massey
 * Date: 11/09/2014
 */
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Inject
	private TransactionRepository transactionRepository;
	@Inject
	private JobRepository jobRepository;

	@Bean
	@StepScope
	public FlatFileItemReader<MoneyTransaction> reader(@Value("#{jobParameters[targetFile]}") String file) {
		FlatFileItemReader<MoneyTransaction> reader = new FlatFileItemReader<>();
		reader.setResource(new FileSystemResource(file));
		reader.setLineMapper(new DefaultLineMapper<MoneyTransaction>() {
								 {
									 setLineTokenizer(new DelimitedLineTokenizer() {
														  {
															  setNames(new String[]{"Number", "Date", "Account", "Payee", "Cleared", "Amount", "Category", "Subcategory", "Memo"});
														  }
													  }
									 );
									 setFieldSetMapper(new BeanWrapperFieldSetMapper<MoneyTransaction>() {
										 {
											 setTargetType(MoneyTransaction.class);

										 }
									 });
								 }
							 }
		);
		reader.setLinesToSkip(1);
		return reader;
	}

	@Bean
	public ItemProcessor<MoneyTransaction, Transaction> processor() {
		return new TransactionProcessor();
	}

	@Bean
	public RepositoryItemWriter writer() {
		RepositoryItemWriter writer = new RepositoryItemWriter();
		writer.setRepository(transactionRepository);
		writer.setMethodName("save");
		return writer;
	}

	@Bean
	public Step step(StepBuilderFactory stepBuilderFactory, ItemReader<MoneyTransaction> reader,
					 ItemWriter<Transaction> writer, ItemProcessor<MoneyTransaction, Transaction> processor) {
		return stepBuilderFactory.get("step")
				.<MoneyTransaction, Transaction>chunk(100)
				.reader(reader)
				.processor(processor)
				.writer(writer)
				.build();
	}

	@Bean
	public SimpleAsyncTaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor();
		executor.setConcurrencyLimit(1);
		return executor;
	}

	@Bean
	public SimpleJobLauncher jobLauncher() {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(jobRepository);
		jobLauncher.setTaskExecutor(taskExecutor());
		return jobLauncher;
	}
}
