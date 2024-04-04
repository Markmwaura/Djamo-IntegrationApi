package com.djamo.IntegrationApi.Service

import com.djamo.IntegrationApi.DTO.ThirdPartyResponse
import com.djamo.IntegrationApi.DTO.TransactionRequest
import com.djamo.IntegrationApi.Util.ApiResult
import com.djamo.IntegrationApi.config.ApiWebClient
import com.djamo.IntegrationApi.config.WebClientConfig
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.*
import reactor.core.publisher.Mono

@Service("integratedClaimServiceImpl")
class TransactionServiceImpl(
    @Qualifier("sendTransactionWebClientConfig")
    val webClient: WebClient
) : ITransactionService {

    @Value("\${restClient.outboundThirdPartyUrl}")
    lateinit var outboundThirdPartyUrl: String

    override suspend fun sendTransaction(id: String, webhookUrl: String?): ThirdPartyResponse {
        val request = TransactionRequest(
            id = id,
            webhookUrl = webhookUrl
        )
        return webClient.post()
            .uri(outboundThirdPartyUrl)
            .headers { h -> h.setBearerAuth("no-auth") }
            .body(BodyInserters.fromValue(request))
            .awaitExchange {
                clientResponse ->
                return@awaitExchange if (clientResponse.statusCode() == HttpStatus.OK) {
                    clientResponse.awaitBody<ThirdPartyResponse>()
                }else {
                    throw clientResponse.createExceptionAndAwait()
                }
            }
    }


    override suspend fun receiveWebhookRequest(id: String, status: String?): Result<Boolean> {
        TODO("Not yet implemented")
    }
}