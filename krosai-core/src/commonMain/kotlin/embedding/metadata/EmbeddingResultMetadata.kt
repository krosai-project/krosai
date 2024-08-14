package org.krosai.core.embedding.metadata

import org.krosai.core.model.ResultMetadata

enum class ModalityType {
    TEXT,
    IMAGE,
    AUDIO,
    VIDEO
}

data class EmbeddingResultMetadata(
    val modality: ModalityType = ModalityType.TEXT,
    val documentId: String = "",
    val documentType: String = "text/plain",
    val documentData: Any? = null
): ResultMetadata