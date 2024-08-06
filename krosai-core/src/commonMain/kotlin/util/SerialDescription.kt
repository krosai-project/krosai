package io.github.krosai.client.ai.core.util

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InheritableSerialInfo

@OptIn(ExperimentalSerializationApi::class)
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.BINARY)
@InheritableSerialInfo
annotation class SerialDescription(val value: String)
