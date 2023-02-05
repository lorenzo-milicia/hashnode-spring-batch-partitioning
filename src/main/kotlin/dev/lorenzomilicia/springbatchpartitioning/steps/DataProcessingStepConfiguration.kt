package dev.lorenzomilicia.springbatchpartitioning.steps

import dev.lorenzomilicia.springbatchpartitioning.domain.CountryRawData
import dev.lorenzomilicia.springbatchpartitioning.domain.CountrySummedData
import dev.lorenzomilicia.springbatchpartitioning.mappers.toDomain
import dev.lorenzomilicia.springbatchpartitioning.mappers.toOutputText
import dev.lorenzomilicia.springbatchpartitioning.steps.listeners.chunkListener
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.FlatFileItemWriter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource

@Configuration
class DataProcessingStepConfiguration(
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
					FileSystemResource("src/main/resources/raw_data/co2_emissions_tonnes_per_person.csv")
				))
			.processor(processor())
			.writer(writer(FileSystemResource("exportedData/outputData.txt")))
			.listener(chunkListener())
			.build()

	fun reader(file: Resource): ItemReader<CountryRawData> =
		FlatFileItemReader<CountryRawData>()
			.apply {
				setResource(file)
				setLinesToSkip(1)
				setLineMapper { line, _ ->
					Thread.sleep(50)
					line.toDomain()
				}
			}
	fun processor(): ItemProcessor<CountryRawData, CountrySummedData> =
		ItemProcessor { (countryName, yearlyData) ->
			Thread.sleep(10)
			CountrySummedData(
				countryName = countryName,
				totalAmount = yearlyData.sumOf { it }
			)
		}
	fun writer(outputFile: Resource): ItemWriter<CountrySummedData> =
		FlatFileItemWriter<CountrySummedData>().apply {
			setResource(outputFile)
			setAppendAllowed(true)
			setLineAggregator {
				it.toOutputText()
			}
		}

}