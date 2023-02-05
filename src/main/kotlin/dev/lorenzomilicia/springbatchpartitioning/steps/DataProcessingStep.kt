package dev.lorenzomilicia.springbatchpartitioning.steps

import dev.lorenzomilicia.springbatchpartitioning.domain.CountryRawData
import dev.lorenzomilicia.springbatchpartitioning.domain.CountrySummedData
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource

@Configuration
class DataProcessingStep(
	private val stepBuilder: StepBuilderFactory,
) {

	@Qualifier("dataProcessingStep")
	@Bean
	fun dataProcessingStep(): Step =
		stepBuilder
			.get("dataProcessingSingleFile")
			.chunk<CountryRawData, CountrySummedData>(10)
			.reader(
				reader(
					FileSystemResource("resources/raw_data/co2_emissions_tonnes_per_person.csv")
				))
			.processor(processor())
			.writer(writer())
			.build()

	fun reader(file: Resource): ItemReader<CountryRawData> = TODO()
	fun processor(): ItemProcessor<CountryRawData, CountrySummedData> = TODO()
	fun writer(): ItemWriter<CountrySummedData> = TODO()
}