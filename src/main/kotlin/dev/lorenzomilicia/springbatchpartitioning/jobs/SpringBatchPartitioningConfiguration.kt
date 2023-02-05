package dev.lorenzomilicia.springbatchpartitioning.jobs

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableBatchProcessing
@Configuration
class SpringBatchPartitioningConfiguration(
	private val jobBuilder: JobBuilderFactory,
	@Qualifier("dataProcessingStep")
	private val step: Step,
) {

	@Bean
	fun job(): Job =
		jobBuilder
			.get("dataProcessing")
			.incrementer(RunIdIncrementer())
			.start(step)
			.build()
}