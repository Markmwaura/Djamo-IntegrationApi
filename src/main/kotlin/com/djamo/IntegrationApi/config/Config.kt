package com.djamo.IntegrationApi.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient


@Configuration
class WebClientConfig(
    @Value("\${restClient.outboundThirdPartyUrl}")
    val outboundThirdPartyUrl: String,
    @Value("\${restClient.inboundWebhookUrl}")
    val inboundWebhookUrl: String,
) {
    @Bean
    @Qualifier("sendTransactionWebClientConfig")
    fun sendTransactionWebClient(webClientBuilder: WebClient.Builder): WebClient? {
        return webClientBuilder
            .baseUrl(outboundThirdPartyUrl)
            .build()
    }
}
