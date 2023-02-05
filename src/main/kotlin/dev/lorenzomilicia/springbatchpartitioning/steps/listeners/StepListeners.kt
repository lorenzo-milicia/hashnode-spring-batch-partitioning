package dev.lorenzomilicia.springbatchpartitioning.steps.listeners

import org.slf4j.LoggerFactory
import org.springframework.batch.core.ChunkListener
import org.springframework.batch.core.scope.context.ChunkContext


fun chunkListener(): ChunkListener =
    object : ChunkListener {
        private val log = LoggerFactory.getLogger("ChunkListener")
        override fun beforeChunk(context: ChunkContext) {}

        override fun afterChunk(context: ChunkContext) {
            val stepExecution = context.stepContext.stepExecution
            log.info("Chunk finished - ReadCount=${stepExecution.readCount}, WriteCount=${stepExecution.writeCount}")
        }

        override fun afterChunkError(context: ChunkContext) {
            log.error("Chunk failed!")
        }
    }