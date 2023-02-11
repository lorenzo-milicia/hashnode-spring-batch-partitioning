package dev.lorenzomilicia.springbatchpartitioning.steps

import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.partition.support.MultiResourcePartitioner
import org.springframework.batch.core.partition.support.Partitioner
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import java.io.File

@Configuration
class DataProcessingPartitioningStepConfiguration(
    private val stepBuilder: StepBuilderFactory,
    @Qualifier("dataProcessingStep")
    private val dataProcessingStep: Step,
) {

    private val resources: List<Resource> =
        File("src/main/resources/raw_data/").listFiles()?.map {
            FileSystemResource(it)
        } ?: emptyList()

    @Qualifier("dataProcessingPartitioningStep")
    @Bean
    fun dataProcessingPartitioningStep(): Step =
        stepBuilder
            .get("dataProcessingMultipleFiles")
            .partitioner("fileProcessingStep", partitioner(resources))
            .step(dataProcessingStep)
            .build()

    private fun partitioner(resources: List<Resource>): Partitioner =
        MultiResourcePartitioner().apply {
            setResources(resources.toTypedArray())
        }
}