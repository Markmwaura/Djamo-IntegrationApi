package com.djamo.IntegrationApi.DTO

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.serialization.Serializable


data class Result<T>(
    val success: Boolean,
    val msg: String? = null,
    val data: T? = null
)

data class ThirdPartyResponse(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("status")
    val status: String,
    val success: Boolean = true
)

data class TransactionRequest(
    val id: String,
    val webhookUrl: String?,
)

data class WebhookDTO(
    val id: String,
    val status: String?,
)

@Serializable
 data class TransactionDTO(
    val id: String?="",
    val webhookUrl: String?="",
    val status: String?="",
)

data class ClientResponseDTO(
    val id: String?,
    val status: String?,
)