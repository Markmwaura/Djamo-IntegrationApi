package com.djamo.IntegrationApi.Service

import com.djamo.IntegrationApi.DTO.*
import com.djamo.IntegrationApi.Util.ResultFactory
import org.redisson.api.RMap
import org.redisson.api.RedissonClient
import org.redisson.codec.TypedJsonJacksonCodec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.awaitExchange
import org.springframework.web.reactive.function.client.createExceptionAndAwait

@Service("ThirdPartyServiceImpl")
class IntegrationServiceImpl(
    @Qualifier("integrationWebClientConfig")
    val webClient: WebClient
) : IThirdPartyTransactionService {

    @Autowired
    protected var client: RedissonClient? = null

    @Value("\${restClient.outboundThirdPartyUrl}")
    lateinit var outboundThirdPartyUrl: String

    @Value("\${restClient.inboundWebhookUrl}")
    lateinit var inboundWebhookUrl: String

    @Value("\${restClient.outboundClientUrl}")
    lateinit var outboundClientUrl: String


    override suspend fun sendDataToThirdParty(id: String, webhookUrl: String?): ThirdPartyResponse {

        val transactionCodec = TypedJsonJacksonCodec(String::class.java, TransactionDTO::class.java)
        var redisMap: RMap<String, TransactionDTO>? = this.client!!.getMap("transactions", transactionCodec)

        if(this.client !== null){
            if (redisMap != null) {
                //Check If request exists/existed return response from cache
                if( redisMap.containsKey(id)){
                    val mapValue = redisMap.get(id)
                    val existingThirdPartyResponse = ThirdPartyResponse(
                        id = mapValue!!.id.toString(),
                        status = mapValue.status.toString()

                    )
                    return existingThirdPartyResponse
                }else{
                    //proceed to make api call to third party
                    val transaction = TransactionDTO(id, "$inboundWebhookUrl/api/v1/integration/transaction/webhook","")
                    redisMap.put(id, transaction)
                }
            }
        }

        val request = TransactionRequest(
            id = id,
            webhookUrl = "$inboundWebhookUrl/api/v1/integration/transaction/webhook"
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
        val transactionCodec = TypedJsonJacksonCodec(String::class.java, TransactionDTO::class.java)

        val redisMap: MutableMap<String, TransactionDTO>? = this.client!!.getMap("transactions", transactionCodec)

        val transaction = TransactionDTO(id, "",status)

//        Update Redis Cache
        redisMap?.put(id, transaction)

        val clientRes = ClientResponseDTO(
            id = id,
            status = status
        )
//        Send Response back to Client
        sendUpdateToClient(clientRes)
        return ResultFactory.getSuccessResult(data = true)
    }

     suspend fun sendUpdateToClient(clientResponseDTO: ClientResponseDTO): Result<Boolean> {

         return webClient.put()
             .uri(outboundClientUrl)
             .body(BodyInserters.fromValue(clientResponseDTO))
             .awaitExchange {
                     clientResponse ->
                 return@awaitExchange if (clientResponse.statusCode() == HttpStatus.OK) {
                     ResultFactory.getSuccessResult(data = true
                     )
                 }else {
                     throw clientResponse.createExceptionAndAwait()
                 }
             }
    }
}