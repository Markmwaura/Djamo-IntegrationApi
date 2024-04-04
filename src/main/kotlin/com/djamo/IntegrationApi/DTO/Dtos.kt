package com.djamo.IntegrationApi.DTO

import com.fasterxml.jackson.annotation.JsonProperty


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