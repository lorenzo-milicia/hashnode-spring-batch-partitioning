package dev.lorenzomilicia.springbatchpartitioning.steps

import dev.lorenzomilicia.springbatchpartitioning.domain.CountryRawData
import dev.lorenzomilicia.springbatchpartitioning.domain.CountrySummedData
import dev.lorenzomilicia.springbatchpartitioning.mappers.toDomain
import dev.lorenzomilicia.springbatchpartitioning.mappers.toOutputText
import dev.lorenzomilicia.springbatchpartitioning.steps.listeners.chunkListener
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.FlatFileItemWriter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.UrlResource

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
            .reader(reader(""))
            .processor(processor())
            .writer(writer(""))
            .listener(chunkListener())
            .build()

    @Bean
    @StepScope
    fun reader(@Value("#{stepExecutionContext[fileName]}") pathToFile: String): FlatFileItemReader<CountryRawData> =
        FlatFileItemReader<CountryRawData>()
            .apply {
                setResource(UrlResource(pathToFile))
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

    @Bean
    @StepScope
    fun writer(@Value("#{stepExecutionContext[fileName]}") pathToFile: String): FlatFileItemWriter<CountrySummedData> =
        FlatFileItemWriter<CountrySummedData>().apply {
            setResource(FileSystemResource("exportedData/output_${pathToFile.substringAfterLast('/')}.txt"))
            setAppendAllowed(true)
            setLineAggregator {
                it.toOutputText()
            }
        }

}